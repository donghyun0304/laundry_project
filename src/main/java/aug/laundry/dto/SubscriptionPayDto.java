package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubscriptionPayDto {
    private Long subscriptionPayId;
    private Long memberId; // 로그인 아이디
    private int selectMonth;// 구독 개월 수
    private String merchantUid;
    private String customerUid;
    private String name;
    private int amount;
    private String payDate;
    private char subscriptionStatus;
    private String impUid;

    public SubscriptionPayDto() {

    }
}
