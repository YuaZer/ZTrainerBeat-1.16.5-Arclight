package io.github.yuazer.ztrainerbeat.utils;

import io.github.yuazer.ztrainerbeat.Main;

import java.util.List;

public class YamlUtils {
    public static String getConfigMessage(String path) {
        return Main.getInstance().getConfig().getString(path).replace("&", "§");
    }

    public static List<String> getConfigStringList(String path) {
        return Main.getInstance().getConfig().getStringList(path);
    }

    public static boolean getConfigBoolean(String path) {
        return Main.getInstance().getConfig().getBoolean(path);
    }

    public static int getConfigInt(String path) {
        return Main.getInstance().getConfig().getInt(path);
    }

    public static double getConfigDouble(String path) {
        return Main.getInstance().getConfig().getDouble(path);
    }
}
