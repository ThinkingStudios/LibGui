package io.github.cottonmc.cotton.gui.impl.client;

import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
	private static final List<Tuple<String, MethodType>> ITEM_USE_METHODS = Util.make(new ArrayList<>(), result -> {
		Class<InteractionHand> interactionHand = InteractionHand.class;
		Class<InteractionResult> interactionResult = InteractionResult.class;
		Class<InteractionResultHolder> interactionResultHolder = InteractionResultHolder.class;
		Class<LivingEntity> livingEntity = LivingEntity.class;
		Class<Player> player = Player.class;
		Class<ItemStack> itemStack = ItemStack.class;
		Class<UseOnContext> useOnContext = UseOnContext.class;
		Class<Level> level = Level.class;

		// Must be mojmap method name!
		// use
		result.add(resolveItemMethod("method_7836", interactionResultHolder, level, player, interactionHand));
		// useOnBlock
		result.add(resolveItemMethod("method_7884", interactionResult, useOnContext));
		// useOnEntity
		result.add(resolveItemMethod("method_7847", interactionResult, itemStack, player, livingEntity, interactionHand));
	});

	private static Tuple<String, MethodType> resolveItemMethod(String name, Class<?> returnType, Class<?>... parameterTypes) {
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

		return new Tuple<>(deobfName, MethodType.methodType(returnClass, paramClasses));
	}

	/**
	 * Checks whether the specified screen is a LibGui screen opened
	 * from an item usage method.
	 *
	 * @throws ReportedException if opening the screen is not allowed
	 */
	public static void checkSetScreen(Screen screen) {
		if (!(screen instanceof CottonScreenImpl cs) || Boolean.getBoolean(ALLOW_ITEM_USE_PROPERTY)) return;

		// Check if this is called via Item.use. If so, crash the game.

		// The calling variant of Item.use[OnBlock|OnEntity].
		// If null, nothing bad happened.
		@Nullable Tuple<? extends Class<?>, String> useMethodCaller = STACK_WALKER.walk(s -> s
				.skip(3) // checkSetScreen, setScreen injection, setScreen
				.flatMap(frame -> {
					if (!Item.class.isAssignableFrom(frame.getDeclaringClass())) return Stream.empty();

					return ITEM_USE_METHODS.stream()
						.filter(method -> method.getA().equals(frame.getMethodName()) &&
							method.getB().equals(frame.getMethodType()))
						.map(method -> new Tuple<>(frame.getDeclaringClass(), method.getA()));
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
			CrashReport report = CrashReport.forThrowable(cause, "Opening screen");
			report.addCategory("Screen opening details")
				.setDetail("Screen class", screen.getClass().getName())
				.setDetail("GUI description", () -> cs.getDescription().getClass().getName())
				.setDetail("Item class", () -> useMethodCaller.getA().getName())
				.setDetail("Involved method", useMethodCaller.getB());
			throw new ReportedException(report);
		}
	}
}
