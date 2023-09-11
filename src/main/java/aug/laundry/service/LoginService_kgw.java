package aug.laundry.service;

import aug.laundry.dto.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public interface LoginService_kgw {

    public void naverLogin(HttpServletRequest request, Model model, HttpSession session);

    public void kakaoProcess(String code, HttpSession session);

    public int registerSocialUser(MemberDto memberDto);

    public int registerSocialNumber(String id);

    public MemberDto login(MemberDto memberDto, HttpSession session);

}
