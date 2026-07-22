package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

    private int previousSlot = -1;

    public Scaffold() {
        super("Scaffold", "Places blocks under you as you walk", Category.PLAYER);
    }

    @Override
    public void onDisable() {
        restoreSlot();
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.playerController == null) {
            return;
        }
        BlockPos below = new BlockPos(mc.player.posX, mc.player.posY - 1.0, mc.player.posZ);
        if (!mc.world.isAirBlock(below)) {
            restoreSlot();
            return;
        }
        BlockPos support = findSupport(below);
        if (support == null) {
            restoreSlot();
            return;
        }
        int slot = findBlockSlot();
        if (slot == -1) {
            restoreSlot();
            return;
        }
        if (previousSlot == -1) {
            previousSlot = mc.player.inventory.currentItem;
        }
        mc.player.inventory.currentItem = slot;
        EnumFacing side = directionTo(support, below);
        Vec3d hitVec = new Vec3d(
                support.getX() + 0.5 + side.getFrontOffsetX() * 0.5,
                support.getY() + 0.5 + side.getFrontOffsetY() * 0.5,
                support.getZ() + 0.5 + side.getFrontOffsetZ() * 0.5);
        mc.playerController.processRightClickBlock(mc.player, mc.world, support, side, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private BlockPos findSupport(BlockPos target) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbour = target.offset(facing);
            if (!mc.world.isAirBlock(neighbour)) {
                return neighbour;
            }
        }
        return null;
    }

    private EnumFacing directionTo(BlockPos from, BlockPos to) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (from.offset(facing).equals(to)) {
                return facing;
            }
        }
        return EnumFacing.UP;
    }

    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    private void restoreSlot() {
        if (previousSlot != -1 && mc.player != null) {
            mc.player.inventory.currentItem = previousSlot;
            previousSlot = -1;
        }
    }
}
