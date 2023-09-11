package aug.laundry.service;

import aug.laundry.dto.Address;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import aug.laundry.dto.OrderPost;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.MemberShip;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface LaundryService {

    OrderInfo firstInfo(Long memberId);

    List<MyCoupon> getCoupon(Long memberId);

    Address getAddress(Long memberId);

    List<Category> getDry(Long memberId);

    List<Category> getRepair(Long memberId);

    MemberShip isPass(Long memberId);

    void update(Long memberId, Long couponListId, OrderPost orderPost);

    void check(Long memberId, HttpSession session);

    boolean insertDrycleaning(Long memberId, Long ordersDetailId, Map<String, Integer> result);

}
