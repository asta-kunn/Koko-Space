package id.ac.ui.cs.advprog.authpembayaran.service.wallet;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.TopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupRequest;
import id.ac.ui.cs.advprog.authpembayaran.wallet.dto.VerifyTopupResponse;
import id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistory;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryStatus;
import id.ac.ui.cs.advprog.authpembayaran.wallet.model.WalletHistoryType;
import id.ac.ui.cs.advprog.authpembayaran.wallet.repository.WalletRepository;
import id.ac.ui.cs.advprog.authpembayaran.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {
    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletHistory walletHistory;

    @Mock
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1) // mock ID
                .email("test@test.com")
                .password("test")
                .name("test")
                .verified(false)
                .active(true)
                .saldo(10000.0)
                .role(Role.PELANGGAN)
                .build();

        walletHistory = WalletHistory.builder()
                .id(1)
                .amount(10000.0)
                .type(WalletHistoryType.PEMASUKAN)
                .verified(false)
                .method("PayPal")
                .detail("Topup Deyails")
                .createdAt(new Date())
                .user(testUser)
                .status(WalletHistoryStatus.BEING_REVIEWED)
                .build();
        walletRepository.save(walletHistory);
    }

    @Test
    void whenTopupWithAmountFieldIsEmptyShouldThrowException() {
        TopupRequest request = new TopupRequest();
        request.setMethod("PayPal");
        request.setDetail("Payment details");

        assertThrows(AmountMethodDetailRequiredException.class, () -> walletService.topup(1, request));

        verify(userRepository, never()).findById(anyInt());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void whenTopupWithMethodFieldIsEmptyShouldThrowException() {
        TopupRequest request = TopupRequest.builder().amount(1000.0).detail("Payment Details").build();

        assertThrows(AmountMethodDetailRequiredException.class, () -> walletService.topup(1, request));

        verify(userRepository, never()).findById(anyInt());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void whenTopupWithDetailFieldIsEmptyShouldThrowException() {
        TopupRequest topupRequest = new TopupRequest(1000.0, "PayPal", null);

        assertThrows(AmountMethodDetailRequiredException.class, () -> walletService.topup(1, topupRequest));

        verify(userRepository, never()).findById(anyInt());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void whenTopupWithMethodFieldIsEmptyStringShouldThrowException() {
        TopupRequest request = new TopupRequest();
        request.setAmount(10000.0);
        request.setMethod("");
        request.setDetail("Payment details");

        assertThrows(AmountMethodDetailRequiredException.class, () -> walletService.topup(1, request));

        verify(userRepository, never()).findById(anyInt());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void whenTopupWithDetailFieldIsEmptyStringShouldThrowException() {
        TopupRequest request = new TopupRequest();
        request.setAmount(10000.0);
        request.setMethod("Paypal");
        request.setDetail("");

        assertThrows(AmountMethodDetailRequiredException.class, () -> walletService.topup(1, request));

        verify(userRepository, never()).findById(anyInt());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void whenTopupWithNegativeAmountShouldThrowException() {
        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setAmount(-1000.0);
        topupRequest.setMethod("PayPal");
        topupRequest.setDetail("TopUpDetail");

        assertThrows(InvalidTopupAmountException.class, () -> walletService.topup(1, topupRequest));

        verify(userRepository, never()).findById(anyInt());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void whenToupUpSuccessWalletHistorySaved(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setAmount(100.0);
        topupRequest.setMethod("PayPal");
        topupRequest.setDetail("Payment details");

        TopupResponse topupResponse = walletService.topup(1,topupRequest);
        verify(walletRepository,times(2)).save(any());
        assertNotNull(topupResponse);
        assertEquals(topupRequest.getAmount(),topupResponse.getAmount());
    }

    @Test
    void whenVerifyTopUpResponseWithInvalidWalletHistoryIdThrowsException() {
        VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest(99, true);

        assertThrows(WalletHistoryNotFoundException.class, () -> walletService.verifyTopup(verifyTopupRequest));

        verify(walletRepository, times(1)).save(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenVerifyTopUpResponseWithWalletHistoryIdIsNullThrowsException() {
        VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest(null, true);

        assertThrows(WallethistoryidAcceptedRequiredException.class, () -> walletService.verifyTopup(verifyTopupRequest));

        verify(walletRepository, times(1)).save(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenVerifyTopUpResponseWithAcceptedIsNullThrowsException() {
        VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest(1, null);

        assertThrows(WallethistoryidAcceptedRequiredException.class, () -> walletService.verifyTopup(verifyTopupRequest));

        verify(walletRepository, times(1)).save(any());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void whenVerifyTopUpResponseWithVerifiedWalletHistoryThrowsException() {

        WalletHistory invalidWalletHistory = WalletHistory.builder()
                .id(2)
                .amount(10000.0)
                .type(WalletHistoryType.PEMASUKAN)
                .verified(true)
                .method("PayPal")
                .detail("Topup Deyails")
                .createdAt(new Date())
                .user(testUser)
                .status(WalletHistoryStatus.BEING_REVIEWED)
                .build();
        walletRepository.save(invalidWalletHistory);

        when(walletRepository.findById(any(Integer.class))).thenReturn(Optional.of(invalidWalletHistory));

        VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest();
        verifyTopupRequest.setWalletHistoryId(2);
        verifyTopupRequest.setAccepted(true);

        assertThrows(InvalidTopupWalletHistoryException.class, () -> walletService.verifyTopup(verifyTopupRequest));

        verify(walletRepository, times(2)).save(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenVerifyTopUpResponseWithWrongTypeWalletHistoryThrowsException() {

        WalletHistory invalidWalletHistory = WalletHistory.builder()
                .id(2)
                .amount(10000.0)
                .type(WalletHistoryType.PENGELUARAN)
                .verified(false)
                .method("PayPal")
                .detail("Topup Deyails")
                .createdAt(new Date())
                .user(testUser)
                .status(WalletHistoryStatus.BEING_REVIEWED)
                .build();
        walletRepository.save(invalidWalletHistory);

        when(walletRepository.findById(any(Integer.class))).thenReturn(Optional.of(invalidWalletHistory));

        VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest();
        verifyTopupRequest.setWalletHistoryId(2);
        verifyTopupRequest.setAccepted(true);

        assertThrows(InvalidTopupWalletHistoryException.class, () -> walletService.verifyTopup(verifyTopupRequest));

        verify(walletRepository, times(2)).save(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenVerifyAcceptedTopupSuccessWalletHistoryStatusChanged(){
        when(walletRepository.findById(any(Integer.class))).thenReturn(Optional.of(walletHistory));

        double saldoBefore = testUser.getSaldo();
        double topupAmount = walletHistory.getAmount();

        VerifyTopupRequest verifyTopupRequest = VerifyTopupRequest.builder().accepted(true).walletHistoryId(1).build();

        VerifyTopupResponse verifyTopupResponse = walletService.verifyTopup(verifyTopupRequest);
        verify(walletRepository,times(2)).save(any());
        assertNotNull(verifyTopupResponse);
        if(verifyTopupRequest.getAccepted()) {
            assertEquals("ACCEPTED",verifyTopupResponse.getStatus());
        }else{
            assertEquals("REJECTED",verifyTopupResponse.getStatus());
        }
        assertEquals(saldoBefore + topupAmount, verifyTopupResponse.getNewSaldo());
    }

    @Test
    void whenVerifyRejectedTopupSuccessWalletHistoryStatusChanged(){
        when(walletRepository.findById(any(Integer.class))).thenReturn(Optional.of(walletHistory));

        double saldoBefore = testUser.getSaldo();
        double topupAmount = walletHistory.getAmount();

        VerifyTopupRequest verifyTopupRequest = new VerifyTopupRequest();
        verifyTopupRequest.setWalletHistoryId(1);
        verifyTopupRequest.setAccepted(false);

        VerifyTopupResponse verifyTopupResponse = walletService.verifyTopup(verifyTopupRequest);
        verify(walletRepository,times(2)).save(any());
        assertNotNull(verifyTopupResponse);
        if(verifyTopupRequest.getAccepted()) {
            assertEquals("ACCEPTED",verifyTopupResponse.getStatus());
        }else{
            assertEquals("REJECTED",verifyTopupResponse.getStatus());
        }
        assertEquals(saldoBefore + topupAmount, verifyTopupResponse.getNewSaldo());
    }

    @Test
    void whengGetAllTopupRequestSuccessReturnAllWalletHistory() {
        WalletHistory walletHistory2 = WalletHistory.builder()
                .id(3)
                .amount(10000.0)
                .type(WalletHistoryType.PEMASUKAN)
                .verified(false)
                .method("PayPal")
                .detail("Topup Deyails")
                .createdAt(new Date())
                .user(testUser)
                .status(WalletHistoryStatus.BEING_REVIEWED)
                .build();
        walletRepository.save(walletHistory2);

        List<WalletHistory> expectedTopupRequests = new ArrayList<>();
        expectedTopupRequests.add(walletHistory);
        expectedTopupRequests.add(walletHistory2);

        // Mock the behavior of walletRepository
        when(walletRepository.findAllByTypeAndStatus(WalletHistoryType.PEMASUKAN, WalletHistoryStatus.BEING_REVIEWED))
                .thenReturn(expectedTopupRequests);

        // Call the method under test
        List<WalletHistory> actualTopupRequests = walletService.getAllTopupRequest();

        // Assert the result
        assertEquals(expectedTopupRequests, actualTopupRequests);
    }

    @Test
    void whenGetAllUserWalletHistorySuccessReturnWalletHistory() {
        WalletHistory walletHistory2 = WalletHistory.builder()
                .id(3)
                .amount(10000.0)
                .type(WalletHistoryType.PEMASUKAN)
                .verified(false)
                .method("PayPal")
                .detail("Topup Deyails")
                .createdAt(new Date())
                .user(testUser)
                .status(WalletHistoryStatus.REJECTED)
                .build();
        walletRepository.save(walletHistory2);

        List<WalletHistory> expectedTopupRequests = new ArrayList<>();
        expectedTopupRequests.add(walletHistory);
        expectedTopupRequests.add(walletHistory2);

        // Mock the behavior of walletRepository
        when(walletRepository.findAllByUserId(testUser.getId()))
                .thenReturn(expectedTopupRequests);

        // Call the method under test
        List<WalletHistory> actualTopupRequests = walletService.getAllUserWalletHistory(testUser.getId());

        // Assert the result
        assertEquals(expectedTopupRequests, actualTopupRequests);
    }

    @Test
    void whenGetAllUserExpensesHistorySuccessReturnUserExpensesHistory() {

        WalletHistory expense = WalletHistory.builder()
                .id(4)
                .amount(100_000.00)
                .type(WalletHistoryType.PENGELUARAN)
                .verified(false)
                .method("PayPal")
                .detail("Expenses Detail")
                .createdAt(new Date())
                .user(testUser)
                .build();

        walletRepository.save(expense);

        List<WalletHistory> expectedResult = new ArrayList<>();
        expectedResult.add(expense);

        // Mock the behavior of walletRepository
        when(walletRepository.findAllExpensesByUserId(testUser.getId()))
                .thenReturn(expectedResult);

        // Call the method under test
        List<WalletHistory> actualResult = walletService.getAllUserExpensesHistory(testUser.getId());

        // Assert the result
        assertEquals(expectedResult, actualResult);
    }
}
