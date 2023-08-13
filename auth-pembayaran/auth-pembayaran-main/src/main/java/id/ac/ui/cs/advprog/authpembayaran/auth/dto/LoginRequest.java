package id.ac.ui.cs.advprog.authpembayaran.auth.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoginRequest {

    private String email;
    String password;
}
