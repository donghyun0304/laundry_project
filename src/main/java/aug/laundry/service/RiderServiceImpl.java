package aug.laundry.service;

import aug.laundry.dao.rider.RiderMapper;
import aug.laundry.domain.DeliveryImage;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Rider;
import aug.laundry.dto.OrdersEnum;
import aug.laundry.enums.orderStatus.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService{

    private final RiderMapper riderMapper;

    @Override
    public List<Orders> OrderList(String status) {
        return riderMapper.orderList(status);
    }
    @Override
    public List<OrdersEnum> OrderListEnum(String status) {
        List<OrdersEnum> ordersEnums = riderMapper.orderListEnum(status);
        ordersEnums.forEach(x -> x.setOrdersStatus(OrderStatus.valueOf("R" + x.getOrdersStatus()).getTitle()));
        return ordersEnums;
    }

    @Override
    public List<Map<String, Integer>> orderListCnt() {
        return riderMapper.orderListCnt();
    }

    @Override
    public Orders orderInfo(Long ordersId) {
        return riderMapper.orderInfo(ordersId);
    }

    @Override
    public int updateOrderRider(Orders orders) {
        return riderMapper.updateOrderRider(orders);
    }

    @Override
    public int updateOrderStatus(Orders orders) {
        return riderMapper.updateOrderStatus(orders);
    }

    @Override
    public Rider riderInfo(String riderName) {
        return riderMapper.riderInfo(riderName);
    }

    @Override
    public DeliveryImage finishImg(Long ordersId) {
        return riderMapper.finishImg(ordersId);
    }
}
