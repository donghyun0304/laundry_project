package aug.laundry.dao.orders;

import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.OrdersListResponseDto;
import aug.laundry.dto.OrdersResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrdersDao {

    private final OrdersMapper ordersMapper;

    public OrdersResponseDto findByOrdersId(Long ordersId){
        OrdersResponseDto findOrder = ordersMapper.findByOrdersId(ordersId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        log.info("orderResponseDto={}", findOrder);
        return findOrder;
    }

    public List<Drycleaning> findDryCleaningByOrdersId(Long ordersId){
        List<Drycleaning> drycleaningList = ordersMapper.findDryCleaningByOrdersId(ordersId);
        log.info("drycleaningList={}", drycleaningList);

        if(drycleaningList == null || drycleaningList.isEmpty()){
            return Collections.EMPTY_LIST;
        } else {
            return drycleaningList;
        }
    }

    public List<Repair> findRepairByOrdersId(Long ordersId){
        List<Repair> repairList = ordersMapper.findRepairByOrdersId(ordersId);
        log.info("repairList={}", repairList);

        if(repairList == null || repairList.isEmpty()){
            return Collections.EMPTY_LIST;
        } else {
            return repairList;
        }
    }

    public boolean isQuickLaundry(Long ordersId){
        if(ordersMapper.findQuickLaundryByOrdersId(ordersId)==1){
            return true;
        } else{
            return false;
        }
    }

    public int updateExpectedPriceByOrdersId(Long ordersId, Long expectedPrice){
        int i = ordersMapper.updateExpectedPriceByOrdersId(ordersId, expectedPrice);
        log.info("updateExpectedPriceByOrdersId={}", i);
        return i;
    }

    public Optional<Long> findExpectedPriceByOrdersId(Long ordersId){
        return ordersMapper.findExpectedPriceByOrdersId(ordersId);
    }

    public int updateOrdersStatusToCompletePayment(Long ordersId){
        return ordersMapper.updateOrdersStatusToCompletePayment(ordersId);
    }

    public int updateCouponListStatusToUsedCoupon(Long couponListId){
        return ordersMapper.updateCouponListStatusToUsedCoupon(couponListId);
    }

    public int addPoint(Long memberId, Long pointStack, String pointStackReason){
        return ordersMapper.addPoint(memberId, pointStack, pointStackReason);
    }

    public List<OrdersListResponseDto> findOrdersByMemberIdAndCri(Criteria cri, Long memberId){
        return ordersMapper.findOrdersByMemberIdAndCri(cri, memberId);
    }

    public int getTotalCount(Long memberId) {
        return ordersMapper.getTotalCount(memberId);
    }



}
