package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.blocks.WaterLettuceBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class BFBlocks {
	public static final Block WATER_LETTUCE = registerBlock(ResourceLocation.fromNamespaceAndPath(BabyFat.MOD_ID, "water_lettuce"), WaterLettuceBlock::new, BlockBehaviour.Properties.of().noCollision().instabreak().sound(SoundType.LILY_PAD).noOcclusion());

	public static void register() {
	}

	public static <T extends Block> T registerBlock(ResourceLocation resourceLocation, Function<BlockBehaviour.Properties, T> function, BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, resourceLocation);
		T block = function.apply(properties.setId(key));
		return Registry.register(BuiltInRegistries.BLOCK, key, block);
	}
}
