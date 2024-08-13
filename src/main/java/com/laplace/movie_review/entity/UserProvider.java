package com.laplace.movie_review.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_providers")
public class UserProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String providerName;

    @Column(length = 100)
    private String providerId;

    public UserProvider() {}

    public UserProvider(User user, String providerName, String providerId) {
        this.user = user;
        this.providerName = providerName;
        this.providerId = providerId;
    }
}
