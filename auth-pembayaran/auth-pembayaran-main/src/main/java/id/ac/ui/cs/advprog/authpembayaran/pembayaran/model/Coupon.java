package id.ac.ui.cs.advprog.authpembayaran.pembayaran.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "_coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Double discount;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    private Double minPrice;

    private Integer maxUsage;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    @SuppressWarnings("squid:S1948")
    @Builder.Default
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<CouponUsage> usages = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "_user_id", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private User user;
    
    public void add(CouponUsage usage) {
        usages.add(usage);
    }

}
