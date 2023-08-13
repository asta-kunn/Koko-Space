package id.ac.ui.cs.advprog.authpembayaran.service.pembayaran;

import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.component.CouponUseManager;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponCURequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponDeleteRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponUseRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.CouponNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.UserNotAllowedException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.CouponUsage;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Status;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponUsageRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class CouponServiceImplTest {
    @Mock
    private CouponRepository couponRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private CouponUsageRepository couponUsageRepository;
    
    @InjectMocks
    private CouponUseManager couponUseManager;
    
    @InjectMocks
    private CouponServiceImpl couponService;
    
    private Coupon testCoupon;
    private User testUser;
    private CouponUsage testCouponUsage;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = User.builder()
                .id(1)
                .email("johndoe@example.com")
                .password("test")
                .name("test")
                .verified(false)
                .active(true)
                .role(Role.PELANGGAN)
                .build();
        testCoupon = Coupon.builder()
                .id(1L)
                .code("test")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis() - 2140000000))
                .endDate(new Date(System.currentTimeMillis() + 2140000000))
                .minPrice(100.0)
                .maxUsage(100)
                .user(testUser)
                .status(Status.AVAILABLE)
                .build();
        testCouponUsage = CouponUsage.builder()
                .id(1L)
                .coupon(testCoupon)
                .user(testUser)
                .usageTime(new Date(System.currentTimeMillis()))
                .build();
    }
    
    @Test
    void whenCreateCouponButUserNotFoundShouldThrowException() {
        CouponCURequest couponCURequest = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(100)
                .userId(3)
                .build();
        when(userRepository.findById(anyInt())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> couponService.createCoupon(couponCURequest));
        verify(userRepository, times(1)).findById(anyInt());
    }
    
    @Test
    void whenCreateCouponSucceeded() {
        CouponCURequest couponCURequest = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(100)
                .userId(1)
                .build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(testUser));
        assertDoesNotThrow(() -> couponService.createCoupon(couponCURequest));
        verify(userRepository, times(1)).findById(anyInt());
        verify(couponRepository, times(1)).save(any(Coupon.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void whenDeleteCouponButCouponNotFound() {
        CouponDeleteRequest couponDeleteRequest = CouponDeleteRequest.builder()
                .userId(1)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> couponService.deleteCoupon(couponDeleteRequest, 1L));
        verify(couponRepository, never()).delete(any(Coupon.class));
    }
    
    @Test
    void whenDeleteCouponButUserNotAllowed() {
        CouponDeleteRequest couponDeleteRequest = CouponDeleteRequest.builder()
                .userId(2)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testCoupon));
        assertThrows(UserNotAllowedException.class, () -> couponService.deleteCoupon(couponDeleteRequest, 1L));
        verify(couponRepository, never()).delete(any(Coupon.class));
    }
    
    @Test
    void whenDeleteCouponSucceeded() {
        CouponDeleteRequest couponDeleteRequest = CouponDeleteRequest.builder()
                .userId(1)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testCoupon));
        assertDoesNotThrow(() -> couponService.deleteCoupon(couponDeleteRequest, 1L));
        verify(couponRepository, times(1)).delete(any(Coupon.class));
    }
    
    @Test
    void whenUpdateCouponButCouponNotFound() {
        CouponCURequest request = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(100)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CouponNotFoundException.class, () -> couponService.updateCoupon(request, 1L));
        verify(couponRepository, times(1)).findById(anyLong());
    }
    
    @Test
    void whenUpdateCouponButUserNotFound() {
        CouponCURequest request = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(100)
                .userId(1)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> couponService.updateCoupon(request, 1L));
        verify(couponRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyInt());
    }
    
    @Test
    void whenUpdateCouponButUserNotAllowed() {
        CouponCURequest request = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(100)
                .userId(3)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        assertThrows(UserNotAllowedException.class, () -> couponService.updateCoupon(request, 1L));
        verify(couponRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyInt());
    }
    
    @Test
    void whenUpdateCouponButCouponMaxed() {
        CouponCURequest request = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(0)
                .userId(1)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        assertDoesNotThrow( () -> couponService.updateCoupon(request, 1L));
        verify(couponRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyInt());
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }
    
    @Test
    void whenUpdateCouponSucceeded() {
        CouponCURequest request = CouponCURequest.builder()
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(10000.0)
                .maxUsage(100)
                .userId(1)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        assertDoesNotThrow( () -> couponService.updateCoupon(request, 1L));
        verify(couponRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyInt());
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }
    
    @Test
    void whenUseCouponSucceeded() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        when(couponRepository.findById(anyInt())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(couponUsageRepository.countByCouponId(anyLong())).thenReturn(0L);
        Double response = couponService.useCoupon(1, 1);
        assertEquals( 100.0, response);
    }
    
    @Test
    void whenUseCouponAndAlreadyUsed() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        testUser.add(testCouponUsage);
        when(couponRepository.findById(anyInt())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        Double response = couponService.useCoupon(1, 1);
        assertEquals(0.0, response);
    }
    
    @Test
    void whenUseCouponAndAlreadyExpired() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        testCoupon.setEndDate(new Date(System.currentTimeMillis() - 100000));
        when(couponRepository.findById(anyInt())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        Double response = couponService.useCoupon(1, 1);
        assertEquals(0.0, response);
    }
    
    @Test
    void whenUseCouponAndAlreadyMaxUsage() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        testCoupon.setMaxUsage(0);
        when(couponRepository.findById(anyInt())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        Double response = couponService.useCoupon(1, 1);
        assertEquals(0.0, response);
    }
    
    @Test
    void whenGetPriceUsingExpiredCoupon() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        testCoupon.setEndDate(new Date(System.currentTimeMillis() - 100000));
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        Double response = couponService.getPriceAfterDiscount(request, 1L);
        assertEquals(10000.0, response);
    }
    
    @Test
    void whenGetPriceUsingCouponSucceeded() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(couponUsageRepository.countByCouponId(anyLong())).thenReturn(0L);
        Double response = couponService.getPriceAfterDiscount(request, 1L);
        assertEquals( 10000.0-100.0, response);
        verify(couponRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyInt());
        verify(couponUsageRepository, times(1)).countByCouponId(anyLong());
    }
    
    @Test
    void whenGetPriceUsingMaxUsedCoupon() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(10000.0)
                .build();
        testCoupon.setMaxUsage(0);
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(testCoupon));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        Double response = couponService.getPriceAfterDiscount(request, 1L);
        assertEquals( 10000.0, response );
    }
}
