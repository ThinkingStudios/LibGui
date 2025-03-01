package org.thinkingstudio.libgui_foxified.events;

import java.io.IOException;
import java.util.function.Consumer;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.ApiStatus;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Called when core shaders ({@linkplain ShaderProgram shader programs} loaded from {@code assets/<namespace>/shaders/core})
 * are loaded to register custom modded shaders.
 */
@FunctionalInterface
public interface CoreShaderRegistrationCallback {
	Event<CoreShaderRegistrationCallback> EVENT = EventFactory.createArrayBacked(CoreShaderRegistrationCallback.class, callbacks -> context -> {
		for (CoreShaderRegistrationCallback callback : callbacks) {
			callback.registerShaders(context);
		}
	});

	/**
	 * Registers core shaders using the registration context.
	 *
	 * @param context the registration context
	 */
	void registerShaders(RegistrationContext context) throws IOException;

	/**
	 * A context object used to create and register core shader programs.
	 *
	 * <p>This is not meant for implementation by users of the API.
	 */
	@ApiStatus.NonExtendable
	interface RegistrationContext {
		/**
		 * Creates and registers a core shader program.
		 *
		 * <p>The program is loaded from {@code assets/<namespace>/shaders/core/<path>.json}.
		 *
		 * @param id           the program ID
		 * @param vertexFormat the vertex format used by the shader
		 * @param loadCallback a callback that is called when the shader program has been successfully loaded
		 */
		void register(Identifier id, VertexFormat vertexFormat, Consumer<ShaderProgram> loadCallback) throws IOException;
	}
}
