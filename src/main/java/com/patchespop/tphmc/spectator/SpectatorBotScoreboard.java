package com.patchespop.tphmc.spectator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class SpectatorBotScoreboard {

  SpectatorBotScoreboard() {
  }

  public void openScoreboard() {
    Player bot = SpectateManager.getSpectateBot().getBot();
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    assert manager != null;
    Scoreboard scoreboard = manager.getNewScoreboard();

    Objective objective = scoreboard.registerNewObjective("Scores", "dummy", "Danger Scores");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    bot.setScoreboard(scoreboard);
  }

  public void removePlayerFromScoreboard(Player player) {
    Player bot = SpectateManager.getSpectateBot().getBot();
    if (bot != null) {
      Scoreboard scoreboard = bot.getScoreboard();
      scoreboard.resetScores("Scores");
    }
  }

  /**
   * Display current players score on the scoreboard for debuggin purposes
   */
  public void updateScoreboard() {
    Player bot = SpectateManager.getSpectateBot().getBot();
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
    Player bot = SpectateManager.getSpectateBot().getBot();
    bot.getScoreboard().resetScores("Scores");
    bot.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
  }
}
