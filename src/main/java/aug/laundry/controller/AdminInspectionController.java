package aug.laundry.controller;

import aug.laundry.domain.CommonLaundry;
import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.AdminInspectionDto;
import aug.laundry.dto.Criteria;
import aug.laundry.dto.DrycleaningListDto;
import aug.laundry.dto.RepairListDto;
import aug.laundry.enums.fileUpload.FileUploadType;
import aug.laundry.service.AdminInspectionService_ksh;
import aug.laundry.service.FileUploadService_ksh;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminInspectionController {

    private final AdminInspectionService_ksh adminInspectionService_ksh;

    @GetMapping("/admin")
    public String getInspectionView() {
        return "project_manager_order_list";
    }
    @GetMapping("/admin/complete")
    public String getInspectedList() {
        return "project_manager_order_list_complete";
    }
    @GetMapping("/getList/{pageNo}/{orderStatus}")
    public @ResponseBody Map<String, Object> getInspectionList(Model model, @PathVariable("pageNo") int pageNo
                                                ,@PathVariable("orderStatus") Long orderStatus) {
        Map<String, Object> inspectionMap = new HashMap<>();

        Criteria cri = new Criteria();
        cri.setPageNo(pageNo, adminInspectionService_ksh.getTotalCount(orderStatus));

        List<AdminInspectionDto> list = adminInspectionService_ksh.getInspectionList(cri, orderStatus);

        if(list != null) {
            inspectionMap.put("res", "success");
            inspectionMap.put("list", list);
            inspectionMap.put("realEnd", cri.getRealEnd());
        }else {
            inspectionMap.put("res", "fail");
        }
        return inspectionMap;
    }

    @GetMapping("/searchOrder/{ordersId}/{status}")
    public @ResponseBody Map<String, Object> getSaerchOrder(@PathVariable("ordersId") Long ordersId,
                                                            @PathVariable("status") Long status) {
        Map<String, Object> searchOrder = new HashMap<>();

        AdminInspectionDto orderInfo = adminInspectionService_ksh.getOrderSearchInfo(ordersId, status);
        if(orderInfo != null) {
            searchOrder.put("res", "success");
            searchOrder.put("orderInfo", orderInfo);
        }else {
            searchOrder.put("res", "fail");
        }
        return searchOrder;
    }

    @GetMapping("/admin/{adminId}/{ordersId}")
    public String viewInspectionDetail(@PathVariable("adminId") Long adminId,
                                      @PathVariable("ordersId") Long ordersId, Model model) {

        model.addAttribute("info", adminInspectionService_ksh.getInspectionDetail(ordersId));

        return "project_manager_order_detail";
    }

    @PostMapping("/admin/{adminId}/{ordersId}")
    public String writeInsepctionResult(AdminInspectionDto adminInfo, CommonLaundry commonLaundry,
                                        @PathVariable("adminId") Long adminId, @PathVariable("ordersId") Long ordersId,
                                        @ModelAttribute(value = "DrycleaningListDto") DrycleaningListDto drycleanings,
                                        @ModelAttribute(value = "RepairListDto") RepairListDto repairs,
                                        List<MultipartFile> files) {
        try{
            adminInspectionService_ksh.updateInspectionResult(adminInfo, commonLaundry, adminId,
                    drycleanings.getDrycleaningList(), repairs.getRepairList(), files);
        }catch(Exception e){
            // 예외처리
            log.info("exception={}", e);
        }
        // 다시 등록되지않게 처리하기
        return "redirect:/admin";
    }

    @GetMapping("/admin/complete/{ordersId}")
    public String viewInspectionCompleteDetail(@PathVariable("ordersId") Long ordersId, Model model) {

        model.addAttribute("info", adminInspectionService_ksh.getInspectionDetail(ordersId));

        return "project_manager_order_complete_detail";
    }
}
