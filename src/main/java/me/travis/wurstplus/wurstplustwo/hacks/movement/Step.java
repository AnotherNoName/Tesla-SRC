package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.MotionUtils;
import net.minecraft.network.play.client.CPacketPlayer;

import java.text.DecimalFormat;

public class Step extends WurstplusHack {
    public Step() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name        = "Step";
        this.tag         = "Step";
        this.description = "Up and down ez";
    }

    WurstplusSetting height = create("Height", "Height", 2.5, 0.5, 2.5);
    WurstplusSetting reverse = create("Reverse", "Reverse", false);
    WurstplusSetting mode = create("Mode", "Modes", "Normal", combobox("Vanilla", "Normal"));

    public void update() {
        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder()
                || mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }

        if (mode.in("Normal")) {
            if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder() && this.reverse.get_value(true)) {
                for (double y = 0.0; y < this.height.get_value(1) + 0.5; y += 0.01) {
                    if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                        mc.player.motionY = -10.0;
                        break;
                    }
                }
            }
            double[] dir = MotionUtils.forward(0.1);
            boolean twofive = false;
            boolean two = false;
            boolean onefive = false;
            boolean one = false;
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.6, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.4, dir[1])).isEmpty()) {
                twofive = true;
            }
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.1, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.9, dir[1])).isEmpty()) {
                two = true;
            }
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.6, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.4, dir[1])).isEmpty()) {
                onefive = true;
            }
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.0, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 0.6, dir[1])).isEmpty()) {
                one = true;
            }
            if (mc.player.collidedHorizontally && (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                if (one && this.height.get_value(1) >= 1.0) {
                    final double[] oneOffset = {0.42, 0.753};
                    for (int i = 0; i < oneOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
                }
                if (onefive && this.height.get_value(1) >= 1.5) {
                    final double[] oneFiveOffset = {0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
                    for (int i = 0; i < oneFiveOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneFiveOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1.5, mc.player.posZ);
                }
                if (two && this.height.get_value(1) >= 2.0) {
                    final double[] twoOffset = {0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
                    for (int i = 0; i < twoOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
                }
                if (twofive && this.height.get_value(1) >= 2.5) {
                    final double[] twoFiveOffset = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
                    for (int i = 0; i < twoFiveOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoFiveOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2.5, mc.player.posZ);
                }
            }
        }
        if (mode.in("Vanilla")) {
            DecimalFormat df = new DecimalFormat("#");
            mc.player.stepHeight = Float.parseFloat(df.format(height.get_value(1)));
        }
    }
}
