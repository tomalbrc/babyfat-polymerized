package codyhuh.babyfat.registry;

import codyhuh.babyfat.BabyFat;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class BFTabs {
    private static ObjectArrayList<Item> CAT= new ObjectArrayList<>();

    public static final CreativeModeTab BF_TAB = CreativeModeTab.builder(null, -1)
            .title(Component.translatable("itemGroup." + BabyFat.MOD_ID))
            .icon(BFItems.RANCHU_BUCKET::getDefaultInstance)
            .displayItems((displayParams, output) -> {
                for (var item : CAT) {
                    output.accept(item);
                }
            })
            .build();

    public static void addItem(Item item) {
        CAT.add(item);
    }

    public static void register() {
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(Identifier.fromNamespaceAndPath(BabyFat.MOD_ID, "items"), BF_TAB);
    }
}