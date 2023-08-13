package id.ac.ui.cs.advprog.authpembayaran.wallet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_wallet_history")
public class WalletHistory {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Integer id;

    private Double amount;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private WalletHistoryType type;
    private Boolean verified;
    private String rentalName;



    private String method;
    @Enumerated(EnumType.STRING)
    private WalletHistoryStatus status;
    private String detail;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
