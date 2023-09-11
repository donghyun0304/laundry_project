package aug.laundry.controller;

import aug.laundry.dto.SubscriptionPayDto;
import aug.laundry.service.SubscribeService_ksh;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService_ksh subscribeService_ksh;

    @GetMapping("/subscription")
    public String subscribeInfo() {
        return "project_subscribe";
    }

    @GetMapping("/subscription/order")
    public String subscribeOrder() {
        //    주문결제Api /subscription/order
        return "project_subscribe_choice";
    }

//    @GetMapping("/subscription/{subscriptionOrderId}/confirm")
//    public String subscribeOrderConfirm(@PathVariable("subscriptionOrderId") Long subscriptionOrderId) {
//        return "project_subscribe_success";
//    }

    @GetMapping("/subscription/confirm")
    public String subscribeOrderConfirm() {
        return "project_subscribe_success";
    }

    @PostMapping("/payments")
    public @ResponseBody Map<String, String> paymentsComplete(@RequestBody SubscriptionPayDto subData) {
        // 빌링키 발급 성공 & 결제 -> 결제 후 검증 하고 디비 저장
        //http://payments/complete?imp_uid=imp_635657837144&merchant_uid=57008833-33005&imp_success=true

        try {
            // access_token 요청해서 받아오기
//            String token = subscribeService_ksh.getAccessToken();
//            log.info("subData.toString={}", subData.toString());

//            결제결과 DB 처리는 웹훅(Webhook)을 연동하여 수신되는 데이터를 기준으로 처리하셔야 결제결과 누락없이 안정적인 결과처리를 완료하실 수 있습니다.
//            int res = subscribeService_ksh.saveSubscribe(subData);
//            log.info("res={}", res);
        } catch (Exception e) {
            log.info("Unable to get access token");
        }

        Map<String, String> data = new HashMap<>();
        data.put("success", "success");

        return data;
    }

    @PostMapping("/repayments")
    public @ResponseBody Map<String, String> repaymentsReserve(@RequestBody String webhookData) {
        // 웹훅 재결제 예약
        log.info("재결제예약TEST");

        //webhookData={"imp_uid":"imp_1234567890","merchant_uid":"merchant_1234567890","status":"paid"}

//        try {
//            // access_token 요청해서 받아오기
//
//            String payResultUrl = "https://api.iamport.kr/users/getToken";
//            String jsonResult = "{\"imp_key\": \"1332441508786676\", \"imp_secret\": \"paZ8k1DpZYmBi6v9oTUTpPbyMsxDyKt025wu7qGVKH914FWliCYP3aj4Bz0mLEPNlItgwKtkzSLSBdlf\"}";
//            StringEntity result = new StringEntity(jsonBody, "UTF-8");
//            json.setContentType("application/json");
//
//            /*
//            *  // imp_uid 또는 merchant_uid로 아임포트 서버에서 결제 정보 조회
//                    const paymentResult = await axios({
//                        url: 'https://api.iamport.kr/payments/' + imp_uid,
//                        method: "get",
//                        headers: { "Authorization": access_token }, // 인증 토큰을 Authorization header에 추가
//                        data: {
//                            imp_uid: imp_uid
//                        }
//                    });
//
//                    console.log("repayments result : ", paymentResult)
//
//            * */
//
//
//            httpClient.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        Map<String, String> data = new HashMap<>();
        data.put("success", "success");

        return data;
    }
}
