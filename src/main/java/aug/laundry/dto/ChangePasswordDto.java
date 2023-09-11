package aug.laundry.dto;

import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,15}$")
  private String memberPassword;

}
