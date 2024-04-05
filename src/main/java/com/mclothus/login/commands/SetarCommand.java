package com.mclothus.login.commands;

import com.lothus.core.Core;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.player.group.rank.Rank;
import com.mclothus.login.AuthPlugin;
import com.mclothus.login.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String s2, String[] args) {
        if (!(s instanceof Player)) {
            return true;
        }

        Player player = (Player) s;
        LothPlayer hyzePlayer = Core.getPlayerController().get(player.getUniqueId());
        if (cmd.getLabel().equalsIgnoreCase("setar")) {
            if (hyzePlayer.getGroup().getRank() != Rank.CEO) {
                if (!hyzePlayer.getGroup().containsPermission("command.login.setar")) {
                    player.sendMessage("§cVocê não tem permissão para executar este comando.");
                    return true;
                }
            }
            if (args.length == 0) {
                player.sendMessage("§cSintaxe incorreta, utilize '/setar [spawn]'.");
                return true;
            } else if (args.length > 0) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    File file = new File(AuthPlugin.getInstance().getDataFolder(), "config.yml");
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set("config.locations.spawn", Utils.serializeLocation(player.getLocation()));
                    try {
                        configuration.save(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    player.sendMessage("§aO spawn foi setado com sucesso.");
                    return true;
                }
            }
        }
        return false;
    }
}
