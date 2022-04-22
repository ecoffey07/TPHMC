package com.patchespop.tphmc.spectator;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Contains the thread method and manages the spectator bot,
 */
public class SpectateManager {

  public final int THREAD_SLEEP_TIME = 1000; // 1 sec for now
  private static ArrayList<SpectatorScore> scoreList;
  private static ScheduledExecutorService exec;
  private static Player spectateBot;
  private static Player spectateTarget;
  private static TPHMC tphmc = null;

  public SpectateManager(TPHMC tphmc) {
    SpectateManager.tphmc = tphmc;
    exec = Executors.newSingleThreadScheduledExecutor();
    scoreList = new ArrayList<>();
  }

  public void StartSpectate(Player player) {
    spectateBot = player;
    // spawn the thread that continuously queries the monitors
    System.out.println("Starting Spectating Thread");
    exec.scheduleAtFixedRate(SpectateManager::SpectateThread, 0, THREAD_SLEEP_TIME, TimeUnit.MILLISECONDS);
    // TODO initialize spectator, set spectate game mode and start spectating a player.
  }

  /**
   * Add survivor to the scoreList, usually called when someone joins the game
   * @param survivor Player to be added
   */
  public synchronized void AddSurvivor(Player survivor) {
      scoreList.add(new SpectatorScore(this, survivor));
  }

  /**
   * Remove survivor from the scoreList, usually called when someone leaves the game
   * @param survivor player to be removed
   */
  public synchronized void RemoveSurvivor(Player survivor) {
    // Check if survivor exists in scoreList and then remove the object
    scoreList.removeIf(spectatorScore -> spectatorScore.getSurvivor().getUniqueId().equals(survivor.getUniqueId()));
  }

  public void StopSpectate() {
    // TODO set spectateBot to survival gamemode and teleport their position to their last spectate target or 0,0 idk
    spectateBot = null;
    exec.shutdown();
  }

  /**
   * Every iteration, query the monitors and determine the best target to spectate
   */
  private static void SpectateThread() {
    try {
      System.out.println("Spectate Trigger: " + spectateBot.getDisplayName());
      tphmc.getServer().getScheduler().scheduleSyncDelayedTask(tphmc, new Runnable() {
        @Override
        public void run() {
          spectateBot.chat("THREAD TEST!!!");
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**     GETTERS AND SETTERS!    **/

  public Player getSpectateBot() {
    return spectateBot;
  }
}
