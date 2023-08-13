package id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDeleteRequest {
    private Integer userId;
}
