package Moa.Web.Controller;


import Moa.Web.DTO.MemberLoginRequestDto;
import Moa.Web.DTO.RegenerateTokenDTO;
import Moa.Web.DTO.TokenInfo;
import Moa.Web.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) throws Exception{
        return memberService.login(memberLoginRequestDto.getAddress(), memberLoginRequestDto.getPassword());

    }

    @PostMapping("/regenerate")
    public ResponseEntity<TokenInfo> regenerate(@RequestBody RegenerateTokenDTO regenerateTokenDTO) throws Exception{
        return memberService.regenerateToken(regenerateTokenDTO);
    }

    @PostMapping("/test")
    public String test(){
        return "success";
    }
}
