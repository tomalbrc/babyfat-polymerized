package codyhuh.babyfat;

import codyhuh.babyfat.registry.BFBlocks;
import codyhuh.babyfat.registry.BFEntities;
import codyhuh.babyfat.registry.BFItems;
import codyhuh.babyfat.registry.BFTabs;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BabyFat implements ModInitializer {
	public static final String MOD_ID = "babyfat";

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets("babyfat");
		PolymerResourcePackUtils.markAsRequired();

		BFBlocks.register();
		BFItems.register();
		BFEntities.register();
		BFTabs.register();

		var key = ResourceKey.create(Registries.PLACED_FEATURE,
				Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "water_lettuce"));
		BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_SAVANNA), GenerationStep.Decoration.VEGETAL_DECORATION, key);
	}
}
