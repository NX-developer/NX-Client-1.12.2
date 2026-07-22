package com.nx.client.module.modules.player;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class AutoTool extends Module {

    public AutoTool() {
        super("AutoTool", "Picks the best weapon for entities and the best tool for blocks", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.objectMouseOver == null || mc.gameSettings == null) {
            return;
        }
        if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            selectBestWeapon();
            return;
        }
        if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.gameSettings.keyBindAttack.isKeyDown()) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            selectBestTool(mc.world.getBlockState(pos));
        }
    }

    private void selectBestWeapon() {
        int bestSlot = -1;
        double bestScore = 0.0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            double score = 0.0;
            if (stack.getItem() instanceof ItemSword) {
                score = 1000.0 + ((ItemSword) stack.getItem()).getAttackDamage();
            } else if (stack.getItem() instanceof ItemAxe) {
                score = 500.0;
            }
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }
        select(bestSlot);
    }

    private void selectBestTool(IBlockState state) {
        int bestSlot = -1;
        float bestSpeed = 1.0F;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            float speed = stack.getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        select(bestSlot);
    }

    private void select(int slot) {
        if (slot != -1 && slot != mc.player.inventory.currentItem) {
            mc.player.inventory.currentItem = slot;
        }
    }
}
