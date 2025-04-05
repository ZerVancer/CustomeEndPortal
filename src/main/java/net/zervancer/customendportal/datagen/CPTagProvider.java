package net.zervancer.customendportal.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class CPTagProvider extends BlockTagProvider {

    public CPTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public void configure(WrapperLookup registries) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(Blocks.END_PORTAL_FRAME);
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(Blocks.END_PORTAL_FRAME);
    }
}
