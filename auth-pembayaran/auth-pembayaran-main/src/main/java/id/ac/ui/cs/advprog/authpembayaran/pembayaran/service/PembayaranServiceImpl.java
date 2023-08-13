package id.ac.ui.cs.advprog.authpembayaran.pembayaran.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranResponse;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.AmountAndRentalNameRequiredException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.InsufficientFundsException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.InvalidAmountException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.MinimalAmountForCouponException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.WrongUserCoupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryType;
import id.ac.ui.cs.advprog.authpembayaran.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PembayaranServiceImpl implements PembayaranService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final CouponRepository couponRepository;
    private final CouponService couponService;

    @Override
    public PembayaranResponse pay(Integer id, PembayaranRequest request) {

        var user = userRepository.findById(id).orElseThrow();
        var coupon = couponRepository.findById(request.getKuponId()).orElse(null);
        Double amount;

        if (request.getAmount() == null || request.getRentalName() == null) {
            throw new AmountAndRentalNameRequiredException();
        }

        if (request.getAmount() < 0) {
            throw new InvalidAmountException();
        }
        if (user.getSaldo() < request.getAmount() - (coupon == null ? 0 : coupon.getDiscount())) {
            throw new InsufficientFundsException();
        }

        if (coupon != null) {

            if (!user.equals(coupon.getUser())) {
                throw new WrongUserCoupon();
            }

            if (request.getAmount() < coupon.getMinPrice()) {
                throw new MinimalAmountForCouponException();
            }

            Double discount = couponService.useCoupon(request.getKuponId(), id);
            amount = request.getAmount() - discount < 0 ? 0 : request.getAmount() - discount;

        } else {
            amount = request.getAmount();
        }

        var history = WalletHistory.builder().amount(amount).type(WalletHistoryType.PENGELUARAN).verified(true)
                .createdAt(new Date()).user(user).rentalName(request.getRentalName()).build();
        user.setSaldo(user.getSaldo() - amount);
        walletRepository.save(history);
        return PembayaranResponse.builder().id(history.getId()).type(WalletHistoryType.PENGELUARAN.toString()).amount(amount)
                .createdAt(new Date()).build();
    }
}
