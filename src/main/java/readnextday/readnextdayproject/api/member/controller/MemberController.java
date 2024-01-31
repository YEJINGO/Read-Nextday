package readnextday.readnextdayproject.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import readnextday.readnextdayproject.api.member.dto.request.LoginRequest;
import readnextday.readnextdayproject.api.member.dto.request.RefreshTokenRequest;
import readnextday.readnextdayproject.api.member.dto.request.SignupRequest;
import readnextday.readnextdayproject.api.member.dto.response.LoginMemberResponse;
import readnextday.readnextdayproject.api.member.serivce.MemberService;
import readnextday.readnextdayproject.common.Response;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public Response<Void> signup(@RequestBody SignupRequest request) {
        return memberService.singup(request);
    }

    // 2. 로그인
    @PostMapping("/login")
    public Response<LoginMemberResponse> login(@RequestBody LoginRequest request) {
        return memberService.login(request);
    }

    //3. 토큰 재발급
    @PostMapping("/reissue")
    public Response<LoginMemberResponse> reissue(@RequestBody RefreshTokenRequest request) {
        return memberService.reissue(request);
    }
}
