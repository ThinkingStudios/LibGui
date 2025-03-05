package io.github.cottonmc.test;

import io.github.cottonmc.test.client.LibGuiTestClient;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(LibGuiTest.MODID)
public class LibGuiTest {
	public static final String MODID = "libgui-test";
	public static GuiBlock GUI_BLOCK;
	public static Block NO_BLOCK_INVENTORY_BLOCK;
	public static BlockItem GUI_BLOCK_ITEM;
	public static RegistryObject<BlockEntityType<GuiBlockEntity>> GUI_BLOCKENTITY_TYPE;
	public static ScreenHandlerType<TestDescription> GUI_SCREEN_HANDLER_TYPE;
	public static ScreenHandlerType<TestItemDescription> ITEM_SCREEN_HANDLER_TYPE;
	public static ScreenHandlerType<ReallySimpleDescription> REALLY_SIMPLE_SCREEN_HANDLER_TYPE;

	public LibGuiTest() {
		if (FMLEnvironment.dist.isClient()){
			LibGuiTestClient.onInitializeClient();
		}
		Registry.register(Registries.ITEM, new Identifier(MODID, "client_gui"), new GuiItem());
		
		GUI_BLOCK = new GuiBlock();
		Registry.register(Registries.BLOCK, new Identifier(MODID, "gui"), GUI_BLOCK);
		GUI_BLOCK_ITEM = new BlockItem(GUI_BLOCK, new Item.Settings());
		Registry.register(Registries.ITEM, new Identifier(MODID, "gui"), GUI_BLOCK_ITEM);
		NO_BLOCK_INVENTORY_BLOCK = new NoBlockInventoryBlock(AbstractBlock.Settings.copy(Blocks.STONE));
		Registry.register(Registries.BLOCK, new Identifier(MODID, "no_block_inventory"), NO_BLOCK_INVENTORY_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MODID, "no_block_inventory"), new BlockItem(NO_BLOCK_INVENTORY_BLOCK, new Item.Settings()));
		GUI_BLOCKENTITY_TYPE = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID).register("gui", () -> BlockEntityType.Builder.create(GuiBlockEntity::new, GUI_BLOCK).build(null));
//		Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "gui"), GUI_BLOCKENTITY_TYPE);
		
		GUI_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>((int syncId, PlayerInventory inventory) -> {
			return new TestDescription(GUI_SCREEN_HANDLER_TYPE, syncId, inventory, ScreenHandlerContext.EMPTY);
		}, FeatureSet.of(FeatureFlags.VANILLA));
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "gui"), GUI_SCREEN_HANDLER_TYPE);

		//TODO: Fabric's ExtendedScreenHandlerType class port to Forge or find a alternative methods
//		ITEM_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
//			var equipmentSlot = buf.readEnumConstant(EquipmentSlot.class);
//			StackReference handStack = StackReference.of(inventory.player, equipmentSlot);
//			return new TestItemDescription(syncId, inventory, handStack);
//		});
//		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "item_gui"), ITEM_SCREEN_HANDLER_TYPE);

		REALLY_SIMPLE_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(ReallySimpleDescription::new, FeatureSet.of(FeatureFlags.VANILLA));
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MODID, "really_simple"), REALLY_SIMPLE_SCREEN_HANDLER_TYPE);

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

}
