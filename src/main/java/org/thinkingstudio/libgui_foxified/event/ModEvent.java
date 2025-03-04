package org.thinkingstudio.libgui_foxified.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiShaders;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = LibGuiCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {
	@SubscribeEvent
	public static void registerShader(RegisterShadersEvent event) throws IOException {
		ShaderInstance tiledRectangleShader = new ShaderInstance(event.getResourceProvider(), new ResourceLocation(LibGuiCommon.MOD_ID, "tiled_rectangle"), DefaultVertexFormat.POSITION);
		event.registerShader(tiledRectangleShader, LibGuiShaders::setTiledRectangle);
	}
}
