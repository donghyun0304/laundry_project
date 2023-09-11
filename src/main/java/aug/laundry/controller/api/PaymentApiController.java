package aug.laundry.controller.api;

import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentApiController {

    @Value("${import.rest-api}")
    private String restApi;

    @Value("${import.rest-api-secret}")
    private String restApiSecret;

    private final IamportClient iamportClient;

    @Autowired
    public PaymentApiController() {
        this.iamportClient = new IamportClient(restApi, restApiSecret);
    }

        @PostMapping("/orders/2/payment/validation")
        public void verifyIamPort(@RequestBody Map<String, String> map){ //void 나중에 수정필요하면 수정

            log.info("validation=[}", map);


    }


}
