package org.thinkingstudio.libgui_foxified.test;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import io.github.cottonmc.cotton.gui.impl.modmenu.ConfigGui;
import io.github.cottonmc.test.LibGuiTest;
import io.github.cottonmc.test.client.DarkModeTestGui;
import io.github.cottonmc.test.client.GhostIconTestGui;
import io.github.cottonmc.test.client.InsetsTestGui;
import io.github.cottonmc.test.client.Issue182TestGui;
import io.github.cottonmc.test.client.Issue196TestGui;
import io.github.cottonmc.test.client.ItemTestGui;
import io.github.cottonmc.test.client.LibGuiTestClient;

import io.github.cottonmc.test.client.ListTestGui;
import io.github.cottonmc.test.client.PaddingTestGui;
import io.github.cottonmc.test.client.ScrollBarTestGui;
import io.github.cottonmc.test.client.ScrollingTestGui;
import io.github.cottonmc.test.client.TabTestGui;
import io.github.cottonmc.test.client.TextAlignmentTestGui;
import io.github.cottonmc.test.client.TextFieldTestGui;
import io.github.cottonmc.test.client.TextureTestGui;
import io.github.cottonmc.test.client.TitleAlignmentTestGui;

import net.minecraft.server.command.CommandManager;

import net.minecraft.text.Text;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

import static io.github.cottonmc.test.client.LibGuiTestClient.openScreen;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mod(LibGuiTest.MODID)
public class LibGuiFoxifiedTest {
	public LibGuiFoxifiedTest(IEventBus modEventBus) {
		TestModRegistries.BLOCK_ENTITY_TYPES.register(modEventBus);
		TestModRegistries.SCREEN_HANDLER_TYPES.register(modEventBus);
		LibGuiTest.onInitialize();
		if (FMLLoader.getDist().isClient()) {
			LibGuiTestClient.onInitializeClient();

			NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, event -> {

				event.getDispatcher().register(
					literal("libgui")
						.then(literal("config").executes(openScreen(client -> new ConfigGui(client.currentScreen))))
						.then(literal("tab").executes(openScreen(client -> new TabTestGui())))
						.then(literal("scrolling").executes(openScreen(client -> new ScrollingTestGui())))
						.then(literal("scrollbar").executes(openScreen(client -> new ScrollBarTestGui())))
						.then(literal("insets").executes(openScreen(client -> new InsetsTestGui())))
						.then(literal("textfield").executes(openScreen(client -> new TextFieldTestGui())))
						.then(literal("paddings")
							.then(argument("horizontal", IntegerArgumentType.integer(0))
								.then(argument("vertical", IntegerArgumentType.integer(0))
									.executes(context -> {
										var hori = IntegerArgumentType.getInteger(context, "horizontal");
										var vert = IntegerArgumentType.getInteger(context, "vertical");
										return openScreen(client -> new PaddingTestGui(hori, vert)).run(context);
									}))))
						.then(literal("#182").executes(openScreen(client -> new Issue182TestGui())))
						.then(literal("#196").executes(openScreen(client -> new Issue196TestGui())))
						.then(literal("darkmode").executes(openScreen(client -> new DarkModeTestGui())))
						.then(literal("titlealignment").executes(openScreen(Text.literal("test title"), client -> new TitleAlignmentTestGui())))
						.then(literal("texture").executes(openScreen(client -> new TextureTestGui())))
						.then(literal("textalignment").executes(openScreen(client -> new TextAlignmentTestGui())))
						.then(literal("list").executes(openScreen(client -> new ListTestGui())))
						.then(literal("ghosticon").executes(openScreen(client -> new GhostIconTestGui())))
						.then(literal("item").executes(openScreen(client -> new ItemTestGui())))
				);
			});
		}
	}
}
