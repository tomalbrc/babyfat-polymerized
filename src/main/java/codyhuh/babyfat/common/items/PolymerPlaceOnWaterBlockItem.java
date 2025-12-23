package codyhuh.babyfat.common.items;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.Block;
import xyz.nucleoid.packettweaker.PacketContext;

public class PolymerPlaceOnWaterBlockItem extends PlaceOnWaterBlockItem implements PolymerItem {
    private final Identifier modelData;
    private final Item virtualItem;

    public PolymerPlaceOnWaterBlockItem(Block block, Properties settings, Item virtualItem, Identifier modelPath) {
        super(block, settings);
        this.modelData = modelPath;
        this.virtualItem = virtualItem;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext packetContext) {
        return this.virtualItem;
    }

    @Override
    public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
        return this.modelData;
    }
}
