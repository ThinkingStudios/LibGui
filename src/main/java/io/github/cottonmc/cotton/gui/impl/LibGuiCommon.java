package io.github.cottonmc.cotton.gui.impl;

import net.minecraft.util.Identifier;

public final class LibGuiCommon {
	public static final String MOD_ID = "libgui";

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void onInitialize() {
		ScreenNetworkingImpl.init();
	}
}
