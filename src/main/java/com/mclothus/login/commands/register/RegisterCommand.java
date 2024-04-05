package com.mclothus.login.commands.register;

import com.mclothus.login.data.info.AuthInfo;
import com.mclothus.login.encrypt.Serializer;
import com.mclothus.login.events.GamerLoggedEvent;
import com.mclothus.login.platform.Platform;
import com.mclothus.login.player.Gamer;
import com.mclothus.login.player.state.AuthState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String s2, String[] args) {
        if (!(s instanceof Player)) {
            return true;
        }

        Player player = (Player) s;
        Gamer gamer = Platform.getGamerManager().getGamer(player.getUniqueId());
        if (cmd.getLabel().equalsIgnoreCase("register")) {
            if (args.length == 0) {
                if (gamer.getState() != AuthState.REGISTER) {
                    player.sendMessage("§cVocê já está registrado, autentique-se utilizando '/logar [senha]'.");
                    return true;
                } else {
                    player.sendMessage("§cSintaxe incorreta, utilize '/registrar [senha]'.");
                    return true;
                }
            } else if (args.length > 0) {
                String password = args[0];
                if (gamer.getState() != AuthState.REGISTER) {
                    player.sendMessage("§cVocê já está registrado, autentique-se utilizando '/logar [senha]'.");
                    return true;
                } else if (password.length() > 16) {
                    player.sendMessage("§cA sua senha não pode conter mais de 16 caracteres.");
                    return true;
                } else if (password.length() < 8) {
                    player.sendMessage("§cA sua senha não pode conter menos de 8 caracteres.");
                    return true;
                } else if (password.contains("@") || password.contains("#") || password.contains("!") || password.contains(".") || password.contains("?")) {
                    player.sendMessage("§cVocê não pode utilizar caracteres especiais.");
                    return true;
                } else {
                    String passwordEncrypted = Serializer.encrypt(password, password);
                    AuthInfo authInfo = new AuthInfo(player.getUniqueId(), passwordEncrypted);
                    Platform.getDataAuth().create(authInfo);
                    gamer.setState(AuthState.LOGGED);
                    player.sendMessage("§aVocê registrou com sucesso!");
                    Bukkit.getPluginManager().callEvent(new GamerLoggedEvent(player, AuthState.LOGGED));
                    return true;
                }
            }
        }
        return false;
    }
}
