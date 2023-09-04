package aug.laundry.controller;

import aug.laundry.domain.CouponList;
import aug.laundry.dto.*;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.CategoryOption;
import aug.laundry.enums.category.MemberShip;
import aug.laundry.enums.category.Pass;
import aug.laundry.service.LaundryService;
import aug.laundry.validator.OrderPostValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LaundryController {

    private final LaundryService laundryService;
    private final OrderPostValidator orderPostValidator;

    @InitBinder("orderPost")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(orderPostValidator);
    }

    @GetMapping("/laundry/order")
    public String order(Model model, HttpServletRequest request) {
        MemberShip memberShip = laundryService.isPass(1L); // 패스 여부
        Long totalPrice = 3000L; // 배송비
        Long discount = 0L;
        OrderInfo info = laundryService.firstInfo(1L); // 빠른세탁, 드라이클리닝, 생활빨래, 수선 선택여부
        List<MyCoupon> coupon = laundryService.getCoupon(1L); // 내가 보유한 쿠폰
        Address address = laundryService.getAddress(1L); // 주소 가져오기
        DateForm dateForm = new DateForm(); // 날짜 가져오기

        totalPrice += info.getIsQuick() != null ? 4000 : 0; // 빠른세탁

        if (info.getIsCommon() != 0) {
            model.addAttribute("common", Category.BASIC);
            totalPrice += memberShip.apply(Category.BASIC.getPrice()); // 생활빨래 기본금액
            discount += Category.BASIC.getPrice() - memberShip.apply(Category.BASIC.getPrice()); // 할인금액
        }
        if (info.getIsDry() != 0) {
            List<Category> getDry = laundryService.getDry(1L);
            model.addAttribute("dry", getDry);
            Long dryTotalPrice = getDry.stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get();
            model.addAttribute("dryTotalPrice", dryTotalPrice);
            totalPrice += memberShip.apply(dryTotalPrice); // 드라이클리닝 총 금액
            discount += dryTotalPrice - memberShip.apply(dryTotalPrice); // 할인금액
        }
        if (info.getIsRepair() != 0) {
            List<Category> getRepair = laundryService.getRepair(1L);
            model.addAttribute("repair", getRepair);
            Long repairTotalPrice = getRepair.stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get();
            model.addAttribute("repairTotalPrice", repairTotalPrice);
            totalPrice += memberShip.apply(repairTotalPrice); // 수선 총 금액
            discount += repairTotalPrice - memberShip.apply(repairTotalPrice); // 할인금액
        }


        model.addAttribute("memberShip", memberShip.getCheck() == Pass.PASS ? 1 : null);
        model.addAttribute("totalPrice", Math.round(totalPrice / 100) * 100);
        model.addAttribute("discount", discount);
        model.addAttribute("quickLaundry", info.getIsQuick());
        model.addAttribute("info", getJoin(info.getIsDry(), info.getIsCommon(), info.getIsRepair()));
        model.addAttribute("infoPrice", info);
        model.addAttribute("dateTime", dateForm);
        model.addAttribute("address", address);
        model.addAttribute("coupon", coupon);
        model.addAttribute("couponCount", coupon.size());
        return "project_order_confirm";
    }



    @PostMapping("/laundry/order")
    public String orderPost(@Validated @ModelAttribute OrderPost orderPost, BindingResult bindingResult, Model model) {

        System.out.println("orderPost = " + orderPost);
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult in Controller = " + bindingResult);
            System.out.println(bindingResult.getAllErrors());
            return "redirect:/laundry/order";
        }
        laundryService.update(1L, orderPost.getCoupon(), orderPost); // 쿠폰 유효성 검사


        return "redirect:/";
    }

    @GetMapping("/members/{memberId}/coupons/select")
    public String selectCoupon(@PathVariable Long memberId, Model model, String takeDate) {
        List<MyCoupon> getCoupon = laundryService.getCoupon(memberId);
        model.addAttribute("memberId", memberId);
        model.addAttribute("coupon", getCoupon);

        return "project_use_coupon";
    }

    @NotNull
    private static String getJoin(Long isDry, Long isCommon, Long isRepair) {
        String[] strArr = new String[(int)(isDry + isCommon + isRepair)];
        int cnt = 0;
        if (isDry != 0) strArr[cnt++] = CategoryOption.DRYCLEANING.getTitle();
        if (isCommon != 0) strArr[cnt++] = CategoryOption.COMMON_LAUNDRY.getTitle();
        if (isRepair != 0) strArr[cnt++] = CategoryOption.REPAIR.getTitle();
        return String.join(",", strArr);
    }




}
