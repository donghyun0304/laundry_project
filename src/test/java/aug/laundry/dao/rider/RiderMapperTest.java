package aug.laundry.dao.rider;

import aug.laundry.domain.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class RiderMapperTest {

    @Autowired
    RiderMapper riderMapper;

    @Test
    void orderList(){git 
        List<Orders> orders = riderMapper.orderList();
        System.out.println("orders = " + orders);
        Assertions.assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void orderListCnt(){
        int res = riderMapper.orderListCnt();

        Assertions.assertThat(res).isEqualTo(1);
    }

}