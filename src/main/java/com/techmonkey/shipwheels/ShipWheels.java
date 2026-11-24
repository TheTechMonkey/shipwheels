package com.techmonkey.shipwheels;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

public class ShipWheels implements ModInitializer {
    public static final String MOD_ID = "shipwheels";

    /** Every vanilla wood a wheel can be made from (named after its planks). */
    public static final String[] WOODS = {
            "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "pale_oak",
            "poplar", "bamboo", "crimson", "warped"
    };

    public static final List<ShipWheelBlock> WHEELS = new ArrayList<>();
    public static final List<BlockItem> WHEEL_ITEMS = new ArrayList<>();

    static {
        for (String wood : WOODS) {
            String name = wood + "_ship_wheel";
            ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id(name));
            ShipWheelBlock block = Registry.register(BuiltInRegistries.BLOCK, blockKey, new ShipWheelBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.5F)
                    .sound(wood.equals("crimson") || wood.equals("warped") ? SoundType.NETHER_WOOD : SoundType.WOOD)
                    .noOcclusion()
                    .setId(blockKey)));
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id(name));
            BlockItem item = Registry.register(BuiltInRegistries.ITEM, itemKey,
                    new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
            WHEELS.add(block);
            WHEEL_ITEMS.add(item);
        }
    }

    public static final BlockEntityType<ShipWheelBlockEntity> WHEEL_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, id("ship_wheel"),
            new BlockEntityType<>(ShipWheelBlockEntity::new, java.util.Set.copyOf(WHEELS)));

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("ship_wheels"),
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.shipwheels.ship_wheels"))
                        .icon(() -> new ItemStack(WHEEL_ITEMS.get(5)))
                        .displayItems((context, output) -> WHEEL_ITEMS.forEach(output::accept))
                        .build());
    }
}
