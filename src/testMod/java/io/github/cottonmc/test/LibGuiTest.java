package io.github.cottonmc.test;

import io.github.cottonmc.test.client.LibGuiTestClient;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(LibGuiTest.MODID)
public class LibGuiTest {
	public static final String MODID = "libgui_test";

	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
	public static DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

	public static final RegistryObject<GuiBlock> GUI_BLOCK = BLOCKS.register("gui", () -> new GuiBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));
	public static final RegistryObject<Block> NO_BLOCK_INVENTORY_BLOCK = BLOCKS.register("no_block_inventory", () -> new NoBlockInventoryBlock(AbstractBlock.Settings.copy(Blocks.STONE)));
	public static RegistryObject<BlockItem> GUI_BLOCK_ITEM;
	public static final RegistryObject<BlockEntityType<GuiBlockEntity>> GUI_BLOCKENTITY_TYPE = BLOCK_ENTITY_TYPES.register("gui", () -> BlockEntityType.Builder.create(GuiBlockEntity::new, GUI_BLOCK.get()).build(null));
	public static RegistryObject<ScreenHandlerType<TestDescription>> GUI_SCREEN_HANDLER_TYPE;
	public static RegistryObject<ScreenHandlerType<TestItemDescription>> ITEM_SCREEN_HANDLER_TYPE;
	public static RegistryObject<ScreenHandlerType<ReallySimpleDescription>> REALLY_SIMPLE_SCREEN_HANDLER_TYPE;

	public LibGuiTest() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ITEMS.register(modEventBus);
		BLOCKS.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		SCREEN_HANDLER_TYPES.register(modEventBus);

		ITEMS.register("client_gui", () -> new GuiItem());
		GUI_BLOCK_ITEM = ITEMS.register("gui", () -> new BlockItem(GUI_BLOCK.get(), new Item.Settings()));
		ITEMS.register("no_block_inventory", () -> new BlockItem(NO_BLOCK_INVENTORY_BLOCK.get(), new Item.Settings()));

		GUI_SCREEN_HANDLER_TYPE = SCREEN_HANDLER_TYPES.register("gui", () -> new ScreenHandlerType<>((int syncId, PlayerInventory inventory) -> {
			return new TestDescription(GUI_SCREEN_HANDLER_TYPE.get(), syncId, inventory, ScreenHandlerContext.EMPTY);
		}, FeatureSet.of(FeatureFlags.VANILLA)));

		ITEM_SCREEN_HANDLER_TYPE = SCREEN_HANDLER_TYPES.register("item_gui", () -> IForgeMenuType.create((syncId, inventory, buf) -> {
			var equipmentSlot = buf.readEnumConstant(EquipmentSlot.class);
			StackReference handStack = StackReference.of(inventory.player, equipmentSlot);
			return new TestItemDescription(syncId, inventory, handStack);
		}));

		REALLY_SIMPLE_SCREEN_HANDLER_TYPE = SCREEN_HANDLER_TYPES.register("really_simple", () -> new ScreenHandlerType<>(ReallySimpleDescription::new, FeatureSet.of(FeatureFlags.VANILLA)));

		if (FMLLoader.getDist().isClient()){
			LibGuiTestClient.onInitializeClient();
		}

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
