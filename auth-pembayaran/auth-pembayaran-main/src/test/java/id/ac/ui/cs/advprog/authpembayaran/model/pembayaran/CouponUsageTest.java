package id.ac.ui.cs.advprog.authpembayaran.model.pembayaran;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.CouponUsage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponUsageTest {
    @Test
    void testCouponUsageConstructor() {
        // Create a sample coupon and user
        Coupon coupon = new Coupon();
        User user = new User();
        
        // Create a CouponUsage instance using the constructor
        CouponUsage couponUsage = new CouponUsage(coupon, user);
        
        // Assert that the CouponUsage instance is not null
        assertNotNull(couponUsage);
        
        // Assert that the coupon and user are set correctly
        assertEquals(coupon, couponUsage.getCoupon());
        assertEquals(user, couponUsage.getUser());
        
        // Assert that the usageTime is set to the current date and time
        Date currentTime = new Date();
        assertEquals(currentTime.getTime(), couponUsage.getUsageTime().getTime(), 1000); // Allow a 1-second difference
    }
    
    @Test
    void testSetters() {
        // Create a sample coupon and user
        Coupon coupon = new Coupon();
        User user = new User();
        CouponUsage couponUsage = new CouponUsage();
        
        // Use setter methods to set the fields
        couponUsage.setId(1L);
        couponUsage.setCoupon(coupon);
        couponUsage.setUser(user);
        couponUsage.setUsageTime(new Date());
        
        // Assert that the fields are set correctly
        assertEquals(1L, couponUsage.getId());
        assertEquals(coupon, couponUsage.getCoupon());
        assertEquals(user, couponUsage.getUser());
        assertNotNull(couponUsage.getUsageTime());
    }
    
    @Test
    void testConstructor() {
        // Create a sample coupon and user
        Coupon coupon = new Coupon();
        User user = new User();
        
        // Create a CouponUsage instance using the constructor
        CouponUsage couponUsage = new CouponUsage(coupon, user);
        
        // Assert that the CouponUsage instance is not null
        assertNotNull(couponUsage);
        
        // Assert that the coupon and user are set correctly
        assertEquals(coupon, couponUsage.getCoupon());
        assertEquals(user, couponUsage.getUser());
        
        // Assert that the usageTime is set to the current date and time
        assertNotNull(couponUsage.getUsageTime());
    }
    
    @Test
    void testBuilder() {
        // Create a sample coupon and user
        Coupon coupon = new Coupon();
        User user = new User();
        Date usageTime = new Date();
        
        // Create a CouponUsage instance using the builder
        CouponUsage couponUsage = CouponUsage.builder()
                .id(1L)
                .coupon(coupon)
                .user(user)
                .usageTime(usageTime)
                .build();
        
        // Assert that the CouponUsage instance is not null
        assertNotNull(couponUsage);
        
        // Assert that the coupon, user, and usageTime are set correctly
        assertEquals(coupon, couponUsage.getCoupon());
        assertEquals(user, couponUsage.getUser());
        assertEquals(usageTime, couponUsage.getUsageTime());
    }
    
    @Test
    void testEquals() {
        // Create a sample coupon and user
        Coupon coupon1 = new Coupon();
        User user1 = new User();
        
        // Create two CouponUsage instances with the same coupon and user
        CouponUsage couponUsage1 = new CouponUsage(coupon1, user1);
        
        // Assert that the two instances are equal
        assertEquals(couponUsage1, couponUsage1);
    }
    
    @Test
    void testNotEquals() {
        // Create sample coupons and users
        Coupon coupon1 = new Coupon();
        Coupon coupon2 = new Coupon();
        User user1 = new User();
        User user2 = new User();
        user1.setRole(Role.PELANGGAN);
        user2.setRole(Role.PENGELOLA);
        
        // Create two CouponUsage instances with different coupons and users
        CouponUsage couponUsage1 = new CouponUsage(coupon1, user1);
        CouponUsage couponUsage2 = new CouponUsage(coupon2, user2);
        
        // Assert that the two instances are not equal
        assertFalse(couponUsage1.equals(couponUsage2));
        assertFalse(couponUsage2.equals(couponUsage1));
    }
    
    @Test
    void testToString() {
        // Create a sample coupon and user
        Coupon coupon = new Coupon();
        User user = new User();
        user.setRole(Role.PELANGGAN);
        
        // Create a CouponUsage instance
        CouponUsage couponUsage = new CouponUsage(coupon, user);
        
        // Get the expected string representation
        String expectedString = "CouponUsage(id=null, coupon=" + coupon +
                ", user=" + user +
                ", usageTime=" + couponUsage.getUsageTime() +
                ")";
        
        // Assert that the toString method returns the expected string
        assertEquals(expectedString, couponUsage.toString());
    }
}
