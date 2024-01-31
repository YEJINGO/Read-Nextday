package readnextday.readnextdayproject.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import readnextday.readnextdayproject.api.member.dto.request.ChangePasswordRequest;
import readnextday.readnextdayproject.api.member.dto.request.FindPassword;
import readnextday.readnextdayproject.api.member.dto.request.FindUser;
import readnextday.readnextdayproject.api.member.serivce.MemberFindService;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;

@RestController
@RequestMapping("/api/member/find")
@RequiredArgsConstructor
public class MemberFindController {

    private final MemberFindService memberFindService;

    //메일로 아이디 보내기
    @PostMapping("/username")
    @ResponseStatus(code = HttpStatus.OK)
    public Response<Void> sendEmail(@RequestBody FindUser findUser) {
        return memberFindService.sendEmail(findUser);
    }

    @PostMapping("/password")
    @ResponseStatus(code = HttpStatus.OK)
    public Response<Void> findPassword(@RequestBody FindPassword findPassword) throws Exception {
        return memberFindService.findPassword(findPassword);
    }

    // 비밀번호 바꾸기
    @PostMapping("/changePassword")
    @ResponseStatus(code = HttpStatus.OK)
    public Response<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                         @AuthenticationPrincipal LoginMember loginMember) throws Exception {
        return memberFindService.changePassword(changePasswordRequest, loginMember);
    }
}
