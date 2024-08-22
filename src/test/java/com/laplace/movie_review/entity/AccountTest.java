package com.laplace.movie_review.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class AccountTest {
    @Test
    public void testAccount() {
        String username = "test";
        String email = "test@test.com";
        String password = "1234";

        Account account = new Account(username, email, password);

        assertEquals(username, account.getUsername());
        assertEquals(email, account.getEmail());
        assertEquals(password, account.getPassword());
    }

    @Test
    public void testPrePersist() {
        Account account = new Account();
        account.onCreate();

        assertNotNull(account.getCreatedAt());
        assertTrue(LocalDateTime.now().isAfter(account.getCreatedAt()));
    }

    @Test
    public void testSettersAndGetters() {
        Account account = new Account();
        Long id = 1L;
        String username = "test";
        String email = "test@test.com";
        String password = "1234";

        account.setId(id);
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(password);

        assertEquals(id, account.getId());
        assertEquals(username, account.getUsername());
        assertEquals(email, account.getEmail());
        assertEquals(password, account.getPassword());
    }
}
