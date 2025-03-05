package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import net.minecraft.util.Identifier;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
	private static final String PROTOCOL_VERSION = "1";
	public static SimpleChannel INSTANCE;
	private static int packetId = 0; // Every packet needs a unique ID (unique for this channel)

	private static int id() {
		return packetId++;
	}

	public static void register() {
		// Make the channel. If needed you can do version checking here
		SimpleChannel channel = NetworkRegistry.ChannelBuilder
			.named(new Identifier(LibGuiCommon.MOD_ID, "screen_message"))
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.clientAcceptedVersions(s -> true)
			.serverAcceptedVersions(s -> true)
			.simpleChannel();

		INSTANCE = channel;

		channel.registerMessage(id(), LibGuiPacket.class, LibGuiPacket::write, LibGuiPacket::new, LibGuiPacket::handle);
	}
}

