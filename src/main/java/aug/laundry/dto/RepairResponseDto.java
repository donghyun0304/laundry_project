package aug.laundry.dto;

import aug.laundry.enums.category.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@ToString
public class RepairResponseDto {

    private Category repairCategory;
    private char repairPossibility;
    private String repairNotReason;
    @NumberFormat(pattern = "###,###")
    private Long repairPrice;

    public RepairResponseDto(Category repairCategory, char repairPossibility, String repairNotReason, Long repairPrice) {
        this.repairCategory = repairCategory;
        this.repairPossibility = repairPossibility;
        this.repairNotReason = repairNotReason;
        this.repairPrice = repairPrice;
    }
}
