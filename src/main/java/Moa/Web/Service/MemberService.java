package Moa.Web.Service;

import Moa.Web.DTO.RegenerateTokenDTO;
import Moa.Web.DTO.TokenInfo;
import Moa.Web.Entity.Member;
import Moa.Web.Entity.MemberTag;
import Moa.Web.Entity.Tag;
import Moa.Web.Jwt.JwtTokenProvider;
import Moa.Web.Repository.MemberRepository;
import Moa.Web.Repository.MembertagRepository;
import Moa.Web.Repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MembertagRepository membertagRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public ResponseEntity<TokenInfo> login(String address, String password) throws Exception{
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(address, password);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

            //redis에 저장, 만료시간 설정 -> 자동삭제처리
            redisTemplate.opsForValue()
                    .set("RefreshToken:" + authentication.getName(), tokenInfo.getRefreshToken(),
                            86400000, TimeUnit.MILLISECONDS);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + tokenInfo.getAccessToken());

            return new ResponseEntity<>(tokenInfo, httpHeaders, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new Exception("Invalid credential");
        }


    }

    public ResponseEntity<TokenInfo> regenerateToken(RegenerateTokenDTO regenerateTokenDTO) throws Exception{
        String refreshToken = regenerateTokenDTO.getRefreshToken();
        try{
            if(!jwtTokenProvider.validateToken(refreshToken)){
                throw new Exception("Invalid refresh token");
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

            //Redis에 저장된 토큰과 검증
            String savedToken = redisTemplate.opsForValue().get("RefreshToken:" + authentication.getName()).toString();
            if(!refreshToken.equals(savedToken)){
                throw new Exception("Refresh Token doesn't match");
            }

            //토큰 재발행
            TokenInfo newToken = jwtTokenProvider.generateToken(authentication);

            //새로운 refreshToken Redis에 업데이트
            redisTemplate.opsForValue().set(authentication.getName(), newToken.getRefreshToken()
            ,86400000,TimeUnit.MILLISECONDS);

            HttpHeaders httpHeaders = new HttpHeaders();

            return new ResponseEntity<>(newToken,httpHeaders, HttpStatus.OK);



        } catch (AuthenticationException e){
            throw new Exception("Invalid refresh token supplied");
        }
    }

    public Member join(Member member){
        Optional<Member> findMembers = memberRepository.findByAddress(member.getAddress());
        if(!findMembers.isEmpty()){
            System.out.println("이미 존재하는 회원입니다.");
        }
        else{
            memberRepository.save(member);
        }
        return member;
    }


    public boolean addFormalTag(Member member, Tag tag){
        List<MemberTag> tagList = member.getMembertags();

        for(MemberTag memberTag : tagList){
            if(memberTag.getTag() == tag) return false;
            else continue;
        }
        membertagRepository.save(MemberTag.builder()
                .member(member)
                .tag(tag)
                .tagAmount(member.getMembertags().size()+1)
                .build());

        return true;


    }
}

