package io.github.cottonmc.cotton.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.client.NarrationMessages;
import io.github.cottonmc.cotton.gui.impl.client.NinePatchTextureRendererImpl;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import juuxel.libninepatch.NinePatch;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WScrollBar extends WWidget {
	private static final int SCROLLING_SPEED = 4;

	protected Axis axis = Axis.HORIZONTAL;
	protected int value;
	protected int maxValue = 100;
	protected int window = 16;

	protected int anchor = -1;
	protected int anchorValue = -1;
	protected boolean sliding = false;

	/**
	 * Constructs a horizontal scroll bar.
	 */
	public WScrollBar() {
	}

	/**
	 * Constructs a scroll bar with a custom axis.
	 *
	 * @param axis the axis
	 */
	public WScrollBar(Axis axis) {
		this.axis = axis;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void paint(GuiGraphics context, int x, int y, int mouseX, int mouseY) {
		var matrices = context.pose();
		boolean darkMode = shouldRenderInDarkMode();

		Painters.BACKGROUND.paintBackground(context, x, y, this);

		NinePatch<ResourceLocation> painter = (darkMode ? Painters.SCROLL_BAR_DARK : Painters.SCROLL_BAR);

		if (maxValue <= 0) return;

		if (sliding) {
			painter = (darkMode ? Painters.SCROLL_BAR_PRESSED_DARK : Painters.SCROLL_BAR_PRESSED);
		} else if (isWithinBounds(mouseX, mouseY)) {
			painter = (darkMode ? Painters.SCROLL_BAR_HOVERED_DARK : Painters.SCROLL_BAR_HOVERED);
		}

		matrices.pushPose();

		if (axis == Axis.HORIZONTAL) {
			matrices.translate(x + 1 + getHandlePosition(), y + 1, 0);
			painter.draw(NinePatchTextureRendererImpl.INSTANCE, context, getHandleSize(), height - 2);

			if (isFocused()) {
				Painters.FOCUS.draw(NinePatchTextureRendererImpl.INSTANCE, context, getHandleSize(), height - 2);
			}
		} else {
			matrices.translate(x + 1, y + 1 + getHandlePosition(), 0);
			painter.draw(NinePatchTextureRendererImpl.INSTANCE, context, width - 2, getHandleSize());

			if (isFocused()) {
				Painters.FOCUS.draw(NinePatchTextureRendererImpl.INSTANCE, context, width - 2, getHandleSize());
			}
		}

		matrices.popPose();
	}

	@Override
	public boolean canResize() {
		return true;
	}

	@Override
	public boolean canFocus() {
		return true;
	}

	/**
	 * Gets the on-axis size of the scrollbar handle in gui pixels 
	 */
	public int getHandleSize() {
		float percentage = (window>=maxValue) ? 1f : window / (float)maxValue;
		int bar = (axis==Axis.HORIZONTAL) ? width-2 : height-2;
		int result = (int)(percentage*bar);
		if (result<6) result = 6;
		return result;
	}
	
	/**
	 * Gets the number of pixels the scrollbar handle is able to move along its track from one end to the other.
	 */
	public int getMovableDistance() {
		int bar = (axis==Axis.HORIZONTAL) ? width-2 : height-2;
		return bar-getHandleSize();
	}
	
	public int pixelsToValues(int pixels) {
		int bar = getMovableDistance();
		float percent = pixels / (float)bar;
		return (int)(percent*(maxValue-window));
	}

	public int getHandlePosition() {
		float percent = value / (float)Math.max(maxValue-window, 1);
		return (int)(percent * getMovableDistance());
	}
	
	/**
	 * Gets the maximum scroll value achievable; this will typically be the maximum value minus the
	 * window size
	 */
	public int getMaxScrollValue() {
		return maxValue - window;
	}

	protected void adjustSlider(int x, int y) {

		int delta = 0;
		if (axis==Axis.HORIZONTAL) {
			delta = x-anchor;
		} else {
			delta = y-anchor;
		}

		int valueDelta = pixelsToValues(delta);
		int valueNew = anchorValue + valueDelta;

		if (valueNew>getMaxScrollValue()) valueNew = getMaxScrollValue();
		if (valueNew<0) valueNew = 0;
		this.value = valueNew;
	}

	@Override
	public InputResult onMouseDown(int x, int y, int button) {
		//TODO: Clicking before or after the handle should jump instead of scrolling
		requestFocus();

		if (axis==Axis.HORIZONTAL) {
			anchor = x;
			anchorValue = value;
		} else {
			anchor = y;
			anchorValue = value;
		}
		sliding = true;
		return InputResult.PROCESSED;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
		adjustSlider(x, y);
		return InputResult.PROCESSED;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public InputResult onMouseUp(int x, int y, int button) {
		//TODO: Clicking before or after the handle should jump instead of scrolling
		anchor = -1;
		anchorValue = -1;
		sliding = false;
		return InputResult.PROCESSED;
	}

	@Override
	public InputResult onKeyPressed(int ch, int key, int modifiers) {
		WAbstractSlider.Direction direction = axis == Axis.HORIZONTAL
				? WAbstractSlider.Direction.RIGHT
				: WAbstractSlider.Direction.DOWN;

		if (WAbstractSlider.isIncreasingKey(ch, direction)) {
			if (value < getMaxScrollValue()) {
				value++;
			}
			return InputResult.PROCESSED;
		} else if (WAbstractSlider.isDecreasingKey(ch, direction)) {
			if (value > 0) {
				value--;
			}
			return InputResult.PROCESSED;
		}

		return InputResult.IGNORED;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public InputResult onMouseScroll(int x, int y, double amount) {
		setValue(getValue() + (int) -amount * SCROLLING_SPEED);
		return InputResult.PROCESSED;
	}

	public int getValue() {
		return value;
	}

	public WScrollBar setValue(int value) {
		this.value = value;
		checkValue();
		return this;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public WScrollBar setMaxValue(int max) {
		this.maxValue = max;
		checkValue();
		return this;
	}

	public int getWindow() {
		return window;
	}

	public WScrollBar setWindow(int window) {
		this.window = window;
		return this;
	}

	/**
	 * Checks that the current value is in the correct range
	 * and adjusts it if needed.
	 */
	protected void checkValue() {
		if (this.value>maxValue-window) {
			this.value = maxValue-window;
		}
		if (this.value<0) this.value = 0;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addNarrations(NarrationElementOutput builder) {
		builder.add(NarratedElementType.TITLE, NarrationMessages.SCROLL_BAR_TITLE);
		builder.add(NarratedElementType.USAGE, NarrationMessages.SLIDER_USAGE);
	}

	@OnlyIn(Dist.CLIENT)
	static final class Painters {
		static final NinePatch<ResourceLocation> SCROLL_BAR = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/scroll_bar_light.png")).cornerSize(4).cornerUv(0.25f).build();
		static final NinePatch<ResourceLocation> SCROLL_BAR_DARK = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/scroll_bar_dark.png")).cornerSize(4).cornerUv(0.25f).build();
		static final NinePatch<ResourceLocation> SCROLL_BAR_PRESSED = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/scroll_bar_pressed_light.png")).cornerSize(4).cornerUv(0.25f).build();
		static final NinePatch<ResourceLocation> SCROLL_BAR_PRESSED_DARK = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/scroll_bar_pressed_dark.png")).cornerSize(4).cornerUv(0.25f).build();
		static final NinePatch<ResourceLocation> SCROLL_BAR_HOVERED = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/scroll_bar_hovered_light.png")).cornerSize(4).cornerUv(0.25f).build();
		static final NinePatch<ResourceLocation> SCROLL_BAR_HOVERED_DARK = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/scroll_bar_hovered_dark.png")).cornerSize(4).cornerUv(0.25f).build();
		static final BackgroundPainter BACKGROUND = BackgroundPainter.createLightDarkVariants(
				createNinePatch(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/background_light.png")),
				createNinePatch(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/background_dark.png"))
		);
		static final NinePatch<ResourceLocation> FOCUS = NinePatch.builder(new ResourceLocation(LibGuiCommon.MOD_ID, "textures/widget/scroll_bar/focus.png")).cornerSize(4).cornerUv(0.25f).build();
	}
}
