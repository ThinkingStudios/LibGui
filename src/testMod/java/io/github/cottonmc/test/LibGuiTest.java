package io.github.cottonmc.test;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import org.thinkingstudio.libgui_foxified.test.TestModRegistries;

public class LibGuiTest {
	public static final String MODID = "libgui_test";

	public static BlockEntityType<GuiBlockEntity> GUI_BLOCKENTITY_TYPE;
	public static ScreenHandlerType<TestDescription> GUI_SCREEN_HANDLER_TYPE;
	public static ScreenHandlerType<TestItemDescription> ITEM_SCREEN_HANDLER_TYPE;
	public static ScreenHandlerType<ReallySimpleDescription> REALLY_SIMPLE_SCREEN_HANDLER_TYPE;

	public static void onInitialize() {
		TestBlocks.register();
		TestItems.register();
		GUI_BLOCKENTITY_TYPE = new BlockEntityType<>(GuiBlockEntity::new, TestBlocks.GUI);
		TestModRegistries.BLOCK_ENTITY_TYPES.register("gui", () -> GUI_BLOCKENTITY_TYPE);
		//Registry.register(Registries.BLOCK_ENTITY_TYPE, id("gui"), GUI_BLOCKENTITY_TYPE);
		
		GUI_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>((int syncId, PlayerInventory inventory) -> {
			return new TestDescription(GUI_SCREEN_HANDLER_TYPE, syncId, inventory, ScreenHandlerContext.EMPTY);
		}, FeatureSet.of(FeatureFlags.VANILLA));
		TestModRegistries.SCREEN_HANDLER_TYPES.register("gui", () -> GUI_SCREEN_HANDLER_TYPE);
		//Registry.register(Registries.SCREEN_HANDLER, id("gui"), GUI_SCREEN_HANDLER_TYPE);
		ITEM_SCREEN_HANDLER_TYPE = IMenuTypeExtension.create((syncId, inventory, buf) -> {
			StackReference handStack = StackReference.of(inventory.player, inventory.getStack(syncId).getEquipmentSlot());
			return new TestItemDescription(syncId, inventory, handStack);
		});
		TestModRegistries.SCREEN_HANDLER_TYPES.register("item_gui", () -> ITEM_SCREEN_HANDLER_TYPE);
		//Registry.register(Registries.SCREEN_HANDLER, id("item_gui"), ITEM_SCREEN_HANDLER_TYPE);

		REALLY_SIMPLE_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(ReallySimpleDescription::new, FeatureSet.of(FeatureFlags.VANILLA));
		TestModRegistries.SCREEN_HANDLER_TYPES.register("really_simple", () -> REALLY_SIMPLE_SCREEN_HANDLER_TYPE);
		//Registry.register(Registries.SCREEN_HANDLER, id("really_simple"), REALLY_SIMPLE_SCREEN_HANDLER_TYPE);

//		Optional<? extends ModContainer> containerOpt = ModList.get().getModContainerById("jankson");
//		if (containerOpt.isPresent()) {
//			IModInfo modInfo = containerOpt.get().getModInfo();
//			Path rootPath = modInfo.getOwningFile().getFile().getSecureJar().getRootPath();
//			System.out.println("Jankson root path: " + rootPath);
//			try {
//				Files.list(rootPath).forEach((path)->{
//					path.getFileSystem().getFileStores().forEach((store)->{
//						System.out.println("        Filestore: "+store.name());
//					});
//					System.out.println("    "+path.toAbsolutePath());
//				});
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			Path modToml = modInfo.getOwningFile().getFile().findResource("/META-INF/neoforge.mods.toml");
//			System.out.println("Jankson META-INF/neoforge.mods.toml path: " + modToml);
//			System.out.println(Files.exists(modToml) ? "Exists" : "Does Not Exist");
//		} else {
//			System.out.println("Container isn't present!");
//		}
	}

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}
}
