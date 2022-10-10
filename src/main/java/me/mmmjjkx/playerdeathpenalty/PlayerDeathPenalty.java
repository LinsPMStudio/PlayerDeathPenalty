package me.mmmjjkx.playerdeathpenalty;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class PlayerDeathPenalty extends JavaPlugin {
    public static PlayerPointsAPI PlayerPointsAPI;
    public static PlayerDeathPenalty instance;
    public static FileConfiguration config;
    public static Economy econ = null;
    public static Permission perms = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        instance = this;
        config = getConfig();
        setupEconomy();
        setupPermissions();
        setupOtherPlugins();
        Objects.requireNonNull(getCommand("playerdeathpenalty")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new Listen(),this);
        getLogger().info(ThingsRunner.colorizeString("&b&lPlayerDeathPenalty2 —— by mmmjjkx"));
        if(hasEconomy()){
            getLogger().info(ThingsRunner.colorizeString("&a&l已找到Vault与其相关联的插件，启动金币惩罚！"));
        }else {
            getLogger().info(ThingsRunner.colorizeString("&4&l未找到Vault与其相关联的插件，关闭金币惩罚！"));
        }
        if(hasPlayerPoints()){
            getLogger().info(ThingsRunner.colorizeString("&a&l已找到PlayerPoints插件，启动点券惩罚！"));
        }else {
            getLogger().info(ThingsRunner.colorizeString("&4&l未找到PlayerPoints插件，关闭点券惩罚！"));
        }
        getLogger().info(ThingsRunner.colorizeString("&a&l已启用！"));
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDefaultConfig();
        getLogger().info(ThingsRunner.colorizeString("&b&lPlayerDeathPenalty2 —— by mmmjjkx"));
        getLogger().info(ThingsRunner.colorizeString("&a&l已卸载！"));
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
    public boolean onCommand(CommandSender sender, Command command,String label,String[] args) {
        if(command.getName().equals("playerdeathpenalty")){
            if(sender.hasPermission("playerdeathpenalty.admin")) {
                reloadConfig();
                setupEconomy();
                setupPermissions();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&a&l已重载配置！"));
            }
        }
        return false;
    }
    private static void setupPermissions() {
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Permission> provider = Bukkit.getServicesManager().getRegistration(Permission.class);
            if (provider != null) {
                perms = provider.getProvider();
            }
        }
    }
    public static List<Integer> getSlots(int size){
        List<Integer> l = new LinkedList<>();
        for(int i=0;i<size;i++){
           l.add(i,new Random().nextInt(35));
        }
        return l;
    }
}