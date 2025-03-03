package io.github.cottonmc.cotton.gui.impl.modmenu;

import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import org.thinkingstudio.libgui_foxified.loader.gui.ModConfigScreenInitializer;

public class ModMenuSupport implements ModConfigScreenInitializer {
	@Override
	public ConfigScreenHandler.ConfigScreenFactory getModConfigScreenFactory() {
		return new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> {
			return new CottonClientScreen(Text.translatable("options.libgui.libgui_settings"), new ConfigGui(screen)) {
				@Override
				public void close() {
					this.client.setScreen(screen);
				}
			};
		});
	}
}
