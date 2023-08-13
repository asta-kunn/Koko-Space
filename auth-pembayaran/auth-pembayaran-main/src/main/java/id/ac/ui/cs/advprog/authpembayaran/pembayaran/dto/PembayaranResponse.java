package id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PembayaranResponse {
    private Integer id;
    private Double amount;
    private Date createdAt;
    private String type;
}
