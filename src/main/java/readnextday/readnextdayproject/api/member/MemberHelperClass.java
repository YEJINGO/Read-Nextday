package readnextday.readnextdayproject.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.MemberRepository;
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberHelperClass {

    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

}
