package net.zervancer.customendportal.mixin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.zervancer.customendportal.util.PortalFrameNode;

@Mixin(EnderEyeItem.class)
public abstract class MixinEnderEyeItem {

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/EndPortalFrameBlock;getCompletedFramePattern()Lnet/minecraft/block/pattern/BlockPattern;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void endPortal(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, World world, BlockPos blockPos){
        ArrayList<BlockPos> endPortalframeChain = getCompleteFrame(blockPos, world);

        if (!endPortalframeChain.isEmpty()) {
            for (BlockPos blockPos2 : endPortalframeChain) {
                world.setBlockState(blockPos2, Blocks.END_PORTAL.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            world.syncGlobalEvent(WorldEvents.END_PORTAL_OPENED, blockPos.add(1, 0, 1), 0);
        }

        cir.setReturnValue(ActionResult.CONSUME);
    }

    public ArrayList<BlockPos> getCompleteFrame(BlockPos startPos, World world) {
        Deque<PortalFrameNode> pendingNodes = new ArrayDeque<>();
        PortalFrameNode node = new PortalFrameNode(startPos);
        pendingNodes.add(node);

        while (!pendingNodes.isEmpty()) {
            node = pendingNodes.poll();
            BlockPos tmp = node.getNodePos();
            for (int z = tmp.getZ()-1; z <= tmp.getZ()+1; z++) {
                for (int x = tmp.getX()-1; x <= tmp.getX()+1; x++) {
                    BlockPos blockPos = new BlockPos(x, tmp.getY(), z);
                    BlockState blockFocus = world.getBlockState(blockPos);
                    if (startPos.equals(blockPos) && node.getPath().size() > 2) {
                        ArrayList<BlockPos> completeMatrix = getPortal(node, world);
                        if (!completeMatrix.isEmpty()) { return completeMatrix; }
                    } else if (blockFocus.isOf(Blocks.END_PORTAL_FRAME) && (Boolean)blockFocus.get(EndPortalFrameBlock.EYE) && !node.hasPassed(blockPos)) {
                        PortalFrameNode pendingNode = new PortalFrameNode(blockPos, node);
                        pendingNodes.add(pendingNode);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<BlockPos> getPortal(PortalFrameNode node, World world) {
        ArrayList<BlockPos> result = new ArrayList<>();
        Deque<BlockPos> pendingPos = new ArrayDeque<>();
        BlockPos blockPos = insideFrames(node);
        pendingPos.add(blockPos);

        while (!pendingPos.isEmpty()) {
            blockPos = pendingPos.poll();
            if (result.contains(blockPos)) { continue; }
            if (world.getBlockState(blockPos).isAir()) {
                result.add(blockPos);
                pendingPos.add(blockPos.offset(Direction.NORTH));
                pendingPos.add(blockPos.offset(Direction.EAST));
                pendingPos.add(blockPos.offset(Direction.WEST));
                pendingPos.add(blockPos.offset(Direction.SOUTH));
            } else if (!node.hasPassed(blockPos)) {
                return new ArrayList<>();
            }
        }

        return result;
    }

    public BlockPos insideFrames(PortalFrameNode node) {
        List<PortalFrameNode> path = node.getPath();
        int minZ = Integer.MAX_VALUE;
        BlockPos save = null;
        path.add(node);

        for (PortalFrameNode node2 : path) {
            BlockPos blockPos = node2.getNodePos();
            if (blockPos.getZ() < minZ) { 
                minZ = blockPos.getZ();
                save = node2.getNodePos();
            } 
        }

        return save.offset(Direction.SOUTH);
    }
    
}
