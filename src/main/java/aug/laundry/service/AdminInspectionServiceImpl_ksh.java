package aug.laundry.service;

import aug.laundry.dao.admin.AdminInspectionDao;
import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.*;
import aug.laundry.enums.category.Category;
import aug.laundry.enums.fileUpload.FileUploadType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminInspectionServiceImpl_ksh implements AdminInspectionService_ksh{

    private final AdminInspectionDao adminInspectionDao;
    private final FileUploadService_ksh fileUpload;

    @Override
    public List<AdminInspectionDto> getInspectionList(Criteria cri, Long orderStatus) {
        return adminInspectionDao.getInspectionList(cri, orderStatus);
    }

    @Override
    public int getTotalCount(Long ordersStatus) {
        return adminInspectionDao.getTotalCount(ordersStatus);
    }

    @Override
    public Map<String, Object> getInspectionDetail(Long ordersId){

        Map<String, Object> detailInfo = new HashMap<>();

        Long orderDetailId = adminInspectionDao.getOrderInfo(ordersId).getOrdersDetailId();

        detailInfo.put("orderDetailId", orderDetailId);

        // 나중에 오류처리 추가해두기
        detailInfo.put("orderInfo", adminInspectionDao.getOrderInfo(ordersId));

        CommonLaundry commonLaundry = adminInspectionDao.getCommonLaundryInfo(orderDetailId);
        if(commonLaundry == null){
            detailInfo.put("commonLaundryInfo", null);
        } else {
            detailInfo.put("commonLaundryInfo", commonLaundry);
        }

        List<Drycleaning> drycleaningInfo = adminInspectionDao.getDrycleaningInfo(orderDetailId);
        if(drycleaningInfo.size() == 0){
            detailInfo.put("drycleaningInfo", null);
        } else {
            for (Drycleaning drycleaning : drycleaningInfo) {
                drycleaning.setDrycleaningCategory(Category.valueOf(drycleaning.getDrycleaningCategory()).getTitle());
            }
            detailInfo.put("drycleaningInfo", drycleaningInfo);
        }

        List<RepairInfoDto> repairInfo = adminInspectionDao.getRepairInfo(orderDetailId);
        if(repairInfo.size() == 0){
            detailInfo.put("repairInfo", null);
        } else {
            for (RepairInfoDto repairInfoDto : repairInfo) {
                repairInfoDto.setRepairCategory(Category.valueOf(repairInfoDto.getRepairCategory()).getTitle());
            }
            detailInfo.put("repairInfo", repairInfo);
        }

        return detailInfo;
    }

    @Override
    public AdminInspectionDto getOrderSearchInfo(Long ordersId, Long ordersStatus) {
        return adminInspectionDao.getOrderSearchInfo(ordersId, ordersStatus);
    }

    @Transactional
    @Override
    public int updateInspectionResult(AdminInspectionDto adminInfo, CommonLaundry commonLaundry, Long adminId,
                                      List<Drycleaning> drycleanings, List<Repair> repairs, List<MultipartFile> files) throws Exception {
        // 검수 저장

        int res = 0;

        if(commonLaundry.getCommonLaundryId() != null) {
            adminInspectionDao.updateCommon(commonLaundry);
        }
        if(repairs != null) {
            for (Repair repair : repairs) {
                adminInspectionDao.updateRepair(repair);
            }
        }
        if(drycleanings != null) {
            for (Drycleaning drycleaning : drycleanings) {
                adminInspectionDao.updateDrycleaning(drycleaning);
            }
        }
        adminInspectionDao.updateInspectionStatus(adminInfo.getOrdersId(), adminId);
        adminInspectionDao.updateOrderStatus(adminInfo.getOrdersId());

        fileUpload.saveFile(files, adminInfo.getInspectionId(), FileUploadType.INSPECTION);

        return res;
    }

}
