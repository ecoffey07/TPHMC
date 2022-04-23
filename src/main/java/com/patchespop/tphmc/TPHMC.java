package com.patchespop.tphmc;

import com.patchespop.tphmc.commands.SpectatorBotCommand;
import com.patchespop.tphmc.listeners.PlayerListener;
import com.patchespop.tphmc.spectator.SpectateManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TPHMC extends JavaPlugin {

  public SpectateManager spectateManager;

  @Override
  public void onEnable() {
    // Plugin startup logic
    System.out.println("Hi!");

    // Enable commands
    Objects.requireNonNull(getCommand("autospectate")).setExecutor(new SpectatorBotCommand(this));

    // Enable Listeners

    getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    spectateManager = new SpectateManager(this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    System.out.println("Bye!");
  }
}
