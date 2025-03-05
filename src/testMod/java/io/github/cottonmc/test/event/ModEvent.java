package io.github.cottonmc.test.event;

import io.github.cottonmc.test.LibGuiTest;

import net.minecraft.server.command.CommandManager;

import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibGuiTest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {
	@SubscribeEvent
	public static void registerCommand(RegisterClientCommandsEvent event){

	}
}
