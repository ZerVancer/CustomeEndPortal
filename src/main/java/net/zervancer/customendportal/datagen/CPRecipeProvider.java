package net.zervancer.customendportal.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CPRecipeProvider extends FabricRecipeProvider {
    public CPRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {        
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Blocks.END_PORTAL_FRAME)
            .pattern("P P")
            .pattern("ECE")
            .pattern("EEE")
            .input('C', Items.END_CRYSTAL)
            .input('P', Blocks.DARK_PRISMARINE)
            .input('E', Blocks.END_STONE)
            .criterion(hasItem(Items.END_CRYSTAL), conditionsFromItem(Items.END_CRYSTAL))
            .criterion(hasItem(Blocks.DARK_PRISMARINE), conditionsFromItem(Blocks.DARK_PRISMARINE))
            .criterion(hasItem(Blocks.END_STONE), conditionsFromItem(Blocks.END_STONE))
            .offerTo(exporter);
    }
}