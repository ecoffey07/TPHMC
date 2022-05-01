package com.patchespop.tphmc.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpectatorBot {

  private Player bot;
  private Player target;

  public SpectatorBot(Player bot) {
    this.bot = bot;
  }

  /**
   * Make bot invisible to all
   * make target invisible to bot
   * make bot invincible
   * make bot fly?
   */
  public void Init() {
    for (Player players : SpectateManager.getTPHMC().getServer().getOnlinePlayers()) {
      players.hidePlayer(SpectateManager.getTPHMC(), bot);
    }
//    if (SpectateManager.getTPHMC().getServer().getAllowFlight()) {
//      bot.setFlying(true);
//    }
  }

  public void updatePosition() {
    SpectateManager.getTPHMC().getServer().getScheduler().scheduleSyncDelayedTask(SpectateManager.getTPHMC(), new Runnable() {
      @Override
      public void run() {
        // TODO figure out smooth movement
        // Use vector manipulations instead of teleporting, maybe if something happens and the distance is too great, then resync with teleport
//        Vector targetVector = target.getLocation().toVector();
//        Vector botVector = target.getLocation().toVector();
//        Vector velocity = botVector.subtract(targetVector);
//        // bot.setVelocity(velocity.normalize().multiply(0.07));
//        bot.setVelocity(targetVector);
        bot.teleport(target.getLocation());
      }
    });
    //bot.teleport(target.getLocation()); // Probably need to add to server queue
  }

  public void updateHotbar() {
    bot.getInventory().setContents(target.getInventory().getContents());
    bot.getInventory().setArmorContents(target.getInventory().getArmorContents());
    bot.getInventory().setHeldItemSlot(target.getInventory().getHeldItemSlot());
  }

  public void updateHealth() {
    bot.setHealth(target.getHealth());
    bot.setSaturation(target.getSaturation());
  }

  /**
   * Make bot visible to all
   * make target visible to bot
   * make bot not invincible and clear inventory/hotbar
   */
  public void stopSpectating() {
//    bot.setFlying(false);
    SpectateManager.getTPHMC().getServer().getScheduler().scheduleSyncDelayedTask(SpectateManager.getTPHMC(), new Runnable() {
      @Override
      public void run() {
        bot.showPlayer(SpectateManager.getTPHMC(), target);
        for (Player players : SpectateManager.getTPHMC().getServer().getOnlinePlayers()) {
          players.showPlayer(SpectateManager.getTPHMC(), bot);
        }
      }
    });
  }

  // GETTERS AND SETTERS

  public Player getBot() {
    return bot;
  }

  public void setBot(Player bot) {
    this.bot = bot;
  }

  public Player getTarget() {
    return target;
  }

  public void setTarget(Player target) {

    Player previousTarget = this.target;
    this.target = target;
    SpectateManager.getTPHMC().getServer().getScheduler().scheduleSyncDelayedTask(SpectateManager.getTPHMC(), new Runnable() {
      @Override
      public void run() {
        if (previousTarget != null) {
          bot.showPlayer(SpectateManager.getTPHMC(), previousTarget);
        }
        bot.hidePlayer(SpectateManager.getTPHMC(), target);
        bot.teleport(target.getLocation());

          // TODO figure out skin swapping, low priority
//        CraftPlayer botPlayer = (CraftPlayer)target;
//        GameProfile targetProfile = botPlayer.getHandle().getGameProfile();
//        GameProfile botProfile = ((CraftPlayer)bot).getHandle().getGameProfile();
//        botProfile.getProperties().removeAll("textures");
//        botProfile.getProperties().putAll("textures", targetProfile.getProperties().get("textures"));
//        ServerPlayer entityPlayer = botPlayer.getHandle();
//        entityPlayer.connection.send(new PacketPlayOutEntityDestroy(botPlayer.getEntityId()));
//        new PacketPlay

//        sendPackets(new PacketPlayOutEntityDestroy(botPlayer.getEntityId()));
//        sendPackets(new PacketPlayOutPlayerInfo(
//                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, botPlayer.getHandle()));
      }
    });
  }
}
