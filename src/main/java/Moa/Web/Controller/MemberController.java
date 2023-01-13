package Moa.Web.Controller;


import Moa.Web.DTO.MemberLoginRequestDto;
import Moa.Web.DTO.TokenInfo;
import Moa.Web.Service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto){
        String address = memberLoginRequestDto.getAddress();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(address,password);

        return tokenInfo;

    }

    @PostMapping("/test")
    public String test(){
        return "success";
    }
}
