package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.items.PolymerPlaceOnWaterBlockItem;
import codyhuh.babyfat.common.items.RanchuBucketItem;
import codyhuh.babyfat.common.items.VanillaPolymerSpawnEggItem;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

public class BFItems {
	public static final Item WATER_LETTUCE = registerItem(
			Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "water_lettuce"),
			x -> new PolymerPlaceOnWaterBlockItem(BFBlocks.WATER_LETTUCE, x, Items.PAPER, Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "water_lettuce")),
			new Item.Properties()
	);

	public static final Item RANCHU_BUCKET = registerItem(
			Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "ranchu_bucket"),
			x -> new RanchuBucketItem(BFEntities.RANCHU, Fluids.WATER, x),
			new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
	);

	public static final Item RANCHU_SPAWN_EGG = registerItem(
			Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "ranchu_spawn_egg"),
			x -> new VanillaPolymerSpawnEggItem(BFEntities.RANCHU, Items.PARROT_SPAWN_EGG, x),
			new Item.Properties()
	);

	public static void register() {
		BFTabs.addItem(WATER_LETTUCE);
		BFTabs.addItem(RANCHU_BUCKET);
		BFTabs.addItem(RANCHU_SPAWN_EGG);
	}

	public static <T extends Item> T registerItem(Identifier identifier, Function<Item.Properties, T> function, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
		T item = function.apply(properties.setId(key));
		Registry.register(BuiltInRegistries.ITEM, key, item);
		return item;
	}
}
