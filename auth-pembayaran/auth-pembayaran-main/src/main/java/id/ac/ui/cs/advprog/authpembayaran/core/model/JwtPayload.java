package id.ac.ui.cs.advprog.authpembayaran.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtPayload {
    private Integer userId;
    private String role;
    private String name;
    private Boolean active;
    private Double saldo;
}
