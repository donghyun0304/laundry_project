package aug.laundry.service;

import aug.laundry.dao.orders.OrdersDao;
import aug.laundry.dao.payment.PaymentDao;
import aug.laundry.dao.point.PointDao;
import aug.laundry.domain.Paymentinfo;
import aug.laundry.dto.CouponCheckDto;
import aug.laundry.dto.PaymentCheckRequestDto;
import aug.laundry.exception.IsNotValidException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PointDao pointDao;
    private final PaymentDao paymentDao;
    private final OrdersDao ordersDao;

    public void isValid(IamportResponse<Payment> irsp, Long paymentinfoId, Long memberId, Long ordersId, PaymentCheckRequestDto payment) {

        Long pointPrice = payment.getPointPrice();

        if (pointPrice != null) {
            Integer point = pointDao.findByMemberId(1L);  //memberId 교체해야함

            if (point < payment.getPointPrice()) {
                throw new IsNotValidException(
                        "주문에서 사용한 포인트가 회원이 가지고 있는 포인트보다 많습니다.[" + paymentinfoId + "]");
            }
        }

        Long couponListId = payment.getCouponListId();
        Long couponPrice = payment.getCouponPrice();

        if (couponListId != null) {
            CouponCheckDto coupon = paymentDao.findCouponByCouponListId(couponListId);

            if (coupon == null) {
                throw new IsNotValidException("존재하지 않는 쿠폰입니다.[" + paymentinfoId + "]");
            }
            if (couponPrice != coupon.getCouponPrice()) {
                throw new IsNotValidException("쿠폰 금액이 일치하지 않습니다.[" + paymentinfoId + "]");
            }
            // 1:미사용 2:주문대기 3:완료처리. 정확한 숫자는 ERD확인요망
            if (coupon.getCouponListStatus() == 3) {
                throw new IsNotValidException("이미 사용한 쿠폰입니다.[" + paymentinfoId + "]");
            }
        }


        //금액 계산후 검증해야함

        Long finalValidPrice = ordersDao.findExpectedPriceByOrdersId(ordersId)
                .orElseThrow(() -> new IsNotValidException("예상금액이 존재하지 않습니다.[" + paymentinfoId + "]"));

        if (pointPrice == null) {
            pointPrice = 0L;
        }

        if (couponPrice == null) {
            couponPrice = 0L;
        }

        // 포인트, 쿠폰까지 적용된 최종가격
        Long expectedTotalPrice = finalValidPrice - pointPrice - couponPrice;

        Long amount = irsp.getResponse().getAmount().longValue();

        if (expectedTotalPrice != amount) {
            throw new IsNotValidException("최종가격과 결제금액이 일치하지 않음[" + paymentinfoId + "]");
        }

    }

    @Transactional
    public void savePaymentInfo(Paymentinfo paymentinfo) {
        paymentDao.savePaymentInfo(paymentinfo);
        if(paymentinfo.getPaymentinfoId() == null){
            throw new IllegalStateException("결제정보가 저장되지 않았습니다.");
        }
    }

    public Paymentinfo findPaymentinfoByPaymentinfoId(Long paymentinfoId){
        Paymentinfo paymentinfo = paymentDao.findPaymentinfoByPaymentinfoId(paymentinfoId);

        if(paymentinfo != null){
            //1:결제완료 2:환불. 숫자의미가 바뀌었을수있으므로 erd확인
            if(paymentinfo.getPaymentStatus() == 2){
                throw new IllegalStateException("이미 환불된 결제정보입니다.");
            }
        }
        if(paymentinfo == null){
            throw new IllegalArgumentException("결제정보가 존재하지 않습니다.");
        }

        return paymentinfo;
    }

}

