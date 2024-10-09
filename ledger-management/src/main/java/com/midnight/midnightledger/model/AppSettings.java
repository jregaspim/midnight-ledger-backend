package com.midnight.midnightledger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_settings")
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "notification_preferences")
    private boolean notificationPreferences;

    @Column(nullable = false)
    private String currency;

    public AppSettings(Long accountId, String currency, boolean notificationPreferences) {
        this.accountId = accountId;
        this.currency = currency;
        this.notificationPreferences = notificationPreferences;
    }
}
