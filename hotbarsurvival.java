package com.hotbarsurvival;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyMapping;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

@Mod("hotbarsurvival")
public class HotbarSurvivalMod {
    public static final String MOD_ID = "hotbarsurvival";
    private static final KeyMapping keyLoadHotbar = new KeyMapping("key.hotbarsurvival.load", 
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, "key.categories.hotbarsurvival");

    public HotbarSurvivalMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(keyLoadHotbar);
        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD)
    public static class CapabilityRegistry {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(HotbarCapability.HotbarStorage.class);
        }
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class KeyInputHandler {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (keyLoadHotbar.consumeClick()) {  
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer player = mc.player;
                if (player != null) {
                    saveHotbar(1, player);
                    player.sendMessage(new net.minecraft.network.chat.TextComponent("Hotbar saved!"), player.getUUID());
                }
            }
        }
    }

    private static void loadHotbar(int slot, Player player) {
        player.getCapability(HotbarCapability.HOTBAR_CAP).ifPresent(hotbar -> {
            CompoundTag hotbarData = hotbar.getHotbarData(slot);
            for (int i = 0; i < 9; i++) {
                if (hotbarData.contains("slot" + i)) {
                    player.getInventory().setItem(i, ItemStack.of(hotbarData.getCompound("slot" + i)));
                }
            }
            player.sendMessage(new net.minecraft.network.chat.TextComponent("Hotbar loaded!"), player.getUUID());
        });
    }

    private static void saveHotbar(int slot, Player player) {
        player.getCapability(HotbarCapability.HOTBAR_CAP).ifPresent(hotbar -> {
            CompoundTag hotbarData = new CompoundTag();
            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                hotbarData.put("slot" + i, stack.save(new CompoundTag()));
            }
            hotbar.setHotbarData(slot, hotbarData);
        });
    }
}