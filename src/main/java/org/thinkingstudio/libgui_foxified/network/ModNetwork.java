package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new Identifier(LibGuiCommon.MOD_ID, "main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	public static void register() {
		int id = 0;
		INSTANCE.registerMessage(id++, LibGuiPacket.class, LibGuiPacket::encode, LibGuiPacket::decode, LibGuiPacket::handle);
	}
}

