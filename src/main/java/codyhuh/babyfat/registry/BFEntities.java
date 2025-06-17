package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.entities.Ranchu;
import codyhuh.babyfat.util.BiomeHelper;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;

public class BFEntities {

	public static final EntityType<Ranchu> RANCHU = register(ResourceLocation.fromNamespaceAndPath(BabyFat.MOD_ID, "ranchu"), FabricEntityType.Builder.createMob(Ranchu::new, MobCategory.WATER_CREATURE, x -> x
            .spawnRestriction(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING, Ranchu::checkFishSpawnRules)
            .defaultAttributes(Ranchu::createAttributes)
	).sized(0.5f, 0.5f));

	public static void register() {
		BiomeHelper.addSpawn(RANCHU, 30, 2, 4, BiomeSelectors.tag(BiomeTags.IS_RIVER));
	}

	private static <T extends Entity> EntityType<T> register(ResourceLocation id, EntityType.Builder<T> builder) {
		EntityType<T> type = builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Ranchu.ID));
		PolymerEntityUtils.registerType(type);
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);
	}
}
