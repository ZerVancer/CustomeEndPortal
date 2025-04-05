package net.zervancer.customendportal.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class CPLootTableProvider extends FabricBlockLootTableProvider{
    public CPLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(Blocks.END_PORTAL_FRAME, LootTable.builder()
            .pools(List.of(
                LootPool.builder()
                    .with(ItemEntry.builder(Items.END_STONE))
                    .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(5)))
                    .conditionally(createWithoutSilkTouchCondition())
                    .build(),
                LootPool.builder()
                    .with(ItemEntry.builder(Blocks.END_PORTAL_FRAME))
                    .conditionally(createSilkTouchCondition())
                    .build(),
                LootPool.builder()
                    .with(ItemEntry.builder(Items.ENDER_EYE)
                    .conditionally(BlockStatePropertyLootCondition.builder(Blocks.END_PORTAL_FRAME)
                    .properties(StatePredicate.Builder.create()
                    .exactMatch(EndPortalFrameBlock.EYE, true)
                    )))
                    .build()
            ))
        );
    }
    
}
