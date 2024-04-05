package com.mclothus.login.listener;

import com.lothus.core.Core;
import com.lothus.core.event.update.UpdateEvent;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.servers.ServerInfo;
import com.lothus.core.servers.type.ServerType;
import com.lothus.core.utils.bukkit.player.PlayerUtil;
import com.mclothus.login.AuthPlugin;
import com.mclothus.login.events.GamerLoggedEvent;
import com.mclothus.login.platform.Platform;
import com.mclothus.login.player.Gamer;
import com.mclothus.login.player.state.AuthState;
import com.mclothus.login.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.github.paperspigot.Title;

import java.util.Comparator;
import java.util.List;

public class GamerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        LothPlayer o = Core.getPlayerController().get(player.getUniqueId());

        if (o.isPremium()) {
            player.kickPlayer("§cNão foi possível carregar sua conta.");
            return;
        }

        Gamer gamer = new Gamer(player);
        if (Platform.getDataAuth().exists(player.getUniqueId())) {
            gamer.setState(AuthState.LOGIN);
            player.sendMessage("§eUtilize /logar [senha] para autenticar-se.");
            player.sendTitle(new Title("§6§lLOTHUS", "§fUtilize '/logar [senha]' para autenticar-se.", 1,99999,1));
        } else {
            gamer.setState(AuthState.REGISTER);
            player.sendMessage("§eUtilize /registrar [senha] para autenticar-se.");
            player.sendTitle(new Title("§6§lLOTHUS", "§fUtilize '/registrar [senha]' para autenticar-se.", 1,99999,1));
        }
        player.teleport(Utils.deserializeLocation(AuthPlugin.getInstance().getConfig().getString("config.locations.spawn")));
        player.setGameMode(GameMode.ADVENTURE);
        Platform.getGamerManager().load(gamer);
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        Player player = event.getPlayer();

        Gamer g = Platform.getGamerManager().getGamer(player.getUniqueId());

        if (g.getState() != AuthState.LOGGED)return;
        ServerInfo serverInfo = getServerInfo(ServerType.LOBBY);

        if (serverInfo == null) {
            player.kickPlayer("§cNossos servidores estão indisponíveis no momento.");
            return;
        }

        PlayerUtil.connect(player.getUniqueId(), serverInfo);
    }

    @EventHandler
    public void onGamerLogged(GamerLoggedEvent e) {
        Player player = e.getPlayer();
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
        player.sendTitle(new Title("§6§lLOTHUS", "§eVocê se autenticou com sucesso!", 1, 20, 1));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Platform.getGamerManager().unload(player.getUniqueId());
        e.setQuitMessage(null);
    }


    private ServerInfo getServerInfo(ServerType type) {
        Comparator<ServerInfo> comparator = Comparator.comparing(ServerInfo::getPlayers);
        List<ServerInfo> list = Core.getServerController().get(type);
        list.sort(comparator);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
}
