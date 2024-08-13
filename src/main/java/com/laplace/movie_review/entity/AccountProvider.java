package com.laplace.movie_review.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account_providers")
public class AccountProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, length = 50)
    private String providerName;

    @Column(length = 100)
    private String providerId;

    public AccountProvider() {}

    public AccountProvider(Account account, String providerName, String providerId) {
        this.account = account;
        this.providerName = providerName;
        this.providerId = providerId;
    }
}
