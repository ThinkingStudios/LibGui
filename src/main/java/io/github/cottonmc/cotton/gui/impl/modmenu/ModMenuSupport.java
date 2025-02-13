package io.github.cottonmc.cotton.gui.impl.modmenu;

import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import org.thinkingstudio.libgui_foxified.loader.gui.ModConfigScreenFactory;
import org.thinkingstudio.libgui_foxified.loader.gui.ModConfigScreenInitializer;

import java.util.Objects;

public class ModMenuSupport implements ModConfigScreenInitializer {
	@Override
	public ModConfigScreenFactory getModConfigScreenFactory() {
		return (modContainer, screen) -> new CottonClientScreen(Text.translatable("options.libgui.libgui_settings"), new ConfigGui(screen)) {
			@Override
			public void close() {
				Objects.requireNonNull(this.client).setScreen(screen);
			}
		};
	}
}
