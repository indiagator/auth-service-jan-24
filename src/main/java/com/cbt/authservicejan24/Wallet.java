package com.cbt.authservicejan24;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @Column(name = "walletid", nullable = false, length = 10)
    private String walletid;

    @Column(name = "balance")
    private Integer balance;

}