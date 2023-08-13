package id.ac.ui.cs.advprog.authpembayaran.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordResponse {

    private String email;
    private String resetLink;
    private ZonedDateTime sentAt;



}
