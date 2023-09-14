package aug.laundry.intercept;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.AdminDto;
import aug.laundry.dto.MemberDto;
import aug.laundry.dto.QuickRiderDto;
import aug.laundry.dto.RiderDto;
import aug.laundry.service.LoginServiceImpl_kgw;
import aug.laundry.service.LoginService_kgw;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final LoginService_kgw loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession();

//        System.out.println("세션 아이디 : " + session.getId());
//        System.out.println("쿠키 : " + loginCookie);
//        System.out.println("쿠키의 sessionId값 : " + loginCookie.getValue());
//        System.out.println("loginService : " + loginService);
//        MemberDto memberDto = loginService.checkUserWithSessionId("7FE471AC9C7B61C7837B70BF9BF612C3");
//        System.out.println("memberDto : " +  memberDto);
        // controller에서 생성되었을 지 모를 쿠키를 꺼내온다.

        System.out.println("0========================================");
        if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
            System.out.println("1========================================");
            Cookie loginCookie = WebUtils.getCookie(request,"loginCookie");
            if(loginCookie != null){
                System.out.println("2========================================");
                String sessionId = loginCookie.getValue();
                MemberDto memberDto = loginService.checkUserWithSessionId(sessionId);
                System.out.println("==========memberId : "  + memberDto.getMemberId());
                if(memberDto != null){
                    session.setAttribute(SessionConstant.LOGIN_MEMBER, memberDto.getMemberId());
                    return true;
                }
            }
            log.info("인증되지않은 사용자 요청");
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        log.info("인증된 사용자");
        return true;
    }

}


