package aug.laundry.dao.admin;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.RepairInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AdminInspectionDao {

    private final AdminInspectionMapper mapper;

    public List<AdminInspectionDto> getInspectionList(Criteria cri, Long orderStatus) {
        return mapper.getInspectionList(cri, orderStatus);
    }
    public int getTotalCount(Long ordersStatus){
        return mapper.getTotalCount(ordersStatus);
    }

    public AdminInspectionDto getOrderInfo(Long ordersId) {
        return mapper.getOrderInfo(ordersId);
    }

    public CommonLaundry getCommonLaundryInfo(Long orderDetailId) {
        return mapper.getCommonLaundryInfo(orderDetailId);
    }

    public List<Drycleaning> getDrycleaningInfo(Long orderDetailId) {
        return mapper.getDrycleaningInfo(orderDetailId);
    }

    public List<RepairInfoDto> getRepairInfo(Long orderDetailId) {
        return mapper.getRepairInfo(orderDetailId);
    }

    public int updateCommon(CommonLaundry commonLaundry){
        return mapper.updateCommon(commonLaundry);
    }
    public int updateRepair(Repair repair){
        return mapper.updateRepair(repair);
    }
    public int updateDrycleaning(Drycleaning drycleaning){
        return mapper.updateDrycleaning(drycleaning);
    }
    public int updateInspectionStatus(Long ordersId, Long adminId){
        return mapper.updateInspectionStatus(ordersId, adminId);
    }
    public int updateOrderStatus(Long ordersId){
        return mapper.updateOrderStatus(ordersId);
    }
    public AdminInspectionDto getOrderSearchInfo(Long ordersId, Long ordersStatus) {
        return mapper.getOrderSearchInfo(ordersId, ordersStatus);
    }
}
