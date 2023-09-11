package aug.laundry.dao.payment;

import aug.laundry.dao.orders.OrdersMapper;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Paymentinfo;
import aug.laundry.domain.Repair;
import aug.laundry.dto.CouponCheckDto;
import aug.laundry.dto.OrdersResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PaymentDao {

    private final PaymentMapper paymentMapper;

    public CouponCheckDto findCouponByCouponListId(Long couponListId) {
        CouponCheckDto coupon = paymentMapper.findCouponByCouponListId(couponListId);
        log.info("coupon={}", coupon);
        return coupon;
    }

    public void savePaymentInfo(Paymentinfo paymentinfo) {
        paymentMapper.savePaymentInfo(paymentinfo);
    }

    public Paymentinfo findPaymentinfoByPaymentinfoId(Long paymentinfoId){
        Paymentinfo paymentinfo = paymentMapper.findPaymentinfoByPaymentinfoId(paymentinfoId);
        log.info("paymentinfo={}", paymentinfo);
        return paymentinfo;
    }
}
