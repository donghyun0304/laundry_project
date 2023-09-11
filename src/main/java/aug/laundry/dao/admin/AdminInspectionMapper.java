package aug.laundry.dao.admin;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Orders;
import aug.laundry.domain.Repair;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.RepairInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminInspectionMapper {

    List<AdminInspectionDto> getInspectionList(@Param("cri") Criteria cri, @Param("orderStatus") Long orderStatus);
    int getTotalCount(Long ordersStatus);
    // 주문정보조회
    AdminInspectionDto getOrderInfo(Long ordersId);
    // 생활빨래
    CommonLaundry getCommonLaundryInfo(Long orderDetailId);
    // 드라이클리닝
    List<Drycleaning> getDrycleaningInfo(Long orderDetailId);
    //수선
    List<RepairInfoDto> getRepairInfo(Long orderDetailId);
    AdminInspectionDto getOrderSearchInfo(@Param("ordersId") Long ordersId,@Param("ordersStatus") Long ordersStatus);
    int updateCommon(CommonLaundry commonLaundry);
    int updateRepair(Repair repair);
    int updateDrycleaning(Drycleaning drycleaning);
    int updateInspectionStatus(@Param("ordersId") Long ordersId, @Param("adminId")Long adminId);
    int updateOrderStatus(Long ordersId);
}
