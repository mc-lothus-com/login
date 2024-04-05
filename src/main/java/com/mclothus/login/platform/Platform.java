package com.mclothus.login.platform;

import com.mclothus.login.data.DataAuth;
import com.mclothus.login.manager.GamerManager;
import lombok.Getter;
import lombok.Setter;

public class Platform {

    @Getter @Setter
    private static DataAuth dataAuth;

    @Getter
    private static GamerManager gamerManager = new GamerManager();
}
