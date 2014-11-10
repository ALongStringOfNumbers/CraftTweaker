package minetweaker.mods.gregtech.machines;

import gregtech.api.GregTech_API;
import static gregtech.api.GregTech_API.MOD_ID;
import minetweaker.annotations.ModOnly;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.action.OneWayAction;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenMethod;

/**
 * Provides access to the extruder recipes.
 * 
 * @author Stan Hebben
 */
@ZenClass("mods.gregtech.Extruder")
@ModOnly(MOD_ID)
public class Extruder {
	/**
	 * Adds an extruder recipe.
	 * 
	 * @param output recipe output
	 * @param input recipe input
	 * @param shape shape (set stack size to 0 to prevent the shape from being consumed)
	 * @param durationTicks extruding time, in ticks
	 * @param euPerTick eu consumption per tick
	 */
	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, IItemStack shape, int durationTicks, int euPerTick) {
		MineTweakerAPI.apply(new AddRecipeAction(output, input, shape, durationTicks, euPerTick));
	}
	
	// ######################
	// ### Action classes ###
	// ######################
	
	private static class AddRecipeAction extends OneWayAction {
		private final IItemStack output;
		private final IItemStack input;
		private final IItemStack shape;
		private final int duration;
		private final int euPerTick;
		
		public AddRecipeAction(IItemStack output, IItemStack input, IItemStack shape, int duration, int euPerTick) {
			this.output = output;
			this.input = input;
			this.shape = shape;
			this.duration = duration;
			this.euPerTick = euPerTick;
		}

		@Override
		public void apply() {
			GregTech_API.sRecipeAdder.addExtruderRecipe(
					MineTweakerMC.getItemStack(input),
					MineTweakerMC.getItemStack(shape),
					MineTweakerMC.getItemStack(output),
					duration,
					euPerTick);
		}

		@Override
		public String describe() {
			return "Adding extruder recipe for " + output;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 67 * hash + (this.output != null ? this.output.hashCode() : 0);
			hash = 67 * hash + (this.input != null ? this.input.hashCode() : 0);
			hash = 67 * hash + (this.shape != null ? this.shape.hashCode() : 0);
			hash = 67 * hash + this.duration;
			hash = 67 * hash + this.euPerTick;
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final AddRecipeAction other = (AddRecipeAction) obj;
			if (this.output != other.output && (this.output == null || !this.output.equals(other.output))) {
				return false;
			}
			if (this.input != other.input && (this.input == null || !this.input.equals(other.input))) {
				return false;
			}
			if (this.shape != other.shape && (this.shape == null || !this.shape.equals(other.shape))) {
				return false;
			}
			if (this.duration != other.duration) {
				return false;
			}
			if (this.euPerTick != other.euPerTick) {
				return false;
			}
			return true;
		}
	}
}
