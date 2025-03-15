package com.opondeath;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.TextComponent;

@Mod(modid = "opondeath", name = "Op On Death", version = "1.0", acceptableRemoteVersions = "*")
public class OpOnDeathMod {
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        MinecraftServer server = player.getServer();

        if (server != null) {
            server.getPlayerList().op(player.getGameProfile());
            player.sendSystemMessage(new TextComponent("You have been OP'd on death!"), player.getUUID());
        }
    }
}