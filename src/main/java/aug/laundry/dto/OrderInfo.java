package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderInfo {

    private Long isQuick;
    private Long isDry;
    private Long isCommon;
    private Long isRepair;

    public OrderInfo(Long isQuick, Long isDry, Long isCommon, Long isRepair) {
        this.isQuick = isQuick;
        this.isDry = isDry;
        this.isCommon = isCommon;
        this.isRepair = isRepair;
    }
}
