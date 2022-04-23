package com.patchespop.tphmc;

import com.patchespop.tphmc.commands.SpectatorBotCommand;
import com.patchespop.tphmc.gamemode.GameManager;
import com.patchespop.tphmc.listeners.PlayerListener;
import com.patchespop.tphmc.spectator.SpectateManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TPHMC extends JavaPlugin {

  public SpectateManager spectateManager;
  public GameManager gameManager;

  @Override
  public void onEnable() {
    // Plugin startup logic
    System.out.println("Hi!");

    // Enable commands
    Objects.requireNonNull(getCommand("autospectate")).setExecutor(new SpectatorBotCommand(this));

    // Enable Listeners
    getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    // Set managers
    spectateManager = new SpectateManager(this);
    gameManager = new GameManager(this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    System.out.println("Bye!");
  }
}
