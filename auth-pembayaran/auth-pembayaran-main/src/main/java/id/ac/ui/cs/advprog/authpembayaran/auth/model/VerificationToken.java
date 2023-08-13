package id.ac.ui.cs.advprog.authpembayaran.auth.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "_VToken")
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String token;
    
    private String email;
    
    @Temporal(TemporalType.TIME)
    private Date expiryDate;
    
    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, VerificationToken.EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
    
    public VerificationToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expiryDate = calculateExpiryDate();
    }
}
