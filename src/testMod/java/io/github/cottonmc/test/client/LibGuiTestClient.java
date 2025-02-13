package io.github.cottonmc.test.client;

import com.mojang.brigadier.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.CottonHud;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.test.LibGuiTest;
import io.github.cottonmc.test.ReallySimpleDescription;
import io.github.cottonmc.test.TestDescription;
import io.github.cottonmc.test.TestItemDescription;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Function;

public class LibGuiTestClient {

	public static void onInitializeClient() {
		HandledScreens.<TestDescription, CottonInventoryScreen<TestDescription>>register(
				LibGuiTest.GUI_SCREEN_HANDLER_TYPE,
				CottonInventoryScreen::new
		);

		HandledScreens.<ReallySimpleDescription, CottonInventoryScreen<ReallySimpleDescription>>register(
				LibGuiTest.REALLY_SIMPLE_SCREEN_HANDLER_TYPE,
				CottonInventoryScreen::new
		);

		HandledScreens.<TestItemDescription, CottonInventoryScreen<TestItemDescription>>register(
				LibGuiTest.ITEM_SCREEN_HANDLER_TYPE,
				CottonInventoryScreen::new
		);

		CottonHud.add(new WHudTest(), 10, -20, 10, 10);
		CottonHud.add(new WLabel(Text.literal("Test label")), 10, -30, 10, 10);
	}

	@OnlyIn(Dist.CLIENT)
	public static Command<ServerCommandSource> openScreen(Function<MinecraftClient, LightweightGuiDescription> screenFactory) {
		return openScreen(ScreenTexts.EMPTY, screenFactory);
	}

	@OnlyIn(Dist.CLIENT)
	public static Command<ServerCommandSource> openScreen(Text title, Function<MinecraftClient, LightweightGuiDescription> screenFactory) {
		return context -> {
			var client = MinecraftClient.getInstance();
			client.send(() -> client.setScreen(new CottonClientScreen(title, screenFactory.apply(client))));
			return Command.SINGLE_SUCCESS;
		};
	}
}
