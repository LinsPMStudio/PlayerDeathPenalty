package me.mmmjjkx.playerdeathpenalty;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.MaxMoneyException;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.milkbowl.vault.permission.Permission;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.mmmjjkx.playerdeathpenalty.PlayerDeathPenalty.config;

public class PermManager {
   private static GroupManager gm;
   private static LuckPerms lp;
   public static int getMoneyGroup(Player p){
       int i = 0;
       if(UseLuckPerms()) {
           for (Group group : lp.getGroupManager().getLoadedGroups()) {
               String groupName = group.getName();
               if(LuckPermsIsPlayerInGroup(p,groupName)){
                   i = config.getInt("money.perm."+groupName);
                   break;
               }
           }
       }else if(UseGroupManager()){
           if(GroupManagerGetGroups(p)!=null){
               for (String groupName: Objects.requireNonNull(GroupManagerGetGroups(p))) {
                   if(GroupManagerIsPlayerInGroup(p,groupName)){
                       i = config.getInt("money.perm."+groupName);
                       break;
                   }
               }
           }
       }else {
           Permission pe = PlayerDeathPenalty.perms;
           for(String groupName:pe.getGroups()){
               if(pe.playerInGroup(p,groupName)){
                   i = config.getInt("money.perm."+groupName);
                   break;
               }
           }
       }
       return i;
   }
    public static int getItemGroup(Player p){
        int i = 0;
        if(UseLuckPerms()) {
            for (Group group : lp.getGroupManager().getLoadedGroups()) {
                String groupName = group.getName();
                if(LuckPermsIsPlayerInGroup(p,groupName)){
                    i = config.getInt("item.perm."+groupName);
                    break;
                }
            }
        }else if(UseGroupManager()){
            if(GroupManagerGetGroups(p)!=null){
                for (String groupName: Objects.requireNonNull(GroupManagerGetGroups(p))) {
                    if(GroupManagerIsPlayerInGroup(p,groupName)){
                        i = config.getInt("item.perm."+groupName);
                        break;
                    }
                }
            }
        }else {
            Permission pe = PlayerDeathPenalty.perms;
            for(String groupName:pe.getGroups()){
                if(pe.playerInGroup(p,groupName)){
                    i = config.getInt("item.perm."+groupName);
                    break;
                }
            }
        }
        return i;
    }
    public static int getExpGroup(Player p){
        int i = 0;
        if(UseLuckPerms()) {
            for (Group group : lp.getGroupManager().getLoadedGroups()) {
                String groupName = group.getName();
                if(LuckPermsIsPlayerInGroup(p,groupName)){
                    i = config.getInt("exp.perm."+groupName);
                    break;
                }
            }
        }else if(UseGroupManager()){
            if(GroupManagerGetGroups(p)!=null){
                for (String groupName: Objects.requireNonNull(GroupManagerGetGroups(p))) {
                    if(GroupManagerIsPlayerInGroup(p,groupName)){
                        i = config.getInt("exp.perm."+groupName);
                        break;
                    }
                }
            }
        }else {
            Permission pe = PlayerDeathPenalty.perms;
            for(String groupName:pe.getGroups()){
                if(pe.playerInGroup(p,groupName)){
                    i = config.getInt("exp.perm."+groupName);
                    break;
                }
            }
        }
        return i;
    }
    public static void TakeMoney(Player p, double amount) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
       if(Bukkit.getPluginManager().isPluginEnabled("Essentials")){
           Economy.subtract(p.getUniqueId(),new BigDecimal(amount));
       } else if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
           com.Zrips.CMI.Modules.Economy.Economy.withdrawPlayer(p,amount);
       } else{
           PlayerDeathPenalty.econ.withdrawPlayer(p,amount);
       }
    }

    private static boolean UseLuckPerms(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(provider != null){
            lp = provider.getProvider();
            return true;
        }
        return false;
    }

    private static boolean UseGroupManager() {
        if (gm != null) return true;

        final PluginManager pluginManager = Bukkit.getPluginManager();
        final Plugin GMplugin = pluginManager.getPlugin("GroupManager");

        if (GMplugin != null && GMplugin.isEnabled()) {
            gm = (GroupManager)GMplugin;
            return true;
        }
        return false;
    }
    private static boolean LuckPermsIsPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }

    private static List<String> GroupManagerGetGroups(final Player player) {
        if (!UseGroupManager()) return null;

        final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
        if (handler == null) return null;

        return Arrays.asList(handler.getGroups(player.getName()));
    }

    private static boolean GroupManagerIsPlayerInGroup(Player p,String group){
        boolean b = false;
        if(UseGroupManager()){
            final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(p);
            b = handler.inGroup(p.getName(),group);
        }
        return b;
    }
}
