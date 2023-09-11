package aug.laundry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RepairInfoDto {

    private Long repairId;
    private Long ordersDetailId;
    private String repairRequest;
    private String repairCategory;
    private char repairPossibility;
    private String repairNotReason;
    private String repairImageUploadName;
    private String repairImageStoreName;

    public RepairInfoDto() {

    }
}
