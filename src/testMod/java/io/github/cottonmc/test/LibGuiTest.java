package io.github.cottonmc.test;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class LibGuiTest {
	public static final String MODID = "libgui_test";

	public static GuiBlock GUI_BLOCK;
	public static Block NO_BLOCK_INVENTORY_BLOCK;
	public static BlockItem GUI_BLOCK_ITEM;
	public static BlockEntityType<GuiBlockEntity> GUI_BLOCKENTITY_TYPE;
	public static ScreenHandlerType<TestDescription> GUI_SCREEN_HANDLER_TYPE;
	public static ScreenHandlerType<TestItemDescription> ITEM_SCREEN_HANDLER_TYPE;
	public static ScreenHandlerType<ReallySimpleDescription> REALLY_SIMPLE_SCREEN_HANDLER_TYPE;

	public static void onInitialize() {
		Registry.register(Registries.ITEM, id("client_gui"), new GuiItem());
		
		GUI_BLOCK = new GuiBlock();
		Registry.register(Registries.BLOCK, id("gui"), GUI_BLOCK);
		GUI_BLOCK_ITEM = new BlockItem(GUI_BLOCK, new Item.Settings());
		Registry.register(Registries.ITEM, id("gui"), GUI_BLOCK_ITEM);
		NO_BLOCK_INVENTORY_BLOCK = new NoBlockInventoryBlock(AbstractBlock.Settings.copy(Blocks.STONE));
		Registry.register(Registries.BLOCK, id("no_block_inventory"), NO_BLOCK_INVENTORY_BLOCK);
		Registry.register(Registries.ITEM, id("no_block_inventory"), new BlockItem(NO_BLOCK_INVENTORY_BLOCK, new Item.Settings()));
		GUI_BLOCKENTITY_TYPE = BlockEntityType.Builder.create(GuiBlockEntity::new, GUI_BLOCK).build(null);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, id("gui"), GUI_BLOCKENTITY_TYPE);
		
		GUI_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>((int syncId, PlayerInventory inventory) -> {
			return new TestDescription(GUI_SCREEN_HANDLER_TYPE, syncId, inventory, ScreenHandlerContext.EMPTY);
		}, FeatureSet.of(FeatureFlags.VANILLA));
		Registry.register(Registries.SCREEN_HANDLER, id("gui"), GUI_SCREEN_HANDLER_TYPE);
		ITEM_SCREEN_HANDLER_TYPE = IMenuTypeExtension.create((syncId, inventory, buf) -> {
			StackReference handStack = StackReference.of(inventory.player, inventory.getStack(syncId).getEquipmentSlot());
			return new TestItemDescription(syncId, inventory, handStack);
		});
		Registry.register(Registries.SCREEN_HANDLER, id("item_gui"), ITEM_SCREEN_HANDLER_TYPE);

		REALLY_SIMPLE_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(ReallySimpleDescription::new, FeatureSet.of(FeatureFlags.VANILLA));
		Registry.register(Registries.SCREEN_HANDLER, id("really_simple"), REALLY_SIMPLE_SCREEN_HANDLER_TYPE);

//		Optional<ModContainer> containerOpt = FabricLoader.getInstance().getModContainer("jankson");
//		if (containerOpt.isPresent()) {
//			ModContainer jankson = containerOpt.get();
//			System.out.println("Jankson root path: "+jankson.getRootPath());
//			try {
//				Files.list(jankson.getRootPath()).forEach((path)->{
//					path.getFileSystem().getFileStores().forEach((store)->{
//						System.out.println("        Filestore: "+store.name());
//					});
//					System.out.println("    "+path.toAbsolutePath());
//				});
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			Path modJson = jankson.getPath("/fabric.mod.json");
//			System.out.println("Jankson fabric.mod.json path: "+modJson);
//			System.out.println(Files.exists(modJson) ? "Exists" : "Does Not Exist");
//		} else {
//			System.out.println("Container isn't present!");
//		}
	}

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}
}
