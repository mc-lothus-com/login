package com.mclothus.login.player;

import com.mclothus.login.player.state.AuthState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter @Setter
public class Gamer {

    private Player player;
    private AuthState state;

    public Gamer(Player player) {
        this.player = player;
    }
}
