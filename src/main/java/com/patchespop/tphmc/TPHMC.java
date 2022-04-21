package com.patchespop.tphmc;

import org.bukkit.plugin.java.JavaPlugin;

public final class TPHMC extends JavaPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic
    System.out.println("Hi!");

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    System.out.println("Bye!");
  }
}
