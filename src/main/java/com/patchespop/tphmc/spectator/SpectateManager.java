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

  private static SpectatorBotScoreboard botScoreboard;

  public final int THREAD_SLEEP_TIME = 1000; // 1 sec for now
  private static ArrayList<SpectatorTarget> targetList;
  private static ScheduledExecutorService exec;
  private static SpectatorBot spectateBot;
  private static SpectatorTarget currentTarget;
  private static TPHMC tphmc = null;
  private static boolean debug;

  public SpectateManager(TPHMC tphmc) {
    botScoreboard = new SpectatorBotScoreboard();
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
    botScoreboard.openScoreboard();
    // spawn the thread that continuously queries the monitors
    System.out.println("Starting Spectating Thread");
    exec.scheduleAtFixedRate(SpectateManager::SpectateThread, 0, THREAD_SLEEP_TIME, TimeUnit.MILLISECONDS);
    // TODO initialize spectator, set spectate game mode and start spectating a player.
  }

  public void StopSpectate() {
    System.out.println("Stop spectating");
    // TODO set spectateBot to survival gamemode and teleport their position to their last spectate target or 0,0 idk
    AddTarget(spectateBot.getPlayer());
    botScoreboard.closeScoreboard();
    spectateBot = null;
    exec.shutdown();
  }

  /**
   * Add spectate target to the scoreList, usually called when someone joins the game
   * @param target Player to be added to targetList
   */
  public synchronized void AddTarget(Player target) {
    // New target should always be unique
    for (SpectatorTarget targets: targetList) {
      if (targets.getTarget().getUniqueId().equals(target.getUniqueId())) {
        System.out.println("Error placing target in targetlist, duplicate detected");
        return;
      }
    }
    System.out.println("Added " + target.getPlayerListName() + " to target list");
    targetList.add(new SpectatorTarget(this, target));
  }

  /**
   * Remove survivor from the scoreList, usually called when someone leaves the game
   * @param target player to be removed from targetList
   */
  public synchronized void RemoveTarget(Player target) {
    // Check if survivor exists in scoreList and then remove the object
    System.out.println("Removing " + target.getPlayerListName() + " from targetlist");
    targetList.removeIf(spectatorTarget -> spectatorTarget.getTarget().getUniqueId().equals(target.getUniqueId()));
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
          spectateBot.getPlayer().chat("THREAD TEST!!!");
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Update the danger scores for every target
    // TODO spawn a thread for each person in the targetList
    if (targetList.size() <= 0) {
      System.out.println("No spectators in list, returning");
      return;
    }

    for (SpectatorTarget target : targetList) {
      target.Update();
    }

    targetList.sort(new SpectatorTargetComparator()); // Sort by target score once all threads are finished
    botScoreboard.updateScoreboard(); // Update scoreboard to new scores

    // If the highest score is greater than some threshold, force spectate that player,
    // If the highest score is less than some threshold, spectate random players, swapping every few seconds??
    int highestScore = targetList.get(0).getScore();

  }

  /**     GETTERS AND SETTERS!    **/
  public static SpectatorBot getSpectateBot() {
    return spectateBot;
  }

  public static void setDebug(boolean debug) {
    SpectateManager.debug = debug;
  }

  public static ArrayList<SpectatorTarget> getTargetList() {
    return targetList;
  }


}
