package me.mmmjjkx.playerdeathpenalty;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import me.mmmjjkx.playerdeathpenalty.data.PermManager;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Listen implements Listener {
    private final FileConfiguration cfg = PlayerDeathPenalty.config;

    @EventHandler
    private void Money(PlayerDeathEvent e) throws MaxMoneyException, UserDoesNotExistException, NoLoanPermittedException {
        if (PlayerDeathPenalty.config.getBoolean("money.enabled")) {
            if(Objects.requireNonNull(e.getEntity().getPlayer()).hasPermission("playerdeathpenalty.bypass")) {
                return;
            }
            if(e.getEntity().getStatistic(Statistic.DEATHS)<PlayerDeathPenalty.config.getInt("money.didntDeath")){
                return;
            }
            Player p = e.getEntity().getPlayer();
            World w = p.getLocation().getWorld();
            if(PlayerDeathPenalty.config.getStringList("money.worlds").contains(w.getName())) {
                if (PlayerDeathPenalty.hasEconomy()) {
                    double money = PlayerDeathPenalty.econ.getBalance(p);
                    if (money <= PlayerDeathPenalty.config.getDouble("money.didnt")) {
                        return;
                    }
                    BigDecimal chance = new BigDecimal(PermManager.getMoneyGroup(p));
                    BigDecimal chance2 = chance.multiply(new BigDecimal("0.01"));
                    BigDecimal money2 = new BigDecimal(money);
                    BigDecimal tak = chance2.multiply(money2).setScale(2,RoundingMode.HALF_UP);
                    double take = Double.parseDouble(tak.toPlainString());
                    PermManager.TakeMoney(p, take);
                    ThingsRunner.playSound("money",p);
                    ThingsRunner.sendMessage(p, cfg.getString("money.message", "").replaceAll("%percentage%",
                            String.valueOf(chance.intValue())).replaceAll("%num%", String.valueOf(take)));
                    ThingsRunner.runCommand("money",p);
                }
            }
        }
    }
    @EventHandler
    private void Item(PlayerDeathEvent e){
        if(PlayerDeathPenalty.config.getBoolean("item.enabled")) {
            if(e.getEntity().hasPermission("playerdeathpenalty.bypass")) {
                return;
            }
            if(e.getEntity().getStatistic(Statistic.DEATHS)<PlayerDeathPenalty.config.getInt("item.didntDeath")){
                return;
            }
            Player p = e.getEntity();
            World w = p.getLocation().getWorld();
            if(PlayerDeathPenalty.config.getStringList("item.worlds").contains(w.getName())) {
                List<Integer> slots = getSlots(PermManager.getItemGroup(p));
                Location loc = p.getLocation();
                PlayerInventory pl = p.getInventory();
                int s = slots.size();
                for (int slot : slots) {
                    ItemStack is = pl.getItem(slot);
                    if (is == null) {
                        s = s - 1;
                        continue;
                    }
                    pl.setItem(slot, new ItemStack(Material.AIR));
                    w.dropItem(loc, is);
                }
                ThingsRunner.playSound("item",p);
                ThingsRunner.sendMessage(p, cfg.getString("item.message", "")
                                .replaceAll("%num%", String.valueOf(s)));
                ThingsRunner.runCommand("item",p);
            }
        }
    }
    @EventHandler
    private void Exp(PlayerDeathEvent e){
        if(PlayerDeathPenalty.config.getBoolean("exp.enabled")){
            if(e.getEntity().hasPermission("playerdeathpenalty.bypass")) {
                return;
            }
            if(e.getEntity().getStatistic(Statistic.DEATHS)<PlayerDeathPenalty.config.getInt("exp.didntDeath")){
                return;
            }
            Player p = e.getEntity();
            World w = p.getLocation().getWorld();
            if(PlayerDeathPenalty.config.getStringList("exp.worlds").contains(w.getName())) {
                float chance = PermManager.getExpGroup(p);
                if (PlayerDeathPenalty.config.getBoolean("exp.usepoints")) {
                    float point = p.getExp();
                    if (point <= PlayerDeathPenalty.config.getInt("exp.didnt")) {
                        return;
                    }
                    BigDecimal b1 = new BigDecimal(point);
                    BigDecimal b2 = new BigDecimal(chance).multiply(BigDecimal.valueOf(0.01));
                    BigDecimal b3 = b1.multiply(b2).setScale(0, RoundingMode.HALF_UP);
                    BigDecimal b4 = b1.subtract(b3);
                    p.setExp(Float.parseFloat(b4.toPlainString()));
                    ThingsRunner.playSound("exp",p);
                    ThingsRunner.sendMessage(p, cfg.getString("exp.message2", "")
                            .replaceAll("%num%", b4.toPlainString()));
                    ThingsRunner.runCommand("exp",p);
                } else {
                    float level = p.getLevel();
                    if (level <= PlayerDeathPenalty.config.getInt("exp.didnt")) {
                        return;
                    }
                    BigDecimal b1 = new BigDecimal(level);
                    BigDecimal b2 = new BigDecimal(chance);
                    BigDecimal b3 = b1.multiply(b2).setScale(0, RoundingMode.HALF_DOWN);
                    BigDecimal b4 = b1.subtract(b3);
                    p.setLevel(Integer.parseInt(b4.toPlainString()));
                    ThingsRunner.playSound("exp",p);
                    ThingsRunner.sendMessage(p, cfg.getString("exp.message", "")
                            .replaceAll("%num%", b4.toPlainString()));
                    ThingsRunner.runCommand("exp",p);
                }
            }
        }
    }
    @EventHandler
    private void Points(PlayerDeathEvent e) {
        if (PlayerDeathPenalty.config.getBoolean("points.enabled")) {
            if(Objects.requireNonNull(e.getEntity().getPlayer()).hasPermission("playerdeathpenalty.bypass")) {
                return;
            }
            if(e.getEntity().getStatistic(Statistic.DEATHS)<PlayerDeathPenalty.config.getInt("points.didntDeath")){
                return;
            }
            Player p = e.getEntity().getPlayer();
            World w = p.getLocation().getWorld();
            if(PlayerDeathPenalty.config.getStringList("points.worlds").contains(w.getName())) {
                if (PlayerDeathPenalty.hasPlayerPoints()) {
                    int point = PlayerDeathPenalty.PlayerPointsAPI.look(p.getUniqueId());
                    if (point <= PlayerDeathPenalty.config.getDouble("points.didnt")) {
                        return;
                    }
                    BigDecimal chance = new BigDecimal(PermManager.getPointsGroup(p));
                    BigDecimal chance2 = chance.multiply(new BigDecimal("0.01"));
                    BigDecimal money2 = new BigDecimal(point);
                    BigDecimal tak = chance2.multiply(money2).setScale(0,RoundingMode.HALF_UP);
                    PlayerDeathPenalty.PlayerPointsAPI.take(p.getUniqueId(), Integer.parseInt(tak.toPlainString()));
                    ThingsRunner.playSound("points",p);
                    ThingsRunner.sendMessage(p, cfg.getString("points.message", "").replaceAll("%percentage%", String.valueOf(chance.intValue()))
                            .replaceAll("%num%", tak.toPlainString()));
                    ThingsRunner.runCommand("points",p);
                }
            }
        }
    }
    public static List<Integer> getSlots(int size){
        if(size>=35){
            size = 34;
        }
        List<Integer> l = new LinkedList<>();
        for(int i=0;i<size;i++){
            l.add(i,new Random().nextInt(35));
        }
        return l;
    }
}
