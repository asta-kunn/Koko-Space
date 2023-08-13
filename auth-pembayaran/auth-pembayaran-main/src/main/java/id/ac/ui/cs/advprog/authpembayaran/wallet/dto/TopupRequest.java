package id.ac.ui.cs.advprog.authpembayaran.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopupRequest {
    private Double amount;
    private String method;
    private String detail;
}