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
public class TopupResponse {
    private Integer walletHistoryId;
    private Double amount;
    private Date createdAt;
    private String method;
}
