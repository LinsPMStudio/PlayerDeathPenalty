package me.mmmjjkx.playerdeathpenalty.data;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.mmmjjkx.playerdeathpenalty.PlayerDeathPenalty;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static me.mmmjjkx.playerdeathpenalty.PlayerDeathPenalty.config;

public class PermManager {
   public static int getMoneyGroup(Player p){
       String[] reutrnback = hasOnePermission(p, "exp");
       if (Boolean.parseBoolean(reutrnback[0])){
           return config.getInt("money.perm."+reutrnback[1]);
       }
       return 0;
   }
    public static int getItemGroup(Player p){
        String[] reutrnback = hasOnePermission(p, "exp");
        if (Boolean.parseBoolean(reutrnback[0])){
            return config.getInt("item.perm."+reutrnback[1]);
        }
        return 0;
    }
    public static int getExpGroup(Player p){
        String[] reutrnback = hasOnePermission(p, "exp");
        if (Boolean.parseBoolean(reutrnback[0])){
            return config.getInt("exp.perm."+reutrnback[1]);
        }
        return 0;
    }
    public static int getPointsGroup(Player p){
        String[] reutrnback = hasOnePermission(p, "exp");
        if (Boolean.parseBoolean(reutrnback[0])) {
            return config.getInt("point.perm." + reutrnback[1]);
        }
        return 0;
    }
    public static void TakeMoney(Player p, double amount) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
       if(Bukkit.getPluginManager().isPluginEnabled("Essentials")){
           com.earth2me.essentials.api.Economy.subtract(p.getUniqueId(),new BigDecimal(amount));
       } else if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
           com.Zrips.CMI.Modules.Economy.Economy.withdrawPlayer(p,amount);
       } else{
           PlayerDeathPenalty.econ.withdrawPlayer(p,amount);
       }
    }

    public static String[] hasOnePermission(Player p,String key){
       String[] objects = new String[2];
       ConfigurationSection cs = config.getConfigurationSection(key+".perm");
       if (cs==null){
           config.createSection(key+".perm");
           objects[0] = String.valueOf(false);
           return objects;
       }
       for (String perm:cs.getKeys(true)) {
           if (p.hasPermission(perm)) {
                objects[0] = String.valueOf(true);
                objects[1] = perm;
           }
       }
       return objects;
    }
}
