package aug.laundry.dao.orders;

import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrdersMapper {

    Optional<OrdersResponseDto> findByOrdersId(Long ordersId);

    List<Drycleaning> findDryCleaningByOrdersId(Long ordersId);

    List<Repair> findRepairByOrdersId(Long ordersId);

    int findQuickLaundryByOrdersId(Long ordersId);

    int updateExpectedPriceByOrdersId(@Param("ordersId") Long ordersId, @Param("expectedPrice") Long expectedPrice);

    Optional<Long> findExpectedPriceByOrdersId(Long ordersId);

    int updateOrdersStatusToCompletePayment(Long ordersId);

    int updateCouponListStatusToUsedCoupon(@Param("couponListId") Long couponListId, @Param("ordersId") Long ordersId);

    int addPoint(@Param("pointDto") AddPointResponseDto pointDto);

    List<OrdersListResponseDto> findOrdersByMemberIdAndCri(@Param("cri") Criteria cri, @Param("memberId") Long memberId);

    List<OrdersListResponseDto> findOrdersFinishedByMemberIdAndCri(@Param("cri") Criteria cri, @Param("memberId") Long memberId);

    int getTotalCount(Long memberId);

    int updatePaymentinfoIdByOrdersId(@Param("paymentinfoId") Long paymentinfoId, @Param("ordersId") Long ordersId);

    int updatePriceNStatusNPaymentinfo(
            @Param("ordersFinalPrice") Long ordersFinalPrice, @Param("paymentinfoId") Long paymentinfoId, @Param("ordersId") Long ordersId);

    Optional<PriceResponseDto> findPricesByOrdersId(Long ordersId);

    int updatePointIdByOrdersId(@Param("pointId") Long pointId, @Param("ordersId") Long ordersId);
}
