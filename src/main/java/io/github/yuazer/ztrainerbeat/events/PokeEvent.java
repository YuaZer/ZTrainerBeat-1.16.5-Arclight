package io.github.yuazer.ztrainerbeat.events;

import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import io.github.yuazer.ztrainerbeat.Main;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PokeEvent {
    @SubscribeEvent
    public void onTrainerWin(BeatTrainerEvent e) {
        NPCTrainer trainer = e.trainer;
        String uuid = trainer.getName("en_us");
        if (Main.getInstance().getConfig().getConfigurationSection("BeatHandler") != null && Main.getInstance().getConfig().getConfigurationSection("BeatHandler").getKeys(false).contains(uuid)) {
            Player player = Bukkit.getPlayer(e.player.func_110124_au());
            if (Main.getInstance().getConfig().getStringList("BeatHandler." + uuid + ".win").isEmpty()) {
                return;
            }
            for (String winCmd : Main.getInstance().getConfig().getStringList("BeatHandler." + uuid + ".win")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), winCmd.replace("%player%", player.getName()));
            }
        }
    }

    @SubscribeEvent
    public void onTrainerLose(LostToTrainerEvent e) {
        NPCTrainer trainer = e.trainer;
        String uuid = trainer.getName("en_us");
        if (Main.getInstance().getConfig().getConfigurationSection("BeatHandler") != null && Main.getInstance().getConfig().getConfigurationSection("BeatHandler").getKeys(false).contains(uuid)) {
            Player player = Bukkit.getPlayer(e.player.func_110124_au());
            if (Main.getInstance().getConfig().getStringList("BeatHandler." + uuid + ".lose").isEmpty()) {
                return;
            }
            for (String winCmd : Main.getInstance().getConfig().getStringList("BeatHandler." + uuid + ".lose")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), winCmd.replace("%player%", player.getName()));
            }
        }
    }
}
