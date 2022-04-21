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

    if (sender instanceof Player) {
      Player player = (Player) sender;

      if (player.isOp() || player.hasPermission("spectatorbot")) {

      }
    }
    return false;
  }
}
