package aug.laundry.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Slf4j
@Aspect
@Component
public class FooterAspect {

    @Before("execution(* aug.laundry.controller.MypageController_osc.*(..))")
    public void footerMyPageAspect(JoinPoint joinPoint) {

        log.info("MyPageController Aspect Before 실행 : {}", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model){
                ((Model) arg).addAttribute("footer", "mypage");
                break;
            }
        }


    }
}
