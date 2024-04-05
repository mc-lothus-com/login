package com.mclothus.login.data.info;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class AuthInfo {

    private UUID uniqueId;
    private String password;

    public AuthInfo(UUID uniqueId, String password) {
        this.uniqueId = uniqueId;
        this.password = password;
    }
}
