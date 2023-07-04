package io.github.yuazer.ztrainerbeat;

import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.yuazer.ztrainerbeat.commands.MainCommand;
import io.github.yuazer.ztrainerbeat.events.PlayerEvent;
import io.github.yuazer.ztrainerbeat.events.PokeEvent;
import io.izzel.arclight.api.Arclight;
import net.minecraftforge.eventbus.api.IEventBus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private static HashMap<UUID, Boolean> NPCSaver = new HashMap<>();

    public static HashMap<UUID, Boolean> getNPCSaver() {
        return NPCSaver;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
        File file = new File("plugins/ZTrainerBeat/trainer");
        if (!file.exists()) {
            file.mkdirs();
        }
        IEventBus eventBus = Pixelmon.EVENT_BUS;
        Arclight.registerForgeEvent(this, eventBus, new PokeEvent());
        Bukkit.getPluginManager().registerEvents(new PlayerEvent(), this);
        Bukkit.getPluginCommand("ztrainerbeat").setExecutor(new MainCommand());
        logLoaded(this);
    }

    @Override
    public void onDisable() {
        logDisable(this);
    }

    public static void logLoaded(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §f已加载", plugin.getName()));
        Bukkit.getLogger().info("§b作者:§eZ菌");
        Bukkit.getLogger().info("§b版本:§e" + plugin.getDescription().getVersion());
    }

    public static void logDisable(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §c已卸载", plugin.getName()));
    }
}
