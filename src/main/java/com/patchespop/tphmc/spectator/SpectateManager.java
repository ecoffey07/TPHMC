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
  private static ArrayList<SpectatorTarget> targetList;
  private static ScheduledExecutorService exec;
  private static SpectatorBot spectateBot;
  private static SpectatorTarget currentTarget;
  private static TPHMC tphmc = null;
  private static boolean debug;

  public SpectateManager(TPHMC tphmc) {
    spectateBot = null;
    currentTarget = null;
    SpectateManager.tphmc = tphmc;
    exec = Executors.newSingleThreadScheduledExecutor();
    targetList = new ArrayList<>();
    debug = false;
  }

  public void StartSpectate(Player player) {
    spectateBot.setBot(player);
    // Can't spectate yourself so remove the bot from targetList
    RemoveTarget(player);
    // spawn the thread that continuously queries the monitors
    System.out.println("Starting Spectating Thread");
    exec.scheduleAtFixedRate(SpectateManager::SpectateThread, 0, THREAD_SLEEP_TIME, TimeUnit.MILLISECONDS);
    // TODO initialize spectator, set spectate game mode and start spectating a player.
  }

  /**
   * Add spectate target to the scoreList, usually called when someone joins the game
   * @param target Player to be added to targetList
   */
  public synchronized void AddTarget(Player target) {
      targetList.add(new SpectatorTarget(this, target));
  }

  /**
   * Remove survivor from the scoreList, usually called when someone leaves the game
   * @param target player to be removed from targetList
   */
  public synchronized void RemoveTarget(Player target) {
    // Check if survivor exists in scoreList and then remove the object
    targetList.removeIf(spectatorTarget -> spectatorTarget.getTarget().getUniqueId().equals(target.getUniqueId()));
  }

  public void StopSpectate() {
    // TODO set spectateBot to survival gamemode and teleport their position to their last spectate target or 0,0 idk
    AddTarget(spectateBot.getBot());
    spectateBot = null;
    exec.shutdown();
  }

  /**
   * Every iteration, query the monitors and determine the best target to spectate
   */
  private static void SpectateThread() {

    try {
      System.out.println("Spectate Trigger: ");
      tphmc.getServer().getScheduler().scheduleSyncDelayedTask(tphmc, new Runnable() {
        @Override
        public void run() {
          spectateBot.getBot().chat("THREAD TEST!!!");
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Update the danger scores for every target
    // TODO spawn a thread for each person in the targetList
    for (SpectatorTarget target : targetList) {
      target.Update();
    }

    targetList.sort(new SpectatorTargetComparator()); // Sort by target score once all threads are finished
    

  }

  /**     GETTERS AND SETTERS!    **/
  public Player getSpectateBot() {
    return spectateBot.getBot();
  }

  public static void setDebug(boolean debug) {
    SpectateManager.debug = debug;
  }
}
