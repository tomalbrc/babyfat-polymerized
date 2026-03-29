package codyhuh.babyfat.common.items;

import codyhuh.babyfat.BabyFat;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RanchuBucketItem extends MobBucketItem implements PolymerItem {
	private final Identifier modelData;

	public RanchuBucketItem(EntityType<? extends Mob> entityType, Fluid fluid, Item.Properties builder) {
		super(entityType, fluid, SoundEvents.BUCKET_EMPTY_FISH, builder);
		this.modelData = Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "ranchu_bucket");
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> consumer, @NotNull TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
		if (itemStack.has(DataComponents.BUCKET_ENTITY_DATA)) {
			Component domesticated = Component.translatable("tooltip.babyfat.domesticated").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
			consumer.accept(domesticated);
		}
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
		return Items.WATER_BUCKET;
	}

	@Override
	public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context, HolderLookup.Provider provider) {
		return this.modelData;
	}
}

