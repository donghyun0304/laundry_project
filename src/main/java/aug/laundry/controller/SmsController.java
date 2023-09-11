package aug.laundry.controller;

import aug.laundry.service.SendSmsService_kgw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SmsController {
    private final SendSmsService_kgw sendSms;

    @ResponseBody
    @GetMapping("/sendSms")
    public Map<String, String> sendSms(String phonenumber){
        Map<String,String> map = new HashMap<String, String>();
        try {
            String number = sendSms.sendMassage(phonenumber);
            map.put("number", number);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("msg", "문자 전송 중 에러가 발생하였습니다.");
        }


        return map;


    }
}
