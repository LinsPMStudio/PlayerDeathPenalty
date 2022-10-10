package me.mmmjjkx.playerdeathpenalty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThingsRunner {
    public static void runCommand(String key, Player p){
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = PlayerDeathPenalty.config;
                List<String> commands = config.getStringList(key + ".commands");
                for(String command:commands) {
                    String[] cmd = command.split(":");
                    if (cmd.length != 2) {
                        continue;
                    }
                    String asWhat = cmd[0];
                    String runCommand = cmd[1].replaceAll("%player%", p.getName());
                    switch (asWhat) {
                        case "asPlayer":
                            p.performCommand(runCommand);
                            break;
                        case "asOp":
                            p.setOp(true);
                            p.performCommand(runCommand);
                            p.setOp(false);
                            break;
                        case "asConsole":
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), runCommand);
                            break;
                    }
                }
            }
        }.run();
    }
    public static void playSound(String key,Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = PlayerDeathPenalty.config;
                List<String> sounds = config.getStringList(key + ".sounds");
                for (String sound : sounds) {
                    String[] s = sound.split(":");
                    if (s.length != 3) {
                        continue;
                    }
                    Sound sou = Sound.valueOf(s[0]);
                    float volume = Float.parseFloat(s[1]);
                    float pitch = Float.parseFloat(s[2]);
                    p.playSound(p.getLocation(), sou, volume, pitch);
                }
            }
        }.run();
    }
    public static String colorizeString(String string) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String color = string.substring(matcher.start(), matcher.end());
            string = string.replace(color, net.md_5.bungee.api.ChatColor.of(color) + ""); // You're missing this replacing
        }
        string = ChatColor.translateAlternateColorCodes('&', string); // Translates any & codes too
        return string;
    }
}
