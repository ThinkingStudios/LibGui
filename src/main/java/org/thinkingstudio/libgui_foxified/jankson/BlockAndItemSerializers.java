package org.thinkingstudio.libgui_foxified.jankson;

import java.util.Collection;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.Marshaller;

public class BlockAndItemSerializers {

	public static ItemStack getItemStack(JsonObject json, Marshaller m) {
		String itemIdString = json.get(String.class, "item");
		Item item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(itemIdString)).orElse(Items.AIR);
		ItemStack stack = new ItemStack(item);
		if (json.containsKey("count")) {
			Integer count = json.get(Integer.class, "count");
			if (count!=null) {
				stack.setCount(count);
			}
		}
		return stack;
	}

	public static ItemStack getItemStackPrimitive(String s, Marshaller m) {
		Item item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(s)).orElse(Items.AIR);
		ItemStack stack = new ItemStack(item);
		return stack;
	}

	public static JsonElement saveItemStack(ItemStack stack, Marshaller m) {
		JsonPrimitive id = new JsonPrimitive(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
		if (stack.getCount()==1) return id;
	
		JsonObject result = new JsonObject();
		result.put("item", new JsonPrimitive(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()));
		result.put("count", new JsonPrimitive(stack.getCount()));
		return result;
	}

	@Deprecated
	public static Block getBlockPrimitive(String blockIdString, Marshaller m) {
		Optional<Block> blockOpt = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(blockIdString));
		return blockOpt.orElse(null);
	}

	@Deprecated
	public static JsonElement saveBlock(Block block, Marshaller m) {
		return new JsonPrimitive(BuiltInRegistries.BLOCK.getKey(block).toString());
	}
	
	
	public static BlockState getBlockStatePrimitive(String blockIdString, Marshaller m) {
		Optional<Block> blockOpt = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(blockIdString));
		if (blockOpt.isPresent()) {
			return blockOpt.get().defaultBlockState();
		} else {
			return null;
		}
	}
	
	/**
	 * @param json A json object representing a BlockState
	 * @return the BlockState represented, or null if the object does not represent a valid BlockState.
	 */
	public static BlockState getBlockState(JsonObject json, Marshaller m) {
		String blockIdString = json.get(String.class, "block");
		
		Block block = BuiltInRegistries.BLOCK.getOptional(new ResourceLocation(blockIdString)).orElse(null);
		if (block==null) return null;
		
		BlockState state = block.defaultBlockState();
		JsonObject stateObject = json.getObject("BlockStateTag");
		if (stateObject==null) stateObject = json;
		
		Collection<Property<?>> properties = state.getProperties();
		for(String key : stateObject.keySet()) {
			if (stateObject==json && (key.equals("BlockStateTag") || key.equals("block"))) continue;
			
			for(Property<?> property : properties) {
				if (property.getName().equals(key)) {
					String val = stateObject.get(String.class, key);
					state = withProperty(state, property, val);
					break;
				}
			}
		}
		
		return state;
	}
	
	public static JsonElement saveBlockState(BlockState state, Marshaller m) {
		BlockState defaultState = state.getBlock().defaultBlockState();
		
		if (state.equals(defaultState)) {
			//Use a String for the blockID only
			return new JsonPrimitive( BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString() );
			
		} else {
			JsonObject result = new JsonObject();
			result.put("block", new JsonPrimitive( BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString() ));
			JsonObject stateObject = result;
			for(Property<?> property : state.getProperties()) {
				String key = property.getName();
				if (key.equals("block") || key.equals("BlockStateTag")) { //Certain dangerous keys mean we should partition off blockstate properties
					stateObject = new JsonObject();
					result.put("BlockStateTag", stateObject);
					break;
				}
			}
			
			for(Property<?> property : state.getProperties()) {
				if (state.getValue(property).equals(defaultState.getValue(property))) continue;
				String key = property.getName();
				String val = getProperty(state, property);
				stateObject.put(key, new JsonPrimitive(val));
			}
			
			return result;
		}
	}
	
	public static <T extends Comparable<T>> BlockState withProperty(BlockState state, Property<T> property, String stringValue) {
		Optional<T> val = property.getValue(stringValue);
		if (val.isPresent()) {
			return state.setValue(property, val.get());
		} else {
			return state;
		}
	}
	
	public static <T extends Comparable<T>> String getProperty(BlockState state, Property<T> property) {
		return property.getName(state.getValue(property));
	}
}
