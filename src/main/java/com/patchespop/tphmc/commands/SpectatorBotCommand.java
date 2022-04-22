package com.patchespop.tphmc.commands;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorBotCommand implements CommandExecutor {

  private final TPHMC tphmc;

  public SpectatorBotCommand(TPHMC tphmc) {
    this.tphmc = tphmc;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    System.out.println("spectatorbot command received");
    if (sender instanceof Player player) {
      System.out.println("Testing for permissions");
      if (player.isOp() || player.hasPermission("spectatorbot")) {
        System.out.println("");
        // If spectateManager is already using sender as a bot, turn off spectate mode
        if (tphmc.spectateManager.getSpectateBot() != null && tphmc.spectateManager.getSpectateBot().getUniqueId().equals(player.getUniqueId())) {
          System.out.println("Bot is no longer spectating");
          tphmc.spectateManager.StopSpectate();
        }
        else {
          System.out.println("Bot is starting to spectate");
          tphmc.spectateManager.StartSpectate(player);
        }
        return true;
      }
    }
    return false;
  }
}
