package io.github.yuazer.ztrainerbeat.commands;

import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import io.github.yuazer.zaxlib.Utils.NMSUtils;
import io.github.yuazer.zaxlib.Utils.PlayerUtil;
import io.github.yuazer.ztrainerbeat.Main;
import io.github.yuazer.ztrainerbeat.utils.YamlUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ztrainerbeat")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("reload")) {
                Main.getInstance().reloadConfig();
                sender.sendMessage(YamlUtils.getConfigMessage("Message.reload"));
                return true;
            }
            if (args[0].equalsIgnoreCase("beat") && args.length == 3) {
                // ztb beat 玩家名 文件名 - 对战指定NPC
                File file = new File("plugins/ZTrainerBeat/trainer/" + args[2] + ".zns");
                if (!file.exists()) {
                    sender.sendMessage(YamlUtils.getConfigMessage("Message.notExist"));
                    return false;
                }
                try {
                    Player player = Bukkit.getPlayer(args[1]);
                    NPCTrainer trainer = getNPCTrainerInFile_NBT(file);
                    battleTrainer(player, trainer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("save") && sender.isOp() && (sender instanceof Player)) {
                Player player = (Player) sender;
                Main.getNPCSaver().put(player.getUniqueId(), !Main.getNPCSaver().getOrDefault(player.getUniqueId(), false));
                player.sendMessage(YamlUtils.getConfigMessage("Message.saveMode").replace("%mode%", String.valueOf(Main.getNPCSaver().getOrDefault(player.getUniqueId(), false))));
            }
            if (args[0].equalsIgnoreCase("check") && args.length == 2) {
                try {
                    // ztb check 文件名 - 查看NPC的名称
                    String filename = args[1];
                    File file = new File("plugins/ZTrainerBeat/trainer/" + filename + ".zns");
                    if (!file.exists()) {
                        sender.sendMessage(YamlUtils.getConfigMessage("Message.notExist"));
                        return false;
                    }
                    NPCTrainer trainer = getNPCTrainerInFile_NBT(file);
                    sender.sendMessage(YamlUtils.getConfigMessage("Message.npcCheck").replace("%name%", trainer.getName(ServerNPCRegistry.en_us)));
                    System.out.println(YamlUtils.getConfigMessage("Message.npcCheck").replace("%name%", trainer.getName(ServerNPCRegistry.en_us)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    public static void battleTrainer(Player player, NPCTrainer trainer) {
        ServerPlayerEntity pl = PlayerUtil.getServerPlayerEntity(player);
        if (BattleRegistry.getBattle(pl) != null && !BattleRegistry.getBattle(pl).battleEnded) {
            return;
        }
        BattleParticipant[] bp =
                {new PlayerParticipant(pl,
                        StorageProxy.getParty(player.getUniqueId()).getTeam(), 1)};
        BattleParticipant[] tp = {new TrainerParticipant(trainer, 1)};
        BattleRegistry.startBattle(tp, bp, trainer.battleRules);
    }

    public static NPCTrainer getNPCTrainerInFile_NBT(File file) throws IOException {
        NPCTrainer npcTrainer = new NPCTrainer(NMSUtils.bkToNmsWorld(Bukkit.getWorld("world")));
        CompoundNBT nbt = CompressedStreamTools.func_74797_a(file);
        npcTrainer.func_70037_a(nbt);
        return npcTrainer;
    }
}
