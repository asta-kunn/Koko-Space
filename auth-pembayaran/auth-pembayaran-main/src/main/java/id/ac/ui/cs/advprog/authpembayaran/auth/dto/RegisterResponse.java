package id.ac.ui.cs.advprog.authpembayaran.auth.dto;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String name;
    private String email;
    private Boolean isVerified;
}
