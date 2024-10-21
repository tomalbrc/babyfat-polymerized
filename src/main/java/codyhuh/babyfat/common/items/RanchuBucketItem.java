package codyhuh.babyfat.common.items;

import codyhuh.babyfat.BabyFat;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluid;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class RanchuBucketItem extends MobBucketItem implements PolymerItem {
	private final ResourceLocation modelData;

	public RanchuBucketItem(EntityType<?> entityType, Fluid fluid, Item.Properties builder) {
		super(entityType, fluid, SoundEvents.BUCKET_EMPTY_FISH, builder);
		this.modelData = ResourceLocation.fromNamespaceAndPath(BabyFat.MOD_ID, "ranchu_bucket");
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
		if (itemStack.has(DataComponents.BUCKET_ENTITY_DATA)) {
			Component domesticated = Component.translatable("tooltip.babyfat.domesticated").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
			list.add(domesticated);
		}
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
		return Items.WATER_BUCKET;
	}

	@Override
	public ResourceLocation getPolymerItemModel(ItemStack itemStack, PacketContext context) {
		return this.modelData;
	}
}

