package com.patchespop.tphmc.spectator;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class SpectatorBotScoreboard {

  SpectatorBotScoreboard() {
  }

  public void openScoreboard() {
    SpectatorBot bot = SpectateManager.getSpectateBot();
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    assert manager != null;
    Scoreboard scoreboard = manager.getNewScoreboard();

    Objective objective = scoreboard.registerNewObjective("Scores", "dummy", "Danger Scores");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    bot.getPlayer().setScoreboard(scoreboard);
  }

  /**
   * Display current players score on the scoreboard for debuggin purposes
   */
  public void updateScoreboard() {
    SpectatorBot bot = SpectateManager.getSpectateBot();
    Scoreboard scoreboard = bot.getPlayer().getScoreboard();
    ArrayList<SpectatorTarget> targetList = SpectateManager.getTargetList();
    Objective objective = scoreboard.getObjective("Scores");
    for (SpectatorTarget target: targetList) {
      assert objective != null;
      Score score = objective.getScore(target.getTarget().getPlayerListName());
      score.setScore(target.getScore());
    }
  }

  public void closeScoreboard() {
    SpectatorBot bot = SpectateManager.getSpectateBot();
    bot.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
  }
}
