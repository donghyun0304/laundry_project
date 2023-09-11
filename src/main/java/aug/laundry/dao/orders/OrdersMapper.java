package aug.laundry.dao.orders;

import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.OrdersResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
