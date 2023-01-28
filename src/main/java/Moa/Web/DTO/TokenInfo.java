package Moa.Web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {

    private String grantType;
    private String accessToken;
    private String refreshToken;
}
