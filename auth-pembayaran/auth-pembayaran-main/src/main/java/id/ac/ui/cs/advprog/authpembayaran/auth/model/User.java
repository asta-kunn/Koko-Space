package id.ac.ui.cs.advprog.authpembayaran.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.CouponUsage;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private Double saldo;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean verified;
    private boolean active;
    
    @Getter
    @OneToMany(mappedBy = "user")
    @SuppressWarnings("squid:S1948")
    @Builder.Default
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<CouponUsage> couponUsed = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @SuppressWarnings("squid:S1948")
    @Builder.Default
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Coupon> couponMade = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public String getRole() {
        return this.role.name();
    }
    
    public void add(CouponUsage usage) {
        couponUsed.add(usage);
    }
    
    public void create(Coupon coupon) {
        couponMade.add(coupon);
    }
    
    @SuppressWarnings("squid:S5411")
    public synchronized boolean setVerified() {
        if (!this.verified) {
            this.verified = true;
            return false;
        }
        return true;
    }
}
