package com.patchespop.tphmc.spectator;

import com.patchespop.tphmc.TPHMC;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Contains the thread method and manages the spectator bot,
 */
public class SpectateManager {

  private final int THREAD_SLEEP_TIME = 1000; // 1 sec for now
  private ScheduledExecutorService exec;
  private Player spectateBot;
  private final TPHMC tphmc;
  private boolean spectateBotSet;

  public SpectateManager(TPHMC tphmc) {
    spectateBotSet = false;
    this.tphmc = tphmc;
    exec = Executors.newSingleThreadScheduledExecutor();
  }

  public void StartSpectate(Player player) {
    spectateBot = player;
    spectateBotSet = true;
    // spawn the thread that continuously queries the monitors
    exec.scheduleAtFixedRate(SpectateManager::SpectateThread, 0, THREAD_SLEEP_TIME, TimeUnit.MILLISECONDS);

  }

  public void StopSpectate(Player player) {
    exec.shutdown();
  }

  private static void SpectateThread() {

  }
}
