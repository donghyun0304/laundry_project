package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.dto.DateForm;
import aug.laundry.service.CommonLaundryService_sdj;
import aug.laundry.service.DrycleaningService_sdj;
import aug.laundry.service.RepairService_sdj;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/laundry")
public class LaundryOrderController {

    private final CommonLaundryService_sdj commonLaundryService;
    private final DrycleaningService_sdj drycleaningService;
    private final RepairService_sdj repairService;

    @GetMapping
    public String first(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Long memberId, Model model) {
        DateForm dateForm = new DateForm(); // 수거, 배송에 대한 정보



        model.addAttribute("dateTime", dateForm);
        return "project_order_1";
    }

    @GetMapping("/dry")
    public String drycleaning() {

        return "project_order_drycleaning";
    }

    @GetMapping("/repair")
    public String repair() {

        return "project_order_repair";
    }
}
