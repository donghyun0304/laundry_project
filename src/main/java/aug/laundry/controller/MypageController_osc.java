package aug.laundry.controller;

import aug.laundry.dto.*;
import aug.laundry.service.LaundryService;
import aug.laundry.service.MemberService_kgw;
import aug.laundry.service.MypageService_osc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MypageController_osc {

  private final MypageService_osc mypageService;
  private final MemberService_kgw memberService;
  private final LaundryService laundryService;

  @GetMapping("/{memberId}/mypage")
  public String MypageMain(@PathVariable Long memberId, Model model){
    MypageDto mypageDto = mypageService.findByNameAndPass(memberId);

    LocalDate day = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
    String sysdate = day.format(formatter);
    String enddate = mypageDto.getSubscriptionExpireDate();

    String name = mypageDto.getMemberName();
    String pass = mypageDto.getSubscriptionExpireDate();

    if(pass==null){
      model.addAttribute("userName", name);
      model.addAttribute("userPass", null);
      return "project_mypage_list";
    }

    if(sysdate.compareTo(enddate) > 0){
      model.addAttribute("userName",name);
      model.addAttribute("userPass", null);
    } else if(sysdate.compareTo(enddate)<0){
      model.addAttribute("userName", name);
      pass="PASS";
      model.addAttribute("userPass", pass);
    } else {
      model.addAttribute("userName", name);
      pass="PASS";
      model.addAttribute("userPass", pass);
    }

    return "project_mypage_list";
  }

  @GetMapping("{memberId}/coupons")
  public String MypageCouponList(@PathVariable Long memberId, Model model){
    List<MyCoupon> getCoupon = laundryService.getCoupon(memberId);
    int someCoupon = mypageService.someCoupon(memberId);

      model.addAttribute("memberId", memberId);
      model.addAttribute("coupon", getCoupon);
      model.addAttribute("someCoupon", someCoupon);
    return "project_coupon";
  }

  @GetMapping("{memberId}/points")
  public String MypagePointsList(@PathVariable Long memberId, Model model){
    List<MyPointDto> getPoint = mypageService.getPoint(memberId);

    if(!getPoint.isEmpty()){
      PointNowDto getpointNow = mypageService.getPointNow(memberId);
      Long pointNow = getpointNow.getPointNow();

      model.addAttribute("memberId", memberId);
      model.addAttribute("point", getPoint);
      model.addAttribute("pointNow", pointNow);
    } else {
      model.addAttribute("memberId", memberId);
      model.addAttribute("point", getPoint);
    }


    return "project_point";
  }

  @GetMapping("{memberId}/invite")
  public String MypageInvite(@PathVariable Long memberId, Model model) {

    String inviteCode = mypageService.findByInviteCode(memberId);

    model.addAttribute("inviteCode", inviteCode);

    return "project_invite";
  }

  @GetMapping("{memberId}/update")
  public String MypageUpdate(@PathVariable Long memberId, Model model){

    MypageDto mypageDto = mypageService.findByInfo(memberId);
    String name = mypageDto.getMemberName();
    String address = "("+mypageDto.getMemberZipcode()+") " + mypageDto.getMemberAddress() + " " + mypageDto.getMemberAddressDetails();
    String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
    String phone = mypageDto.getMemberPhone().replaceAll(regEx, "$1-$2-$3");

    model.addAttribute("userName", name);
    model.addAttribute("userAddress", address);
    model.addAttribute("userPhone", phone);

    return "project_mypage";
  }

  @GetMapping("{memberId}/address/update")
  public String addressUpdate(@PathVariable Long memberId){
    return "project_update_address";
  }

//  @PostMapping("{memberId}/address/update")
//  public String addressUpdate(@PathVariable Long memberId, HttpServletRequest request){
//
//    String memberZipcode = request.getParameter("zipcode");
//    String memberAddress = request.getParameter("searchAddressValue");
//    String memberAddressDetails = request.getParameter("detailAddress");
//
//    if(memberZipcode!=null && memberAddress!=null && memberAddressDetails!=null){
//      int res = mypageService.updateAddress(memberId, memberZipcode, memberAddress, memberAddressDetails);
//      return "redirect:/members/{memberId}/update";
//    } else {
//      return "redirect:/members/{memberId}/update";
//    }
//  }

  @PostMapping("{memberId}/address/update")
  public String addressUpdate(@PathVariable Long memberId, @Valid @ModelAttribute("addressUpdate") UpdateAddressDto addressDto, BindingResult bindingResult){

    if(bindingResult.hasErrors()){

      return "project_update_address";
    }

    mypageService.updateAddress(memberId, addressDto);
    return "redirect:/members/{memberId}/update";
  }

  @GetMapping("{memberId}/phone/update")
  public String phoneUpdate(@PathVariable Long memberId){
    return "project_update_phone";
  }

//  @PostMapping("{memberId}/phone/update")
//  public String phoneUpdate(@PathVariable Long memberId, HttpServletRequest request){
//
//    String requestPhone = request.getParameter("phone");
//    String memberPhone = requestPhone.replace("-","");
//
//    if(memberPhone!=null){
//      int res = mypageService.updatePhone(memberId, memberPhone);
//      return "redirect:/members/{memberId}/update";
//    } else {
//      return "redirect:/members/{memberId}/update";
//    }
//  }

  @PostMapping("{memberId}/phone/update")
  public String phoneUpdate(@PathVariable Long memberId, @Valid @ModelAttribute("phone") UpdatePhoneDto updatePhoneDto, BindingResult bindingResult){

    if(bindingResult.hasErrors()){
      return "project_update_phone";
    }

    updatePhoneDto.setMemberPhone(updatePhoneDto.getMemberPhone().replace("-",""));
    mypageService.updatePhone(memberId, updatePhoneDto);
    return "redirect:/members/{memberId}/update";
  }

  @GetMapping("{memberId}/unregister")
  public String unregister(@PathVariable Long memberId){

    MemberDto memberDto = memberService.selectOne(memberId);
    if(memberDto!=null){
      int res = mypageService.unregister(memberDto.getMemberId());
      return "redirect:/";
    } else {
      return "redirect:/";
    }
  }

  @GetMapping("{memberId}/password/update")
  public  String passwordUpdate(@PathVariable Long memberId){
    return "project_change_password";
  }

//  @PostMapping("{memberId}/password/update")
//  public String passwordUpdate(@PathVariable Long memberId, HttpServletRequest request){
//    String memberPassword = request.getParameter("userPw");
//    if(memberPassword!=null){
//      mypageService.updatePassword(memberId, memberPassword);
//      return "redirect:/members/{memberId}/update";
//    } else {
//      return "redirect:/members/{memberId}/password/update";
//    }
//  }

  @PostMapping("{memberId}/password/update")
  public String changePassword(@PathVariable Long memberId, @Valid @ModelAttribute("userPw") ChangePasswordDto changePasswordDto, BindingResult bindingResult){

    if(bindingResult.hasErrors()){
      return  "project_change_password";
    }


    mypageService.changePassword(memberId, changePasswordDto);
    return "redirect:/members/{memberId}/update";
  }

}
