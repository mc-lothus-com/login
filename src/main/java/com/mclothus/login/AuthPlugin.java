package com.mclothus.login;

import com.mclothus.login.commands.SetarCommand;
import com.mclothus.login.commands.login.LoginCommand;
import com.mclothus.login.commands.register.RegisterCommand;
import com.mclothus.login.data.impl.DataAuthImpl;
import com.mclothus.login.listener.GamerListeners;
import com.mclothus.login.listener.others.OthersListeners;
import com.mclothus.login.platform.Platform;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class AuthPlugin extends JavaPlugin implements PluginMessageListener {

    @Getter @Setter
    private static AuthPlugin instance;

    @Override
    public void onLoad() {
        setInstance(this);
        saveDefaultConfig();
        Platform.setDataAuth(new DataAuthImpl());
    }

    @Override
    public void onEnable() {
        getCommand("setar").setExecutor(new SetarCommand());
        getCommand("login").setExecutor(new LoginCommand());
        getCommand("register").setExecutor(new RegisterCommand());
        Bukkit.getPluginManager().registerEvents(new GamerListeners(), this);
        Bukkit.getPluginManager().registerEvents(new OthersListeners(), this);

        getInstance().getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getInstance().getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }
}
