package aug.laundry.service;

import aug.laundry.dao.LaundryRepository;
import aug.laundry.domain.Orders;
import aug.laundry.dto.Address;
import aug.laundry.dto.MyCoupon;
import aug.laundry.dto.OrderInfo;
import aug.laundry.dto.OrderPost;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.category.Delivery;
import aug.laundry.enums.category.MemberShip;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaundryServiceImpl implements LaundryService{

    private final LaundryRepository laundryRepository;

    @Override
    public OrderInfo firstInfo(Long memberId) {
        return laundryRepository.firstInfo(memberId);
    }

    @Override
    public List<MyCoupon> getCoupon(Long memberId) {
        return laundryRepository.getCoupon(memberId);
    }

    @Override
    public Address getAddress(Long memberId) {
        return laundryRepository.getAddress(memberId);
    }

    @Override
    public List<Category> getDry(Long memberId) {
        return laundryRepository.getDry(memberId);
    }

    @Override
    public List<Category> getRepair(Long memberId) {
        return laundryRepository.getRepair(memberId);
    }

    @Override
    public MemberShip isPass(Long memberId) {
        return new MemberShip(laundryRepository.isPass(memberId));
    }

    @Transactional
    @Override
    public void update(Long memberId, Long couponListId, OrderPost orderPost) {
        boolean validCoupon = laundryRepository.validCoupon(memberId, couponListId); // 쿠폰 유효성 검사
        Long expectedPrice = 0L;
        if (validCoupon) {
            expectedPrice -= laundryRepository.getCouponDiscount(memberId, couponListId);
            laundryRepository.useCoupon(memberId, couponListId); // 쿠폰 업데이트
        }
        System.out.println("orderPost = " + orderPost);
        Orders orders = getOrders(memberId, orderPost);


        MemberShip memberShip = new MemberShip(laundryRepository.isPass(memberId));
        OrderInfo orderInfo = laundryRepository.firstInfo(memberId); // 빠른세탁, 드라이클리닝, 생활빨래, 수선

        // 빠른배송 or 일반배송
        expectedPrice += orderInfo.getIsQuick() != null && orderInfo.getIsQuick() != 0 ? Delivery.QUICK_DELIVERY.getPrice() : Delivery.COMMON_DELIVERY.getPrice();
        if (orderInfo.getIsCommon() != 0) expectedPrice += memberShip.apply(Category.BASIC.getPrice());
        if (orderInfo.getIsDry() != 0) expectedPrice += memberShip.apply(laundryRepository.getDry(memberId).stream().map(x -> x.getPrice()).reduce((a, b) -> a + b).get());
        if (orderInfo.getIsRepair() != 0) expectedPrice += memberShip.apply(laundryRepository.getRepair(memberId).stream().map(x -> x.getPrice()).reduce((a,b) -> a + b).get());


        orders.setOrdersExpectedPrice(Math.round(expectedPrice / 100) * 100);
        orders.setOrdersStatus(2);
        System.out.println("최종 orders = " + orders);
        Integer result = laundryRepository.insert(orders);
        System.out.println("성공!");
    }

    @NotNull
    private Orders getOrders(Long memberId, OrderPost orderPost) {
        Orders orders = new Orders();
        orders.setMemberId(memberId);
        orders.setOrdersAddress(orderPost.getAddress());
        orders.setOrdersAddressDetails(orderPost.getAddressDetails());
        orders.setOrdersPickup(orderPost.getLocation());
        orders.setOrdersPickupDate(getDateString(orderPost.getTakeDate()));
        orders.setOrdersReturnDate(getDateString(orderPost.getDeliveryDate()));
        orders.setOrdersInfo(orderPost.getPassword() == null || orderPost.getPassword().equals("") ? null : orderPost.getPassword());
        orders.setOrdersRequest(orderPost.getRequest());
        return orders;
    }

    public String getDateString(LocalDateTime dateTime) {
        String date = dateTime.toString();
        return date.substring(0, date.indexOf(":") + 3).replaceAll("T", " ");
    }


}
