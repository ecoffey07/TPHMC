package com.patchespop.tphmc.listeners;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

  TPHMC tphmc;

  public PlayerListener(TPHMC tphmc) {
    this.tphmc = tphmc;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    // TODO maybe move the camera to hover over the body of the deceased?
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    tphmc.spectateManager.AddTarget(event.getPlayer());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    tphmc.spectateManager.RemoveTarget(event.getPlayer());
  }
}
