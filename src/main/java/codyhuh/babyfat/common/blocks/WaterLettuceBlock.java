package codyhuh.babyfat.common.blocks;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.registry.BFBlocks;
import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import xyz.nucleoid.packettweaker.PacketContext;

public class WaterLettuceBlock extends VegetationBlock implements BonemealableBlock, PolymerTexturedBlock {
    private final BlockState polymerBlockState;

	public static final MapCodec<WaterLettuceBlock> CODEC = simpleCodec(WaterLettuceBlock::new);


	public WaterLettuceBlock(BlockBehaviour.Properties properties) {
		super(properties);
        PolymerBlockModel blockModel = PolymerBlockModel.of(ResourceLocation.fromNamespaceAndPath(BabyFat.MOD_ID, "block/water_lettuce"));
		this.polymerBlockState = PolymerBlockResourceUtils.requestBlock(BlockModelType.PLANT_BLOCK, blockModel);
	}

	@Override
	@NotNull
	public MapCodec<? extends VegetationBlock> codec() {
		return CODEC;
	}

	protected boolean mayPlaceOn(BlockState blockState, BlockGetter level, BlockPos pos) {
		FluidState fluidstate = level.getFluidState(pos);
		FluidState fluidstate1 = level.getFluidState(pos.above());
		return (fluidstate.getType() == Fluids.WATER || blockState.is(BlockTags.ICE)) && fluidstate1.getType() == Fluids.EMPTY;
	}


	@Override
	public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		return true;
	}

	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState blockState) {
		return true;
	}

	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState) {
		boolean flag = false;
		boolean flag1 = false;

		for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
			BlockState blockstate = level.getBlockState(blockpos);

			if(blockstate.isAir() && BFBlocks.WATER_LETTUCE.defaultBlockState().canSurvive(level, blockpos)){
				if(!flag1 && random.nextBoolean()){
					level.setBlock(blockpos, BFBlocks.WATER_LETTUCE.defaultBlockState(), 3);
					flag1 = true;
					continue;
				}

				if(!flag && random.nextBoolean()){
					level.setBlock(blockpos, BFBlocks.WATER_LETTUCE.defaultBlockState(), 3);
					flag = true;
					continue;
				}

				if(flag && flag1){
					break;
				}
			}
		}
	}

	@Override
	public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
		return this.polymerBlockState;
	}
}