package fr.guen.dev.sgm.payload.common;

import fr.guen.dev.sgm.common.enums.Role;
import fr.guen.dev.sgm.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private Role role;
    private Status status;

}
