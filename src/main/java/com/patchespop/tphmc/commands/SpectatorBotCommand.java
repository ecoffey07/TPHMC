package com.patchespop.tphmc.commands;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
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
      if (player.isOp() || player.hasPermission("spectatorbot")) {
        boolean notNull = tphmc.spectateManager.getSpectateBot() != null;
        if (notNull) {
          boolean isSamePlayer = tphmc.spectateManager.getSpectateBot().getUniqueId().equals(player.getUniqueId());
          if (isSamePlayer) {
            player.sendMessage("You are no longer the spectate bot");
            tphmc.spectateManager.StopSpectate();
          }
          else {
            player.sendMessage("There is already a spectator bot activated");
          }
        }
        else {
          player.sendMessage("You are now the spectate bot");
          tphmc.spectateManager.StartSpectate(player);
        }
        return true;
      }
    }
    return false;
  }
}
