package io.github.cottonmc.cotton.gui.widget;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A builder for widget tooltips.
 *
 * @since 3.0.0
 */
@OnlyIn(Dist.CLIENT)
public final class TooltipBuilder {
	final List<FormattedCharSequence> lines = new ArrayList<>();

	int size() {
		return lines.size();
	}

	/**
	 * Adds the lines to this builder.
	 *
	 * @param lines the lines
	 * @return this builder
	 */
	public TooltipBuilder add(Component... lines) {
		for (Component line : lines) {
			this.lines.add(line.getVisualOrderText());
		}

		return this;
	}

	/**
	 * Adds the lines to this builder.
	 *
	 * @param lines the lines
	 * @return this builder
	 */
	public TooltipBuilder add(FormattedCharSequence... lines) {
		Collections.addAll(this.lines, lines);

		return this;
	}
}
