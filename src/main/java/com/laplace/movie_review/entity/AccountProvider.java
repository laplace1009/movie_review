package com.laplace.movie_review.entity;

import com.laplace.movie_review.dto.accountProvider.AccountProviderCreateDTO;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "AccountProvider{" +
                "id=" + id +
                ", account=" + account +
                ", providerName='" + providerName + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }

    public AccountProviderCreateDTO toDto() {
        return new AccountProviderCreateDTO(account.getUserId(), providerName, providerId);
    }
}
