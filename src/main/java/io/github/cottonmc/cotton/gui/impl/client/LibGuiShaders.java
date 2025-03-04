package io.github.cottonmc.cotton.gui.impl.client;

import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

public final class LibGuiShaders {
	private static @Nullable ShaderInstance tiledRectangle;

	public static void setTiledRectangle(@Nullable ShaderInstance tiledRectangle) {
		LibGuiShaders.tiledRectangle = tiledRectangle;
	}

	static void register() {
//		CoreShaderRegistrationCallback.EVENT.register(context -> {
//			// Register our core shaders.
//			// The tiled rectangle shader is used for performant tiled texture rendering.
//			context.register(new ResourceLocation(LibGuiCommon.MOD_ID, "tiled_rectangle"), DefaultVertexFormat.POSITION, program -> tiledRectangle = program);
//		});
	}

	private static ShaderInstance assertPresent(ShaderInstance program, String name) {
		if (program == null) {
			throw new NullPointerException("Shader libgui:" + name + " not initialised!");
		}

		return program;
	}

	public static ShaderInstance getTiledRectangle() {
		return assertPresent(tiledRectangle, "tiled_rectangle");
	}
}
