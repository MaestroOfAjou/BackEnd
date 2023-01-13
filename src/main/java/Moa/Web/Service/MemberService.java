package Moa.Web.Service;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public TokenInfo login(String address, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(address,password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
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

