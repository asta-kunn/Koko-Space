package id.ac.ui.cs.advprog.authpembayaran.wallet.service;

import java.util.Date;
import java.util.List;

import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions.*;

import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryStatus;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryType;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;
import id.ac.ui.cs.advprog.authpembayaran.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    public List<WalletHistory> getAllUserWalletHistory(Integer userId) {
        return walletRepository.findAllByUserId(userId);
    }

    @Override
    public List<WalletHistory> getAllUserExpensesHistory(Integer userId) {
        return walletRepository.findAllExpensesByUserId(userId);
    }

    @Override
    public List<WalletHistory> getAllTopupRequest(){
        return walletRepository.findAllByTypeAndStatus(WalletHistoryType.PEMASUKAN, WalletHistoryStatus.BEING_REVIEWED);
    }


    @Override
    public TopupResponse topup(Integer userId, TopupRequest request){

        //check input
        if (request.getAmount() == null || request.getMethod() == null || request.getDetail() == null
        || request.getMethod().equals("") || request.getDetail().equals("")) {
            throw new AmountMethodDetailRequiredException();
        }

        if (request.getAmount() <= 0) {
            throw new InvalidTopupAmountException();
        }

        var user = userRepository.findById(userId).orElse(null);

        var history = WalletHistory.builder()
                .amount(request.getAmount())
                .type(WalletHistoryType.PEMASUKAN)
                .verified(false)
                .method(request.getMethod())
                .detail(request.getDetail())
                .createdAt(new Date())
                .user(user)
                .status(WalletHistoryStatus.BEING_REVIEWED)
                .build();
        walletRepository.save(history);

        return TopupResponse.builder()
                .walletHistoryId(history.getId())
                .amount(history.getAmount())
                .createdAt(history.getCreatedAt())
                .method(history.getMethod())
                .build();
    }

    @Override
    public VerifyTopupResponse verifyTopup(VerifyTopupRequest request) {

        //check input
        if (request.getWalletHistoryId() == null || request.getAccepted() == null) {
            throw new WallethistoryidAcceptedRequiredException();
        }

        var history = walletRepository.findById(request.getWalletHistoryId()).orElse(null);

        //check if history able to be verified
        if(history==null) {
            throw new WalletHistoryNotFoundException();
        }else if(Boolean.TRUE.equals(history.getVerified()) || history.getType() == WalletHistoryType.PENGELUARAN){
            throw new InvalidTopupWalletHistoryException();
        }
        var user = history.getUser();

        WalletHistoryStatus status;
        if(Boolean.TRUE.equals(request.getAccepted())){
            status = WalletHistoryStatus.ACCEPTED;
        }else{
            status = WalletHistoryStatus.REJECTED;
        }

        history.setStatus(status);
        history.setVerified(true);
        walletRepository.save(history);

        user.setSaldo(history.getAmount() + user.getSaldo());
        userRepository.save(user);

        return VerifyTopupResponse.builder()
                .id(history.getId())
                .userId(user.getId())
                .amount(history.getAmount())
                .createdAt(new Date())
                .status(history.getStatus().toString())
                .newSaldo(user.getSaldo())
                .build();

    }
}
