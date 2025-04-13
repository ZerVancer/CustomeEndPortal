package net.zervancer.customendportal.mixin;

import java.util.Stack;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;



@Mixin(EndPortalFrameBlock.class)
public class MixinEndPortalFrameBlock extends Block {

    public MixinEndPortalFrameBlock(Settings settings) {
        super(settings);
    }

    // Not the best implementation to get rid of dropsNothing(), but one that works
    @ModifyArg(method="<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings endPortalFrameSettings(Settings settings) {
        return AbstractBlock.Settings.create()
            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla("end_portal_frame")))
            .mapColor(MapColor.GREEN)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sounds(BlockSoundGroup.STONE)
            .luminance(state -> 1)
            .strength(50F, 3600000.0F)
            .requiresTool();
    }
    
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        
        super.onBroken(world, pos, state);

        Stack<BlockPos> stack = new Stack<>();
        stack.add(pos.offset(Direction.NORTH));
        stack.add(pos.offset(Direction.EAST));
        stack.add(pos.offset(Direction.WEST));
        stack.add(pos.offset(Direction.SOUTH));
        BlockPos blockPos;

        while (!stack.isEmpty()) {
            blockPos = stack.pop();

            if (world.getBlockState(blockPos).isOf(Blocks.END_PORTAL)) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                stack.add(blockPos.offset(Direction.NORTH));
                stack.add(blockPos.offset(Direction.EAST));
                stack.add(blockPos.offset(Direction.WEST));
                stack.add(blockPos.offset(Direction.SOUTH));
            }
        }
    }

}
