package com.patchespop.tphmc.gamemode;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.entity.Player;

/**
 * Class to handle the leftover lives, betting, triggers bash script to reset server
 */
public class GameManager {

  TPHMC tphmc;
  private int respawnsLeft; // Read in from xml file possibly?

  public GameManager(TPHMC tphmc) {
    this.tphmc = tphmc;
  }

  /**
   * Trigger the death procedurs
   * @param player the person who died
   */
  public void TriggerDeath(Player player) {

  }


}
