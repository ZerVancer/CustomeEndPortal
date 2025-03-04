package net.zervancer.customendportal.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class PortalFrameNode {

    private List<PortalFrameNode> path = new LinkedList<>();

    private BlockPos blockPos;

    public PortalFrameNode(BlockPos blockPos) {
        this.blockPos = blockPos;
        this.path = new ArrayList<>();
    }
    
    public PortalFrameNode(BlockPos blockPos, PortalFrameNode node){
        this.blockPos = blockPos;
        this.path = node.getPath();
        this.path.add(node);
    }

    public boolean hasPassed(BlockPos blockPos){
        if (this.blockPos.equals(blockPos)) { return true; }
        for (PortalFrameNode portalFrameNode : this.path) {
            if (portalFrameNode.getNodePos().equals(blockPos)) { return true; }
        }
        return false;
    }

    public BlockPos getNodePos() { return new BlockPos(blockPos); }

    public List<PortalFrameNode> getPath() { 
        return new LinkedList<PortalFrameNode>(path);
    }
}
