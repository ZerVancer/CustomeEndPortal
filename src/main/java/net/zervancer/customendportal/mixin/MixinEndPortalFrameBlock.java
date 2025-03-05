package net.zervancer.customendportal.mixin;

import java.util.Stack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;



@Mixin(EndPortalFrameBlock.class)
public class MixinEndPortalFrameBlock extends Block {

    public MixinEndPortalFrameBlock(Settings settings) {
        super(settings);
    }

    @ModifyArg(method="<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings endPortalFrameSettings(AbstractBlock.Settings settings) {
        return settings.hardness(25.0f).sounds(BlockSoundGroup.STONE);
    }
    
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        
        super.onStateReplaced(state, world, pos, newState, moved);

        if (newState.isAir()) {
            Stack<BlockPos> stack = new Stack<>();
            stack.add(pos.offset(Direction.NORTH));
            stack.add(pos.offset(Direction.EAST));
            stack.add(pos.offset(Direction.WEST));
            stack.add(pos.offset(Direction.SOUTH));
            BlockPos blockPos;

            while (!stack.isEmpty()) {
                blockPos = stack.pop();
                
                if (world.getBlockState(blockPos).isOf(Blocks.END_PORTAL)) {
                    world.setBlockState(blockPos, newState);
                    stack.add(blockPos.offset(Direction.NORTH));
                    stack.add(blockPos.offset(Direction.EAST));
                    stack.add(blockPos.offset(Direction.WEST));
                    stack.add(blockPos.offset(Direction.SOUTH));
                }
            }
        }
    }

}
