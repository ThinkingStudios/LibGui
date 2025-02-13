package io.github.cottonmc.cotton.gui.impl.client;

//import net.fabricmc.loader.api.FabricLoader;
//import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import net.minecraft.world.World;

import net.neoforged.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Crashes the game if a LibGui screen is opened in {@code Item.use/useOnBlock/useOnEntity}.
 */
public final class ItemUseChecker {
	// Setting this property to "true" disables the check.
	private static final String ALLOW_ITEM_USE_PROPERTY = "libgui.allowItemUse";

	// Stack walker instance used to check the caller.
	private static final StackWalker STACK_WALKER =
			StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

	// List of banned item use methods.
	private static final List<Pair<String, MethodType>> ITEM_USE_METHODS = Util.make(new ArrayList<>(), result -> {
		Class<Hand> hand = Hand.class;
		Class<ActionResult> actionResult = ActionResult.class;
		Class<LivingEntity> livingEntity = LivingEntity.class;
		Class<PlayerEntity> playerEntity = PlayerEntity.class;
		Class<ItemStack> itemStack = ItemStack.class;
		Class<ItemUsageContext> itemUsageContext = ItemUsageContext.class;
		Class<World> world = World.class;

		// Must be mojmap method name!
		// use
		result.add(resolveItemMethod("use", actionResult, world, playerEntity, hand));
		// useOnBlock
		result.add(resolveItemMethod("useOnBlock", actionResult, itemUsageContext));
		// useOnEntity
		result.add(resolveItemMethod("useOnEntity", actionResult, itemStack, playerEntity, livingEntity, hand));
	});

	private static Pair<String, MethodType> resolveItemMethod(String name, Class<?> returnType, Class<?>... parameterTypes) {
		// Remap the method name.
		String deobfName = ObfuscationReflectionHelper.findMethod(Item.class, name, parameterTypes).getName();

		// Remap the descriptor types.
		Function<Class<?>, Object> getIntermediaryClass = className -> {
			try {
				return Class.forName(className.getName());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Could not resolve class" + className.getName(), e);
			}
		};
		Class<?>[] paramClasses = Arrays.stream(parameterTypes)
				.map(getIntermediaryClass)
				.toArray(Class[]::new);
		Class<?> returnClass = getIntermediaryClass.apply(returnType).getClass();

		// Check that the method actually exists.
		try {
			Item.class.getMethod(deobfName, paramClasses);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Could not find Item method " + deobfName, e);
		}

		return new Pair<>(deobfName, MethodType.methodType(returnClass, paramClasses));
	}

	/**
	 * Checks whether the specified screen is a LibGui screen opened
	 * from an item usage method.
	 *
	 * @throws CrashException if opening the screen is not allowed
	 */
	public static void checkSetScreen(Screen screen) {
		if (!(screen instanceof CottonScreenImpl cs) || Boolean.getBoolean(ALLOW_ITEM_USE_PROPERTY)) return;

		// Check if this is called via Item.use. If so, crash the game.

		// The calling variant of Item.use[OnBlock|OnEntity].
		// If null, nothing bad happened.
		@Nullable Pair<? extends Class<?>, String> useMethodCaller = STACK_WALKER.walk(s -> s
						.skip(3) // checkSetScreen, setScreen injection, setScreen
						.flatMap(frame -> {
							if (!Item.class.isAssignableFrom(frame.getDeclaringClass())) return Stream.empty();

							return ITEM_USE_METHODS.stream()
									.filter(method -> method.getLeft().equals(frame.getMethodName()) &&
											method.getRight().equals(frame.getMethodType()))
									.map(method -> new Pair<>(frame.getDeclaringClass(), method.getLeft()));
						})
						.findFirst())
				.orElse(null);

		if (useMethodCaller != null) {
			String message = """
						[LibGui] Screens cannot be opened in item use methods. Some alternatives include:
							- Using a packet together with LightweightGuiDescription
							- Using an ItemSyncedGuiDescription
						Setting the screen in item use methods leads to threading issues and
						other potential crashes on both the client and the server.
						If you want to disable this check, set the system property %s to "true"."""
					.formatted(ALLOW_ITEM_USE_PROPERTY);
			var cause = new UnsupportedOperationException(message);
			cause.fillInStackTrace();
			CrashReport report = CrashReport.create(cause, "Opening screen");
			report.addElement("Screen opening details")
					.add("Screen class", screen.getClass().getName())
					.add("GUI description", () -> cs.getDescription().getClass().getName())
					.add("Item class", () -> useMethodCaller.getLeft().getName())
					.add("Involved method", useMethodCaller.getRight());
			throw new CrashException(report);
		}
	}
}
