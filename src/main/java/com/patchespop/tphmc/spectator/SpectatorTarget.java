package com.patchespop.tphmc.spectator;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpectatorTarget {

  private SpectateManager manager;
  private Player target;
  private int score;
  private double previousHealth;
  private final int HEAT_COOLDOWN = 10; // Every 10 seconds, remove 1 heat??
  private final double MOB_DETECTION_RANGE = 20.0;
  private double heat; // 0-10 range for how much damage player has taken in a range of time
  private int cooldownCount;

  public SpectatorTarget(Player target) {
    this.target = target;
    score = 0;
    previousHealth = 10.00;
  }

  /**
   * Updates the players score by taking account of their situation, health, food, etc..
   */
  public void Update() {
    int deltaTime = SpectateManager.THREAD_SLEEP_TIME;
    double currentHealth = Math.round(target.getHealth() * 100.0) / 100.0;
    cooldownCount += deltaTime;

    // Reduce heat if applicable
    System.out.println("Calculating heat cooldown for " + target.getPlayerListName());
    if (cooldownCount >= HEAT_COOLDOWN) {
      if (heat - 1 < 0) {
        heat = 0;
      }
      else {
        heat -= 1;
      }
      cooldownCount = 0;
    }

    System.out.println("Heat check for " + target.getPlayerListName());
    // Check if damage has been taken
    if (currentHealth < previousHealth) {
      // Target has taken damage since last check
      double damage = previousHealth - currentHealth;
      double damagePercent = damage / previousHealth;
      heat += (int)Math.floor(damagePercent);
      cooldownCount = 0; // Reset cooldown
    }

    // Now do range detections on monsters
    System.out.println("Calculating mob ranges for " + target.getPlayerListName());
    Future<List<Entity>> listFuture = SpectateManager.getTPHMC().getServer().getScheduler().callSyncMethod(SpectateManager.getTPHMC(), new Callable<List<Entity>>() {
      @Override
      public List<Entity> call() throws Exception {
        return target.getNearbyEntities(MOB_DETECTION_RANGE, MOB_DETECTION_RANGE, MOB_DETECTION_RANGE);
      }
    });

    List<Entity> entityList = null;
    try {
      entityList = listFuture.get();
    } catch (Exception e) {
      System.out.println("Error getting Entity List for: " + target.getPlayerListName());
      e.printStackTrace();
    }

    double closestDistance = Double.POSITIVE_INFINITY;
    assert entityList != null;
    if (!entityList.isEmpty()) {
      Location targetCoords = target.getLocation();
      for (Entity monster: entityList) {
        if (monster instanceof  Monster) {
          Location monsterCoords = monster.getLocation();
          double distance = Math.sqrt(Math.pow(targetCoords.getX() - monsterCoords.getX(), 2) +
                  Math.pow(targetCoords.getY() - monsterCoords.getY(), 2) +
                  Math.pow(targetCoords.getZ() - monsterCoords.getZ(), 2));
          if (distance < closestDistance) {
            closestDistance = distance;
          }
        }
      }
    }

    int hunger = target.getFoodLevel();
    // We now have a heat level, distance to closest mob, current health, and hunger?
    // Current health affects the weighting of all the parameters
    System.out.println("Calculating total score for: " + target.getPlayerListName());
    score = 0;
    double weight = 1 - (Math.round((currentHealth / Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue()) * 100.0) / 100.0);
    double missingHealth = Math.round((Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue() - currentHealth) * 100.0) / 100.0;
    score += Math.round(missingHealth * weight);
    if (closestDistance < Double.POSITIVE_INFINITY) {
      score += Math.round( (MOB_DETECTION_RANGE - closestDistance) * weight);
    }
    score += Math.round(heat * weight);
    System.out.println("Score for: " + target.getPlayerListName() + " is: " + score);

    previousHealth = currentHealth;
  }

  /**     GETTERS AND SETTERS!    **/

  public Player getTarget() {
    return target;
  }

  public int getScore() {
    return score;
  }

  @Override
  public String toString() {
    return "Target: " + target.getPlayerListName() + "\nScore: " + score;
  }
}

class SpectatorTargetComparator implements Comparator<SpectatorTarget> {
  @Override
  public int compare(SpectatorTarget o1, SpectatorTarget o2) {
    return Integer.compare(o1.getScore(), o2.getScore());
  }
}
