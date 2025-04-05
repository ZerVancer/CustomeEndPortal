package net.zervancer.customendportal;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.zervancer.customendportal.datagen.CPLootTableProvider;
import net.zervancer.customendportal.datagen.CPRecipeProvider;
import net.zervancer.customendportal.datagen.CPTagProvider;

public class CustomEndPortalDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack myPack = fabricDataGenerator.createPack();

    	myPack.addProvider(CPLootTableProvider::new);
		myPack.addProvider(CPRecipeProvider::new);
		myPack.addProvider(CPTagProvider::new);
	}
}
