package Moa.Web.DTO;

import lombok.Data;

@Data
public class MemberLoginRequestDto {
    private String address;
    private String password;
}
