package org.thinkingstudio.libgui_foxified.test;

import io.github.cottonmc.test.LibGuiTest;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;

import net.neoforged.neoforge.registries.DeferredRegister;

public class TestModRegistries {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LibGuiTest.MODID);
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LibGuiTest.MODID);

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LibGuiTest.MODID);
	public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLER_TYPES = DeferredRegister.create(Registries.SCREEN_HANDLER, LibGuiTest.MODID);
}
