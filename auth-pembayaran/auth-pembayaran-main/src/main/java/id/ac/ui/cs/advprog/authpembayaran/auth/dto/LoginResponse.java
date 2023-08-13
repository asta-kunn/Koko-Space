package id.ac.ui.cs.advprog.authpembayaran.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String name;
    private String email;
    private Double saldo;
    private String role;
    private Boolean verified;

}
