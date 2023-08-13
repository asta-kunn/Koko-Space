package id.ac.ui.cs.advprog.authpembayaran.wallet.repository;

import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryStatus;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletHistory, Integer> {
    List<WalletHistory> findAllByUserId(Integer userId);
    List<WalletHistory> findAllByTypeAndStatus(WalletHistoryType type, WalletHistoryStatus status);

    @Query(value = "SELECT * FROM _wallet_history WHERE type = 'PENGELUARAN' AND user_id = ?1", nativeQuery = true)
    List<WalletHistory> findAllExpensesByUserId(Integer userId);
}
