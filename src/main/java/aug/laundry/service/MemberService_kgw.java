package aug.laundry.service;

import aug.laundry.domain.Member;
import aug.laundry.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Service
public interface MemberService_kgw {
    public MemberDto selectOne(Long memberId);

    public int checkId(String memberAccount);

    public Integer registerUser(MemberDto memberDto);

    public int inviteCodeCheck(String inviteCode);

    public List<MemberDto> confirmId(String memberName, String memberPhone);





}
