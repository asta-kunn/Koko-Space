package id.ac.ui.cs.advprog.authpembayaran.pembayaran.controller;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranResponse;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.PembayaranService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static id.ac.ui.cs.advprog.authpembayaran.auth.controller.LoginController.getCurrentUser;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PembayaranController {

    private final PembayaranService pembayaranService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<PembayaranResponse> pay(@RequestBody PembayaranRequest request) {
        return ResponseEntity.ok(pembayaranService.pay(getCurrentUser().getId(), request));
    }

}
