package com.patchespop.tphmc.spectator;

import org.bukkit.entity.Player;

public class SpectatorBot {

  Player bot;
  SpectateManager manager;
  SpectatorTarget currentTarget;

  public SpectatorBot(SpectateManager manager, Player bot) {
    this.bot = bot;
    this.manager = manager;
  }

  public void moveTarget(SpectatorTarget target) {

  }

  public Player getPlayer() {
    return bot;
  }

  public void setBot(Player bot) {
    this.bot = bot;
  }

  public SpectatorTarget getCurrentTarget() {
    return currentTarget;
  }

  public void setCurrentTarget(SpectatorTarget currentTarget) {
    this.currentTarget = currentTarget;
  }
}
