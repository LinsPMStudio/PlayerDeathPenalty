package me.mmmjjkx.playerdeathpenalty;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PlayerDeathPenalty extends JavaPlugin {
    public static PlayerPointsAPI PlayerPointsAPI;
    public static PlayerDeathPenalty instance;
    public static FileConfiguration config;
    public static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;
        config = getConfig();
        setupEconomy();
        setupOtherPlugins();
        Objects.requireNonNull(getCommand("playerdeathpenalty")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new Listen(), this);
        Metrics m = new Metrics(this, 17098);
        log("&b&lPlayerDeathPenalty2 —— by mmmjjkx");
        if (hasEconomy()) {
            log("&a&l已找到Vault与其相关联的插件，启动金币惩罚！");
        } else {
            log("&4&l未找到Vault与其相关联的插件，关闭金币惩罚！");
        }
        if (hasPlayerPoints()) {
            log("&a&l已找到PlayerPoints插件，启动点券惩罚！");
        } else {
            log("&4&l未找到PlayerPoints插件，关闭点券惩罚！");
        }
        log("&a&l已启用！");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDefaultConfig();
        log("&b&lPlayerDeathPenalty2 —— by mmmjjkx");
        log("&a&l已卸载！");
    }
    private static void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }
    public static boolean hasEconomy(){
        return econ != null;
    }
    private static void setupOtherPlugins(){
        //PlayerPoints
        if(Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")){
            PlayerPointsAPI = PlayerPoints.getInstance().getAPI();
        }
    }
    public static boolean hasPlayerPoints(){
        return PlayerPointsAPI != null;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if(command.getName().equals("playerdeathpenalty")){
            if(sender.hasPermission("playerdeathpenalty.admin")) {
                reloadConfig();
                setupEconomy();
                ThingsRunner.sendMessage(sender, getConfig().getString("message.reloadSuccess"));
            }
        }
        return false;
    }
    private void log(String info){
        if (ThingsRunner.classExists("net.kyori.adventure.text.Component")){
            getComponentLogger().info(LegacyComponentSerializer.legacyAmpersand().deserializeOr(info, Component.newline()));
        }else {
            getLogger().info(ThingsRunner.colorizeString(info));
        }
    }
}