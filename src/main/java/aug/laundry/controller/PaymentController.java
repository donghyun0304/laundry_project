package aug.laundry.controller;

import aug.laundry.commom.SessionConstant;
import aug.laundry.domain.Paymentinfo;
import aug.laundry.dto.PaymentCheckRequestDto;
import aug.laundry.service.OrdersService_kdh;
import aug.laundry.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import retrofit2.http.Path;

import java.io.IOException;

@Controller
public class PaymentController {


    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    private IamportClient iamportClient;
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping("/orders/{ordersId}/payment/complete")
    public String complete(@PathVariable Long ordersId, @ModelAttribute PaymentCheckRequestDto payment,
                           @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false)Long memberId) throws IamportResponseException, IOException {

        iamportClient = new IamportClient(restApi, restApiSecret);
        IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(payment.getImp_uid());
        Payment response = irsp.getResponse();

        Paymentinfo paymentinfo = new Paymentinfo(
                1L, response.getImpUid(), response.getPayMethod(), response.getMerchantUid(),
                response.getBuyerName(), response.getBuyerTel(), response.getAmount().longValue()
        );//memberId 세션에서 가져온 값으로 바꿔야함

        try {
            paymentService.savePaymentInfo(paymentinfo);
        } catch(IllegalStateException e){

            iamportClient.cancelPaymentByImpUid(
                    new CancelData(
                            irsp.getResponse().getImpUid(), true, irsp.getResponse().getAmount()));

            return ""; // 결제정보가 db에 저장되지않아서 취소후 결제시스템 오류있다고 안내페이지 보여줘야함
        }

        paymentService.isValid(irsp, paymentinfo.getPaymentinfoId(), memberId, ordersId, payment);

        //redirect로 바꿔야함
        return "project_search_id_result";
    }
}