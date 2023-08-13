package id.ac.ui.cs.advprog.authpembayaran.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyTopupResponse {
    private Integer id;
    private Integer userId;
    private Double amount;
    private Double newSaldo;
    private Date createdAt;
    private String status;

}
