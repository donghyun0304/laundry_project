package aug.laundry.controller;



import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.ConfirmIdDto;
import aug.laundry.dto.MemberDto;
import aug.laundry.service.LoginService_kgw;
import aug.laundry.service.MemberServiceImpl_kgw;
import aug.laundry.service.MemberService_kgw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class MemberController {
    private final LoginService_kgw service;
    private final MemberService_kgw memberServce;

    @GetMapping("/naver_callback")
    public String naverLogin_callback(HttpServletRequest request, Model model, HttpSession session) {
        service.naverLogin(request, model, session);
        System.out.println("naverLogin =======");
        System.out.println("naver sessionId : " + session.getAttribute("memberId"));


        return "redirect:/members/" + session.getAttribute("memberId") + "/mypage";
    }
    @GetMapping("/kakaoLogin")
    public String kakaoLogin_Redirect(String code, Model model, HttpSession session){
        service.kakaoProcess(code, session);

        return "redirect:/members/" + session.getAttribute("memberId") + "/mypage";


    }


    @PostMapping("/loginAction")
    public  String login(MemberDto memberDto, HttpSession session, Model model) {
        MemberDto dto = service.login(memberDto, session);
        if (dto != null) {
            session.setAttribute(SessionConstant.LOGIN_MEMBER, dto.getMemberId());
            return "redirect:/members/" + session.getAttribute("memberId") + "/mypage";
        } else {
            model.addAttribute("memberAccount", memberDto.getMemberAccount());
            model.addAttribute("errorMsg", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            return "project_login";
        }
    }

<<<<<<< HEAD:src/main/java/aug/laundry/controller/MemberController.java
    @PostMapping("/find-account/confirm")
    public String confirmId(ConfirmIdDto confirmIdDto, Model model){
        List<MemberDto> list = memberServce.confirmId(confirmIdDto.getMemberName(), confirmIdDto.getMemberPhone());
        model.addAttribute("list", list);
        return "project_search_id_result";
    }

    @GetMapping("/find-account")
    public String goFindAccount(){
        return "project_search_id";
    }


=======
>>>>>>> parent of 25aae82 (충돌해결):src/main/java/aug/laundry/controller/LoginController.java
    @GetMapping
    public String goLogin(){
        return "project_login";
    }


}


















