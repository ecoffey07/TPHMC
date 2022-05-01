package com.patchespop.tphmc.spectator;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Contains the thread method and manages the spectator bot,
 */
public class SpectateManager {

  private static SpectatorBotScoreboard botScoreboard;

  public final static int THREAD_SLEEP_TIME = 1000; // 1 sec for now
  public final static int THREAD_TELEPORT_TIME = 17; // roughly 60 times a second
  public final static int SPECTATOR_SCORE_COOLDOWN = 15000; // 15 sec for now
  public final static int SPECTATOR_RANDOM_COOLDOWN = 10000; // 10 sec for now
  private static ArrayList<SpectatorTarget> targetList;
  private static ScheduledExecutorService exec;
  private static SpectatorBot spectateBot;
  private static SpectatorTarget currentTarget;
  private static TPHMC tphmc = null;
  private static boolean debug;
  private static int timeSinceTargetSwap;

  public SpectateManager(TPHMC tphmc) {
    botScoreboard = new SpectatorBotScoreboard();
    spectateBot = null;
    currentTarget = null;
    SpectateManager.tphmc = tphmc;
    targetList = new ArrayList<>();
    debug = false;
    timeSinceTargetSwap = 0;
  }


  public void StartSpectate(Player player) {
    spectateBot = new SpectatorBot(player);
    // Can't spectate yourself so remove the bot from targetList
    RemoveTarget(player);
    System.out.println("Opening scoreboard");
    botScoreboard.openScoreboard();
    // spawn the thread that continuously queries the monitors
    System.out.println("Starting Spectating Thread");
    exec = Executors.newSingleThreadScheduledExecutor();
    timeSinceTargetSwap = SPECTATOR_RANDOM_COOLDOWN;
    exec.scheduleAtFixedRate(SpectateManager::SpectateThread, 0, THREAD_SLEEP_TIME, TimeUnit.MILLISECONDS);
    exec.scheduleAtFixedRate(SpectateManager::TeleportThread, 0, THREAD_TELEPORT_TIME, TimeUnit.MILLISECONDS);
    // TODO initialize spectator, set spectate game mode and start spectating a player.
    /**
     * For spectator mode:
     * Make the spectator bot invisible and invincible
     * Somehow make the spectator bot follow the target
     * copy the target's hotbar, health, armor, maybe even air bubbles to the spectator bot's inventory
     * copy the target's skin
     * read the players events and mimic them, i.e. play the attack animation when they attackS
     */
    spectateBot.Init();
  }

  public void StopSpectate() {
    System.out.println("Stop spectating");
    // TODO set spectateBot to survival gamemode and teleport their position to their last spectate target or 0,0 idk
    AddTarget(spectateBot.getBot());
    botScoreboard.closeScoreboard();
    spectateBot.stopSpectating();
    spectateBot = null;
    currentTarget = null;
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
    targetList.add(new SpectatorTarget(target));
  }

  /**
   * Remove survivor from the scoreList, usually called when someone leaves the game
   * @param target player to be removed from targetList
   */
  public synchronized void RemoveTarget(Player target) {
    // Remove target only when someone becomes the bot or someone leaves
    // No matter what, remove the player from the targetlist
    System.out.println("Removing " + target.getPlayerListName() + " from targetlist");

    // Target left the game
    if (!target.isOnline()) {
      if (spectateBot.getBot().getUniqueId().equals(target.getUniqueId())) {
        // The bot left, but this shouldn't happen normally
        StopSpectate();
        spectateBot = null;
      }
      else {
        // A regular target left the game
        targetList.removeIf(spectatorTarget -> spectatorTarget.getTarget().getUniqueId().equals(target.getUniqueId()));
        botScoreboard.removePlayerFromScoreboard(target);
      }
    }
    else { // target is still online
      // the person became a bot
      targetList.removeIf(spectatorTarget -> spectatorTarget.getTarget().getUniqueId().equals(target.getUniqueId()));
      botScoreboard.removePlayerFromScoreboard(target);
    }
  }

  private static void TeleportThread() {
    if (currentTarget != null && spectateBot != null) {
      //spectateBot.setTarget(currentTarget.getTarget());
      spectateBot.updatePosition();
      spectateBot.updateHotbar();
      spectateBot.updateHealth();
    }
  }

  /**
   * Every iteration, query the monitors and determine the best target to spectate
   */
  private static void SpectateThread() {
    // Update the danger scores for every target
    // TODO spawn a thread for each person in the targetList
    if (targetList.size() <= 0) {
      System.out.println("No spectators in list, returning");
      return;
    }
//    System.out.println("Updating target values");
    for (SpectatorTarget target : targetList) {
      target.Update();
    }

    targetList.sort(new SpectatorTargetComparator()); // Sort by target score once all threads are finished
//    System.out.println("Updating the scoreboard");
    botScoreboard.updateScoreboard(); // Update scoreboard to new scores

    UpdateSpectateTarget();
  }

  private static synchronized void UpdateSpectateTarget() {
    timeSinceTargetSwap += THREAD_SLEEP_TIME;

    int highestScore = targetList.get(0).getScore(); // List is sorted
    // IT WORKS!
    if (highestScore < 10) {
      // Spectate random players, swapping every few seconds
      if (timeSinceTargetSwap >= SPECTATOR_RANDOM_COOLDOWN) {
        int index = new Random().nextInt(targetList.size());
        currentTarget = targetList.get(index);
        spectateBot.setTarget(currentTarget.getTarget());
        timeSinceTargetSwap = 0;
      }
    }
    else if (highestScore > 25) {
      // Immediately spectate this player
      currentTarget = targetList.get(0);
      spectateBot.setTarget(currentTarget.getTarget());
      timeSinceTargetSwap = 0;
    }
    else {
      // Spectate the player with the highest score, but don't immediately swap if someone gets above the score
      if (timeSinceTargetSwap >= SPECTATOR_SCORE_COOLDOWN) {
        currentTarget = targetList.get(0);
        spectateBot.setTarget(currentTarget.getTarget());
        timeSinceTargetSwap = 0;
      }
    }
  }

  /**     GETTERS AND SETTERS!    **/
  public static SpectatorBot getSpectateBot() {
    return spectateBot;
  }

  public static SpectatorTarget getCurrentTarget() {
    return currentTarget;
  }

  public static void setDebug(boolean debug) {
    SpectateManager.debug = debug;
  }

  public static ArrayList<SpectatorTarget> getTargetList() {
    return targetList;
  }

  public static TPHMC getTPHMC() {
    return tphmc;
  }
}
