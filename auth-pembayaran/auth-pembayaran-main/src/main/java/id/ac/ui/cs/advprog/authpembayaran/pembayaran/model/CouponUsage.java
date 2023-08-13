package id.ac.ui.cs.advprog.authpembayaran.pembayaran.model;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Table(name = "_coupon_usage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class CouponUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Coupon coupon;

    @ManyToOne
    private User user;

    @Temporal(TemporalType.TIME)
    private Date usageTime;

    public CouponUsage(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        this.usageTime = new Date(cal.getTime().getTime());
    }
}
