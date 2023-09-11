package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dao.member.MemberMapper;
import aug.laundry.dto.*;
import aug.laundry.enums.category.Delivery;
import aug.laundry.enums.category.MemberShip;
import aug.laundry.enums.category.Pass;
import aug.laundry.service.LaundryService;
import aug.laundry.service.OrdersService_kdh;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/orders")
public class OrdersController_kdh {

    private final OrdersService_kdh ordersServiceKdh;
    private final LaundryService laundryService;
    private final MemberMapper memberMapper;

    @GetMapping
    public String orders(){


        return "project_order_list";
    }

    @GetMapping("/getList/{pageNo}")
    @ResponseBody
    public Map<String, Object> getOrdersList(@PathVariable("pageNo") int pageNo, Model model){

        Map<String, Object> ordersMap = new HashMap<>();

        Criteria cri = new Criteria();
        cri.setPageNo(pageNo, ordersServiceKdh.getTotalCount(1L)); //memberId 수정

        List<OrdersListResponseDto> list = ordersServiceKdh.findOrdersByMemberIdAndCri(cri, 1L);
        log.info("================list============={}", list);
        if(!list.isEmpty()) {
            ordersMap.put("res", "success");
            ordersMap.put("list", list);
            ordersMap.put("realEnd", cri.getRealEnd());
        }else {
            ordersMap.put("res", "fail");        }

        return ordersMap;
    }



    @GetMapping("/{ordersId}/payment")
    public String payOrder(@PathVariable Long ordersId,
                           @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false)Long memberId,
                           Model model){


        OrdersResponseDto ordersResponseDto = ordersServiceKdh.findByOrdersId(ordersId);

        if(memberId != ordersResponseDto.getMemberId()){
            throw new IllegalStateException("비정상적인 요청입니다. (결제회원과 로그인회원이 일치하지않음)");
        }
        MemberShip memberShip = laundryService.isPass(memberId);
        Pass pass = memberShip.getCheck();


        MemberDto member = memberMapper.selectOne(memberId); // 멤버아이디 바꾸기
        model.addAttribute("member", member);

        Map<String, Object> dryMap = ordersServiceKdh.findDryCleaningByOrdersId(ordersId);
        Map<String, Object> repairMap = ordersServiceKdh.findRepairByOrdersId(ordersId);
        boolean isQuickLaundry = ordersServiceKdh.isQuickLaundry(ordersId);
        Integer point = ordersServiceKdh.findPointByMemberId(memberId);


        //배송금액 로직 (생활빨래, 드라이클리닝, 수선만 포함)(배송비X 빠른세탁 X)
        Long totalPrice = ordersResponseDto.getCommonLaundryPrice() +
                (Long)dryMap.get("totalDryPrice") +
                (Long)repairMap.get("totalRepairPrice");

        DeliveryResponseDto delivery = setDeliveryPrice(totalPrice, isQuickLaundry);

        Long deliveryPrice = calcDeliveryPrice(isQuickLaundry, totalPrice);

        Long totalPriceWithDeliveryPrice = deliveryPrice + totalPrice;

        boolean isPass = false;
        Long totalPriceWithPassApplied = null;
        if(pass == Pass.PASS){
            isPass = true;
            totalPriceWithPassApplied = memberShip.apply(totalPrice);
            totalPriceWithDeliveryPrice = deliveryPrice + totalPriceWithPassApplied;
        }

        ordersServiceKdh.updateExpectedPriceByOrdersId(ordersId, totalPriceWithDeliveryPrice);

        model.addAttribute("totalPriceWithDeliveryPrice", totalPriceWithDeliveryPrice);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalPriceWithPassApplied", totalPriceWithPassApplied);


        log.info("dryMap={}", dryMap);
        log.info("repairMap={}", repairMap);
        model.addAttribute("isPass", isPass);
        model.addAttribute("order", ordersResponseDto);
        model.addAttribute("dryMap", dryMap);
        model.addAttribute("repairMap", repairMap);
        model.addAttribute("point", point);
        model.addAttribute("memberId", memberId);
        model.addAttribute("quickLaundry",
                new QuickLaundryResponseDto(Delivery.QUICK_DELIVERY, isQuickLaundry));
        model.addAttribute("delivery", delivery);

        return "project_order_view";
    }


    private static Long calcDeliveryPrice(boolean isQuickLaundry, Long totalPrice) {
        Long deliveryPrice = null;
        if(isQuickLaundry){
            deliveryPrice = Delivery.COMMON_DELIVERY.getPrice() + Delivery.QUICK_DELIVERY.getPrice();
        } else {
            deliveryPrice = (totalPrice >=30000) ? 0L : Delivery.COMMON_DELIVERY.getPrice();
        }
        return deliveryPrice;
    }

    private static DeliveryResponseDto setDeliveryPrice(Long totalPrice, boolean isQuickLaundry) {
        DeliveryResponseDto delivery = new DeliveryResponseDto(Delivery.COMMON_DELIVERY);

        //빠른배송이 아니라면
        if(!isQuickLaundry){
            //총액이 3만원이 넘으면 배송비 없음
            if(totalPrice >= 30000){
                delivery.setDeliveryStatus(false);
            } else {
                delivery.setDeliveryStatus(true);
            }
        } //빠른배송이라면 배송비 있음
        else {
            delivery.setDeliveryStatus(true);
        }

        return delivery;
    }

    @GetMapping("/{ordersId}/members/{memberId}/coupons")
    public String coupons(@PathVariable Long ordersId, @PathVariable Long memberId, Model model, String takeDate){

        Long expectedPrice = ordersServiceKdh.findExpectedPriceByOrdersId(ordersId);
        List<MyCoupon> getCoupon = laundryService.getCoupon(memberId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("coupon", getCoupon);
        model.addAttribute("expectedPrice", expectedPrice);

        return "project_use_coupon2";
    }







}

