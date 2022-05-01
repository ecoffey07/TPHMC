package com.patchespop.tphmc.listeners;

import com.patchespop.tphmc.TPHMC;
import com.patchespop.tphmc.spectator.SpectateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Watch for damage, moving, jumping, attacking, interacting, mining
 */
public class TargetListener implements Listener {

  TPHMC tphmc;

  public TargetListener(TPHMC tphmc) {
    this.tphmc = tphmc;
  }

  @EventHandler
  public void onTargetDamage(EntityDamageEvent event) {
    if (SpectateManager.getCurrentTarget() != null) {
      Player target = SpectateManager.getCurrentTarget().getTarget();
      if (target != null && event.getEntity() instanceof Player eventPlayer) {
        if (target.getUniqueId().equals(eventPlayer.getUniqueId())) {
          //SpectateManager.getSpectateBot().updateHealth(); // Update spectatorbot's hp
          //System.out.println("Target took damage");
        }
      }
    }
  }

  @EventHandler
  public void onTargetMove(PlayerMoveEvent event) {
    if (SpectateManager.getCurrentTarget() != null) {
      Player target = SpectateManager.getCurrentTarget().getTarget();
      if (event.getPlayer().getUniqueId().equals(target.getUniqueId())) { // If the target moves
        // Set the position of the spectator bot to the position of the target | might need to update this to a constant loop
        //SpectateManager.getSpectateBot().updatePosition();
        //System.out.println("Target moved");
      }
    }
  }
}
