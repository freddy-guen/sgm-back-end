package fr.guen.dev.sgm.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponse {
    private JwtAuthenticationResponse jwtAuthenticationResponse;
    private String message;
    private HttpStatus httpStatus;
}
