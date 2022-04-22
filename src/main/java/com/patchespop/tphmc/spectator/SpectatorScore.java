package com.patchespop.tphmc.spectator;

import org.bukkit.entity.Player;

public class SpectatorScore {

  private SpectateManager manager;
  private Player survivor;
  private int score;
  private int previousHealth;
  private int timeSinceDamage;
  private boolean inFight;
  private boolean isStarving;

  public SpectatorScore(SpectateManager manager, Player survivor) {
    this.survivor = survivor;
    this.manager = manager;
    timeSinceDamage = 0;
    score = 0;
    inFight = false;
    isStarving = false;
  }

  /**
   * Updates the players score by taking account of their situation, health, food, etc..
   */
  public void Update() {
    int deltaTime = manager.THREAD_SLEEP_TIME;
  }

  /**     GETTERS AND SETTERS!    **/
  public SpectateManager getManager() {
    return manager;
  }

  public void setManager(SpectateManager manager) {
    this.manager = manager;
  }

  public Player getSurvivor() {
    return survivor;
  }

  public void setSurvivor(Player survivor) {
    this.survivor = survivor;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getPreviousHealth() {
    return previousHealth;
  }

  public void setPreviousHealth(int previousHealth) {
    this.previousHealth = previousHealth;
  }

  public int getTimeSinceDamage() {
    return timeSinceDamage;
  }

  public void setTimeSinceDamage(int timeSinceDamage) {
    this.timeSinceDamage = timeSinceDamage;
  }

  public boolean isInFight() {
    return inFight;
  }

  public void setInFight(boolean inFight) {
    this.inFight = inFight;
  }

  public boolean isStarving() {
    return isStarving;
  }

  public void setStarving(boolean starving) {
    isStarving = starving;
  }
}
