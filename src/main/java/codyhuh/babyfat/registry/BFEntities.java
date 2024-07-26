package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.entities.Ranchu;
import codyhuh.babyfat.util.BiomeHelper;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;

public class BFEntities {
	public static final EntityType<Ranchu> RANCHU = register(
			Ranchu.ID,
			FabricEntityTypeBuilder.createMob()
					.entityFactory(Ranchu::new)
					.spawnGroup(MobCategory.WATER_CREATURE)
					.dimensions(EntityDimensions.scalable(0.5f, 0.5f))
					.defaultAttributes(Ranchu::createAttributes)
					.spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Ranchu::checkFishSpawnRules)
	);

	public static void register() {
		BiomeHelper.addSpawn(RANCHU, 30, 2, 4, BiomeSelectors.tag(BiomeTags.IS_RIVER));
	}

	private static <T extends Entity> EntityType<T> register(ResourceLocation id, FabricEntityTypeBuilder builder) {
		EntityType<T> type = builder.build();
		PolymerEntityUtils.registerType(type);
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);
	}
}
