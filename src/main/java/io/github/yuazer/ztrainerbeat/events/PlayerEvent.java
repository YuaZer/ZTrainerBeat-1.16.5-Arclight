package io.github.yuazer.ztrainerbeat.events;

import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import io.github.yuazer.zaxlib.Utils.NMSUtils;
import io.github.yuazer.zaxlib.Utils.PokeUtils;
import io.github.yuazer.ztrainerbeat.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.io.IOException;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) throws IOException {
        try {
            Player player = event.getPlayer();
            if (event.getHand().equals(EquipmentSlot.HAND) && Main.getNPCSaver().getOrDefault(player.getUniqueId(), Boolean.FALSE)) {
                Entity entity = event.getRightClicked();
                net.minecraft.entity.Entity nmsEntity = NMSUtils.bkToNmsEntity(entity);
                if (nmsEntity instanceof NPCTrainer) {
                    File file = new File("plugins/ZTrainerBeat/Trainer/" + nmsEntity.func_110124_au() + ".zns");
                    PokeUtils.setNPCTrainerInFile_NBT((NPCTrainer) NMSUtils.bkToNmsEntity(entity), file);
                    player.sendMessage("§aNPC保存成功!文件名为:" + nmsEntity.func_110124_au() + ".zns");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
