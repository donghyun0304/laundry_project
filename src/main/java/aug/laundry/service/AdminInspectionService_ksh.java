package aug.laundry.service;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.DrycleaningListDto;
import aug.laundry.dto.RepairListDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface AdminInspectionService_ksh {
    List<AdminInspectionDto> getInspectionList(Criteria cri, Long orderStatus);

    int getTotalCount(Long ordersStatus);

    Map<String, Object> getInspectionDetail(Long ordersId);

    AdminInspectionDto getOrderSearchInfo(Long ordersId, Long ordersStatus);

    int updateInspectionResult(AdminInspectionDto adminInfo, CommonLaundry commonLaundry, Long adminId,
                               List<Drycleaning> drycleanings, List<Repair> repairs, List<MultipartFile> files) throws Exception;
}
