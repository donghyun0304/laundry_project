package aug.laundry.service;

import aug.laundry.dao.orders.OrdersDao;
import aug.laundry.dao.payment.PaymentDao;
import aug.laundry.dao.point.PointDao;
import aug.laundry.dto.CouponCheckDto;
import aug.laundry.dto.PaymentCheckRequestDto;
import aug.laundry.exception.IsNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PointDao pointDao;
    private final PaymentDao paymentDao;

    public void isValid(Long memberId, Long ordersId, PaymentCheckRequestDto payment){

        Long pointPrice = payment.getPointPrice();

        if (pointPrice != null) {
            Integer point = pointDao.findByMemberId(memberId);

            if(point < payment.getPointPrice()){
                throw new IsNotValidException("주문에서 사용한 포인트가 회원이 가지고 있는 포인트보다 많습니다.");
            }
        }

        Long couponListId = payment.getCouponListId();

        if(couponListId != null){
            CouponCheckDto coupon = paymentDao.findCouponByCouponListId(couponListId);

            if(coupon == null){
                throw new IsNotValidException("존재하지 않는 쿠폰입니다.");
            }
            // 1:미사용 2:주문대기 3:완료처리. 정확한 숫자는 ERD확인요망
            if(coupon.getCouponListStatus() == 3){
                throw new IsNotValidException("이미 사용한 쿠폰입니다.");
            }
        }
        
        
        //금액 계산후 검증해야함







    }

}

