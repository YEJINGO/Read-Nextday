package readnextday.readnextdayproject.api.member.serivce;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.member.dto.request.ChangePasswordRequest;
import readnextday.readnextdayproject.api.member.dto.request.FindPassword;
import readnextday.readnextdayproject.api.member.dto.request.FindUser;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberFindService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String setEmail = "yaejingo@naver.com";


    public Response<Void> sendEmail(FindUser findUser) {
        Member findMember = memberRepository.findByEmail(findUser.getEmail()).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        try {
            if (findMember != null) {
                String email = findMember.getEmail();
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setTo(findMember.getEmail());
                simpleMailMessage.setSubject("이메일 찾기");
                simpleMailMessage.setText("가입하신 이메일은 " + email + "입니다.");
                simpleMailMessage.setFrom(setEmail);
                mailSender.send(simpleMailMessage);
            } else {
                throw new Exception("이메일이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시 처리 로직
            e.printStackTrace(); // 혹은 로깅 등을 수행
        }
        return Response.success("이메일 전송 성공");
    }

    public Response<Void> findPassword(FindPassword findPassword) throws Exception {
        String email = findPassword.getEmail();
        String nickname = findPassword.getNickname();

        Member findMember = memberRepository.findByEmailAndNickname(email, nickname).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        if (findMember != null) {
            // 값이 존재하는 경우 처리
            String str = getTempPassword();
            findMember.setPassword(bCryptPasswordEncoder.encode(str));
            memberRepository.save(findMember);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(findMember.getEmail());
            simpleMailMessage.setSubject("임시 비밀번호 안내 이메일 입니다.");
            simpleMailMessage.setText("안녕하세요. 임시 비밀번호 안내 관련 이메일 입니다.\n" + "회원님의 임시 비밀번호는 " + str + " 입니다. \n" + "로그인 후 비밀 변호를 변경해주세요.");
            simpleMailMessage.setFrom(setEmail);
            mailSender.send(simpleMailMessage);

        } else {
            throw new Exception("아이디가 존재하지 않습니다.");
        }
        return Response.success("임시 비밀번호 발급");
    }

    public Response<Void> changePassword(ChangePasswordRequest changePasswordRequest,
                                         LoginMember loginMember) throws Exception {


        Member member = memberRepository.findByEmail(loginMember.getMember().getEmail()).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        String newPassword = changePasswordRequest.getNewPassword();
        String currentPassword = loginMember.getPassword();
        String registeredPassword = member.getPassword();

        if (bCryptPasswordEncoder.matches(currentPassword, registeredPassword)) {
            member.passwordUpdate(bCryptPasswordEncoder.encode(newPassword));
            memberRepository.save(member);
        } else {
            throw new Exception("현재 비밀번호가 일치하지 않습니다");
        }
        return Response.success("비밀번호 변경 완료");
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}
