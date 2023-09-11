package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
public class PaymentCheckRequestDto {


    private Long couponListId;
    private Long couponPrice;
    private Long pointPrice;
    private String imp_uid;
    private String merchant_uid;
    private boolean imp_success;


}
