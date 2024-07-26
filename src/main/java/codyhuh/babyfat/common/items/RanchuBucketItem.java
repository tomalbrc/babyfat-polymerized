package codyhuh.babyfat.common.items;

import codyhuh.babyfat.BabyFat;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RanchuBucketItem extends MobBucketItem implements PolymerItem {
	private final PolymerModelData modelData;

	public RanchuBucketItem(EntityType<?> entityType, Fluid fluid, Item.Properties builder) {
		super(entityType, fluid, SoundEvents.BUCKET_EMPTY_FISH, builder);
		this.modelData = PolymerResourcePackUtils.requestModel(Items.WATER_BUCKET, ResourceLocation.tryBuild(BabyFat.MOD_ID, "item/ranchu_bucket"));
	}

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, level, list, tooltipFlag);
		if (itemStack.hasTag()) {
			Component domesticated = Component.translatable("tooltip.babyfat.domesticated").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
			list.add(domesticated);
		}
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
		return this.modelData.item();
	}

	@Override
	public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayer player) {
		return this.modelData.value();
	}
}

