package com.patchespop.tphmc.spectator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class SpectatorBotScoreboard {

  SpectatorBotScoreboard() {
  }

  public void openScoreboard() {
    Player bot = SpectateManager.getSpectateBot();
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    assert manager != null;
    Scoreboard scoreboard = manager.getNewScoreboard();

    Objective objective = scoreboard.registerNewObjective("Scores", "dummy", "Danger Scores");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    bot.setScoreboard(scoreboard);
  }

  /**
   * Display current players score on the scoreboard for debuggin purposes
   */
  public void updateScoreboard() {
    Player bot = SpectateManager.getSpectateBot();
    Scoreboard scoreboard = bot.getScoreboard();
    ArrayList<SpectatorTarget> targetList = SpectateManager.getTargetList();
    Objective objective = scoreboard.getObjective("Scores");
    for (SpectatorTarget target: targetList) {
      assert objective != null;
      Score score = objective.getScore(target.getTarget().getPlayerListName());
      score.setScore(target.getScore());
    }
  }

  public void closeScoreboard() {
    Player bot = SpectateManager.getSpectateBot();
    bot.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
  }
}
