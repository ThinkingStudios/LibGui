package org.thinkingstudio.libgui_foxified.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

public class ModNetworking {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation("modid", "main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	public static void register() {
		int id = 0;
		INSTANCE.registerMessage(id++, LibGuiPacket.class, LibGuiPacket::encode, LibGuiPacket::decode, LibGuiPacket::handle);
	}
}

