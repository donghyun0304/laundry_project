package aug.laundry.dto;

import aug.laundry.domain.Drycleaning;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DrycleaningListDto {

    private List<Drycleaning> drycleaningList;

    public DrycleaningListDto() {

    }
}
