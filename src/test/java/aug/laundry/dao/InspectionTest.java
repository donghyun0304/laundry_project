package aug.laundry.dao;

import aug.laundry.dao.admin.AdminInspectionMapper;
import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.RepairInfoDto;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InspectionTest {

    @Autowired
    private AdminInspectionMapper inspection;

    @Test
    public void getList() {

//        List<AdminInspectionDto> list = inspection.getInspectionList(1,10);
//
//        Assertions.assertThat(1).isEqualTo(list.size());
    }

    @Test
    public void detailTest() {
        Long ordersId = 8L;
        AdminInspectionDto orderInfo = inspection.getOrderInfo(ordersId);

        Long orderDetailId = 4L;

        CommonLaundry commonLaundry = inspection.getCommonLaundryInfo(orderDetailId);
        List<Drycleaning> drycleanings = inspection.getDrycleaningInfo(orderDetailId);
        List<RepairInfoDto> repairInfoDtos = inspection.getRepairInfo(orderDetailId);

        Assertions.assertThat(3L).isEqualTo(orderDetailId);
        Assertions.assertThat(4L).isEqualTo(commonLaundry.getCommonLaundryId());
    }

    @Test
    public void updateTest() {
        Long ordersId = 8L;
        Long adminId = 9L;

        CommonLaundry commonLaundry = new CommonLaundry();
        commonLaundry.setCommonLaundryId(4L);
        commonLaundry.setCommonLaundryWeight(3.0);

        int res1 = inspection.updateCommon(commonLaundry);
        Assertions.assertThat(res1).isEqualTo(1);

        Repair re = new Repair();
        re.setRepairId(4L);
        re.setRepairPossibility('Y');
        re.setRepairNotReason("");

        Repair re1 = new Repair();
        re1.setRepairId(5L);
        re1.setRepairPossibility('N');
        re1.setRepairNotReason("불가능이유");

        List<Repair> repairs = new ArrayList<>();
        repairs.add(re);
        repairs.add(re1);

        int res2=0;
        for(int i=0; i<repairs.size(); i++) {
            res2 += inspection.updateRepair(repairs.get(i));
        }

        Assertions.assertThat(res2).isEqualTo(2);

        Drycleaning dry1 = new Drycleaning();
        dry1.setDrycleaningId(3L);
        dry1.setDrycleaningPossibility('N');
        dry1.setDrycleaningNotReason("test");

        Drycleaning dry2 = new Drycleaning();
        dry2.setDrycleaningId(4L);
        dry2.setDrycleaningPossibility('Y');
        dry2.setDrycleaningNotReason("");

        List<Drycleaning> drycleanings = new ArrayList<>();
        drycleanings.add(dry1);
        drycleanings.add(dry2);

        int res3=0;
        for (Drycleaning drycleaning : drycleanings) {
            res3 += inspection.updateDrycleaning(drycleaning);
        }
        Assertions.assertThat(res3).isEqualTo(2);

        int res4 = inspection.updateInspectionStatus(ordersId, adminId);
        Assertions.assertThat(res4).isEqualTo(1);

        int res5 = inspection.updateOrderStatus(ordersId);
        Assertions.assertThat(res5).isEqualTo(1);
    }
}
