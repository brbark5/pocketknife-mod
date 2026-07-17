package net.brett.pocketknifemod.Item;

import net.brett.pocketknifemod.PocketknifeMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup KNIFE_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(PocketknifeMod.MOD_ID, "knife"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.knife"))
                    .icon(() -> new ItemStack(ModItems.BASIC_KNIFE)).entries((displayContext, entries) -> {
                        entries.add(ModItems.BASIC_KNIFE);
                        entries.add(ModItems.GOLD_KNIFE);


                    }).build());

    public static void registerItemGroups() {
        PocketknifeMod.LOGGER.info("Registering Item Groups for " + PocketknifeMod.MOD_ID);
    }
}
