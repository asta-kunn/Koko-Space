package id.ac.ui.cs.advprog.authpembayaran.wallet.controller;

import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;
import id.ac.ui.cs.advprog.authpembayaran.wallet.service.WalletService;

import static id.ac.ui.cs.advprog.authpembayaran.auth.controller.LoginController.getCurrentUser;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<List<WalletHistory>> getAllUserExpensesHistory() {
        return ResponseEntity.ok(walletService.getAllUserExpensesHistory(getCurrentUser().getId()));
    }

    @GetMapping("/history-all")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<List<WalletHistory>> getAllUserWalletHistory() {
        return ResponseEntity.ok(walletService.getAllUserWalletHistory(getCurrentUser().getId()));
    }

    @GetMapping("/get-all-topup-request")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<List<WalletHistory>> getAllTopupRequest() {
        return ResponseEntity.ok(walletService.getAllTopupRequest());
    }

    @PostMapping("/topup")
    @PreAuthorize("hasAuthority('PELANGGAN')")
    public ResponseEntity<TopupResponse> topup(@RequestBody TopupRequest request) {
        return ResponseEntity.ok(walletService.topup(getCurrentUser().getId(), request));
    }

    @PostMapping("/verify-topup")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<VerifyTopupResponse> verifyTopup(@RequestBody VerifyTopupRequest request) {
        return ResponseEntity.ok(walletService.verifyTopup(request));
    }
}
