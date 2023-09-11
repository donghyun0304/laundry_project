package aug.laundry.enums.orderStatus;

import lombok.Getter;

@Getter
public enum OrderStatus {

    R1("주문취소"),
    R2("주문완료"),
    R3("수거전"),
    R4("수거완료"),
    R5("업체배송완료"), //검수전
    R6("검수완료"),
    R7("결제대기"),
    R8("결제완료"),
    R9("세탁중"),
    R10("세탁완료"),
    R11("수거전"),
    R12("수거완료"),
    R13("배달완료");

    private String title;

    OrderStatus(String title) {
        this.title = title;
    }
}
