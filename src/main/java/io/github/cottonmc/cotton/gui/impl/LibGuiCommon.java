package io.github.cottonmc.cotton.gui.impl;

import net.minecraft.util.Identifier;

public final class LibGuiCommon {
	public static final String MOD_ID = "libgui";

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
