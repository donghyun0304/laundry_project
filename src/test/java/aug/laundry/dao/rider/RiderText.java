package aug.laundry.dao.rider;

import aug.laundry.enums.orderStatus.OrderStatus;
import org.junit.jupiter.api.Test;

public class RiderText {

    @Test
    void test() {
        String title = OrderStatus.valueOf("R" + 1).getTitle();
        System.out.println("title = " + title);
    }
}
