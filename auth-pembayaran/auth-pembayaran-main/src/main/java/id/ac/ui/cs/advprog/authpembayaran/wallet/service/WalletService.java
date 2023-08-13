package id.ac.ui.cs.advprog.authpembayaran.wallet.service;

import java.util.List;

import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;


public interface WalletService {
    public List<WalletHistory> getAllUserWalletHistory(Integer userId);
    public List<WalletHistory> getAllTopupRequest();
    public TopupResponse topup(Integer userId, TopupRequest request);
    public VerifyTopupResponse verifyTopup(VerifyTopupRequest request);
    public List<WalletHistory> getAllUserExpensesHistory(Integer userId);
}