package aug.laundry.dao;

import aug.laundry.controller.LaundryController;
import aug.laundry.domain.CouponList;
import aug.laundry.domain.Orders;
import aug.laundry.dto.Address;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import aug.laundry.enums.category.Category;
import aug.laundry.service.LaundryServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LaundryMapper {

    OrderInfo firstInfo(Long memberId);

    Integer isQuick(Long memberId);
    Integer isDry(Long memberId);
    Integer isCommon(Long memberId);
    Integer isRepair(Long memberId);

    List<MyCoupon> getCoupon(Long memberId);

    Address getAddress(Long memberId);

    List<String> getDry(Long memberId);

    List<String> getRepair(Long memberId);

    Integer isPass(Long memberId);

    CouponList validCoupon(@Param("memberId") Long memberId, @Param("couponListId") Long couponListId);

    Integer useCoupon(@Param("memberId") Long memberId, @Param("couponListId") Long couponListId);

    Integer insert(Orders orders);

    Long getCouponDiscount(@Param("memberId") Long memberId, @Param("couponListId") Long couponListId);
}
