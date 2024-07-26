package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.items.PolymerPlaceOnWaterBlockItem;
import codyhuh.babyfat.common.items.RanchuBucketItem;
import eu.pb4.polymer.core.api.item.PolymerSpawnEggItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class BFItems {
	public static final Item WATER_LETTUCE = new PolymerPlaceOnWaterBlockItem(BFBlocks.WATER_LETTUCE, new Item.Properties(), Items.PAPER, ResourceLocation.tryBuild(BabyFat.MOD_ID, "item/water_lettuce"));
	public static final Item RANCHU_BUCKET = new RanchuBucketItem(BFEntities.RANCHU, Fluids.WATER, new Item.Properties().stacksTo(1));
	public static final Item RANCHU_SPAWN_EGG = new PolymerSpawnEggItem(BFEntities.RANCHU, Items.PARROT_SPAWN_EGG, new Item.Properties());

	public static void register() {
		register(WATER_LETTUCE, ResourceLocation.tryBuild(BabyFat.MOD_ID, "water_lettuce"));
		register(RANCHU_BUCKET, ResourceLocation.tryBuild(BabyFat.MOD_ID, "ranchu_bucket"));
		register(RANCHU_SPAWN_EGG, ResourceLocation.tryBuild(BabyFat.MOD_ID, "ranchu_spawn_egg"));
	}

	static public void register(Item item, ResourceLocation identifier) {
		Registry.register(BuiltInRegistries.ITEM, identifier, item);
		BFTabs.addItem(item);
	}
}
