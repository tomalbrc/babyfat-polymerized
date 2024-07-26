package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.blocks.WaterLettuceBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BFBlocks {
	public static final Block WATER_LETTUCE = new WaterLettuceBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.LILY_PAD).noOcclusion());

	public static void register() {
		Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(BabyFat.MOD_ID, "water_lettuce"), WATER_LETTUCE);
	}
}
