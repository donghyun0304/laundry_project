package aug.laundry.dto;

import aug.laundry.domain.Repair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RepairListDto {

    private List<Repair> repairList;

    public RepairListDto() {

    }
}
