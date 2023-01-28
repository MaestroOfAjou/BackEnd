package Moa.Web.DTO;

import lombok.Data;

@Data
public class RegenerateTokenDTO {
    private String refreshToken;
    private String address;
}
