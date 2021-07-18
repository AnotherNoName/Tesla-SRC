package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPlayerJump;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Mod;

public class Scaffold extends WurstplusHack {
    public Scaffold() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name        = "Scaffold";
        this.tag         = "Scaffold";
        this.description = "Scaffold";
    }

    private final WurstplusTimer timer = new WurstplusTimer();

    WurstplusSetting rotation = create("Rotate", "Rotate", false);

    @Override
    public void enable() {
        this.timer.reset();
    }

    @EventHandler
    private Listener<WurstplusEventMotionUpdate> onMove = new Listener<>(event -> {
        BlockPos playerBlock;
        if ((mc.player == null || mc.world == null)) {
            return;
        }
        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            this.timer.reset();
        }
        if (WurstplusBlockUtil.isScaffoldPos((playerBlock = WurstplusEntityUtil.getPlayerPosWithEntity()).add(0, -1, 0))) {
            if (WurstplusBlockUtil.isValidBlock(playerBlock.add(0, -2, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.UP);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(1, -1, 0))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(0, -1, -1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                this.place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (WurstplusBlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(-1, -1, 1))) {
                if (WurstplusBlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
                }
                this.place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (WurstplusBlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
            } else if (WurstplusBlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                if (WurstplusBlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    this.place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
                }
                this.place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
            }
        }
    });

    public void place(BlockPos posI, EnumFacing face) {
        Block block;
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        } else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        } else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        } else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        } else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        int oldSlot = mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (InventoryUtil.isNull(stack) || !(stack.getItem() instanceof ItemBlock) || !Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock())
                continue;
            newSlot = i;
            break;
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        if (!mc.player.isSneaking() && WurstplusBlockUtil.emptyBlocks.contains(block = mc.world.getBlockState(pos).getBlock())) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            crouched = true;
        }
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(newSlot));
            mc.player.inventory.currentItem = newSlot;
            mc.playerController.updateController();
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionX *= 0.3;
            mc.player.motionZ *= 0.3;
            mc.player.jump();
            if (this.timer.passed(1500L)) {
                mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        if (this.rotation.get_value(true)) {
            float[] angle = WurstplusMathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], (float) MathHelper.normalizeAngle((int) angle[1], 360), mc.player.onGround));
        }
        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        mc.player.inventory.currentItem = oldSlot;
        mc.playerController.updateController();
        if (crouched) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
