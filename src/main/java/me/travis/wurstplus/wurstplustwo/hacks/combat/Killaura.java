package me.travis.wurstplus.wurstplustwo.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.util.internal.MathUtil;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMathUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;

public class Killaura extends WurstplusHack {
    public Killaura() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);

        this.name        = "Killaura";
        this.tag         = "Killaura";
        this.description = "Attack enemies";
    }

    boolean rotating;
    public static EntityPlayer target;

    ArrayList<String> aimModes = new ArrayList<>();
    boolean swordOnly = true;
    boolean caCheck = true;
    boolean tpsSync = false;
    boolean isAttacking = false;
    WurstplusSetting range = create("Range", "Range",4.5, 0.0, 10.0);
    WurstplusSetting rotate = create("Rotate", "Rotate", true);

    public void disable() {
        rotating = false;
    }


    public void update() {
        if (mc.player != null || mc.world != null) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player != mc.player) {
                    if (mc.player.getDistance(player) < range.get_value(1)) {
                        if (WurstplusFriendUtil.isFriend(player.getName())) return;
                        if (player.isDead || player.getHealth() > 0) {
                            if (rotating && rotate.get_value(true)) {
                                float[] angle = WurstplusMathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), player.getPositionVector());
                                mc.player.rotationYaw = angle[0];
                                mc.player.rotationPitch = angle[1];
                            }
                            attackPlayer(player);
                        }
                        target = player;
                    } else {
                        rotating = false;
                    }
                }
            }
        }
    }


    public void attackPlayer(EntityPlayer player) {
        if (player != null) {
            if (player != mc.player) {
                if (mc.player.getCooledAttackStrength(0.0f) >= 1) {
                    rotating = true;
                    mc.playerController.attackEntity(mc.player, player);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
        else {
            rotating = false;
        }
    }
}
