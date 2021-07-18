package me.travis.wurstplus.wurstplustwo.hacks.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.util.internal.MathUtil;
import me.travis.wurstplus.wurstplustwo.event.events.EventPlayerTravel;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.BadTimer;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMathUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.ArrayList;

public class ElytraFly extends WurstplusHack {

    public ElytraFly() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name        = "ElytraFly";
        this.tag         = "ElytraFly";
        this.description = "ElytraFly";
    }

    WurstplusSetting mode = create("Mode", "Mode", "Normal", combobox("Normal", "Tarzan", "Superior", "Packet", "Control"));
    WurstplusSetting speed = create("Speed", "SpeedEly", 1.82, 0.0, 10.0);
    WurstplusSetting DownSpeed = create("DownSpeed", "DownSpeed", 1.82, 0.0, 10.0);
    WurstplusSetting GlideSpeed = create("GlideSpeed", "GlideSpeed", 1, 0, 10);
    WurstplusSetting UpSpeed = create("UpSpeed", "UpSpeed", 2.0, 0, 10);
    WurstplusSetting Accelerate = create("Accelerate", "Accelerate", true);
    WurstplusSetting vAccelerationTimer = create("Timer", "Timer", 1000, 0, 10000);
    WurstplusSetting RotationPitch = create("RotationPitch", "RotationPitch", 0.0, -90.0, 90.0);
    WurstplusSetting CancelInWater = create("CancelInWater", "CancelInWater", true);
    WurstplusSetting CancelAtHeight = create("CancelAtHeight", "CancelAtHeight", 5, 0, 10);
    WurstplusSetting InstantFly = create("InstantFly", "InstantFly", false);
    WurstplusSetting EquipElytra = create("EquipElytra", "EquipElytra", false);
    WurstplusSetting PitchSpoof = create("PitchSpoof", "PitchSpoof", false);

    private BadTimer PacketTimer = new BadTimer();
    private BadTimer AccelerationTimer = new BadTimer();
    private BadTimer AccelerationResetTimer = new BadTimer();
    private BadTimer InstantFlyTimer = new BadTimer();
    private boolean SendMessage = false;

    private int ElytraSlot = -1;

    @Override
    public void enable()
    {
        ElytraSlot = -1;

        if (EquipElytra.get_value(true))
        {
            if (mc.player != null && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            {
                for (int l_I = 0; l_I < 44; ++l_I)
                {
                    ItemStack l_Stack = mc.player.inventory.getStackInSlot(l_I);

                    if (l_Stack.isEmpty() || l_Stack.getItem() != Items.ELYTRA)
                        continue;

                    ItemElytra l_Elytra = (ItemElytra)l_Stack.getItem();

                    ElytraSlot = l_I;
                    break;
                }

                if (ElytraSlot != -1)
                {
                    boolean l_HasArmorAtChest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.AIR;

                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);

                    if (l_HasArmorAtChest)
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    @Override
    public void disable()
    {

        if (mc.player == null)
            return;

        if (ElytraSlot != -1)
        {
            boolean l_HasItem = !mc.player.inventory.getStackInSlot(ElytraSlot).isEmpty() || mc.player.inventory.getStackInSlot(ElytraSlot).getItem() != Items.AIR;

            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);

            if (l_HasItem)
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
        }

    }

    @EventHandler
    private Listener<EventPlayerTravel> OnTravel = new Listener<>(p_Event ->
    {
        if (mc.player == null)
            return;

        /// Player must be wearing an elytra.
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            return;

        if (!mc.player.isElytraFlying())
        {
            if (!mc.player.onGround && InstantFly.get_value(true))
            {
                if (!InstantFlyTimer.passed(1000))
                    return;

                InstantFlyTimer.reset();

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }

            return;
        }

        if(mode.in("Normal") || mode.in("Tarzan") || mode.in("Packet")) {
            HandleNormalModeElytra(p_Event);
        } else if(mode.in("Superior")) {
            HandleImmediateModeElytra(p_Event);
        } else if(mode.in("Control")) {
            HandleControlMode(p_Event);
        }
    });

    public void HandleNormalModeElytra(EventPlayerTravel p_Travel)
    {
        double l_YHeight = mc.player.posY;

        if (l_YHeight <= CancelAtHeight.get_value(1))
        {
            if (!SendMessage)
            {
                SendMessage = true;
            }

            return;
        }

        boolean l_IsMoveKeyDown = mc.player.movementInput.moveForward > 0 || mc.player.movementInput.moveStrafe > 0;

        boolean l_CancelInWater = !mc.player.isInWater() && !mc.player.isInLava() && CancelInWater.get_value(true);

        if (mc.player.movementInput.jump)
        {
            p_Travel.cancel();
            Accelerate();
            return;
        }

        if (!l_IsMoveKeyDown)
        {
            AccelerationTimer.resetTimeSkipTo(-vAccelerationTimer.get_max(1));
        }
        else if ((mc.player.rotationPitch <= RotationPitch.get_value(1) || mode.in("Tarzan")) && l_CancelInWater)
        {
            if (Accelerate.get_value(true))
            {
                if (AccelerationTimer.passed(vAccelerationTimer.get_value(1)))
                {
                    Accelerate();
                    return;
                }
            }
            return;
        }

        p_Travel.cancel();
        Accelerate();
    }

    public void HandleImmediateModeElytra(EventPlayerTravel p_Travel)
    {
        if (mc.player.movementInput.jump)
        {
            double l_MotionSq = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);

            if (l_MotionSq > 1.0)
            {
                return;
            }
            else
            {
                double[] dir = WurstplusMathUtil.directionSpeedNoForward(speed.get_value(1));

                mc.player.motionX = dir[0];
                mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);
                mc.player.motionZ = dir[1];
            }

            p_Travel.cancel();
            return;
        }

        mc.player.setVelocity(0, 0, 0);

        p_Travel.cancel();

        double[] dir = WurstplusMathUtil.directionSpeed(speed.get_value(1));

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);
            mc.player.motionZ = dir[1];
        }

        if (mc.player.movementInput.sneak)
            mc.player.motionY = -DownSpeed.get_value(1);

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }

    public void Accelerate()
    {
        if (AccelerationResetTimer.passed(vAccelerationTimer.get_value(1)))
        {
            AccelerationResetTimer.reset();
            AccelerationTimer.reset();
            SendMessage = false;
        }

        float l_Speed = (float)this.speed.get_value(1);

        final double[] dir = WurstplusMathUtil.directionSpeed(l_Speed);

        mc.player.motionY = -(GlideSpeed.get_value(1) / 10000f);

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        if (mc.player.movementInput.sneak)
            mc.player.motionY = -DownSpeed.get_value(1);

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }


    private void HandleControlMode(EventPlayerTravel p_Event)
    {
        final double[] dir = WurstplusMathUtil.directionSpeed(speed.get_value(1));

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];

            mc.player.motionX -= (mc.player.motionX*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionX;
            mc.player.motionZ -= (mc.player.motionZ*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionZ;
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        mc.player.motionY = (-WurstplusMathUtil.degToRad(mc.player.rotationPitch)) * mc.player.movementInput.moveForward;


        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
        p_Event.cancel();
    }

    @EventHandler
    private Listener<WurstplusEventPacket> PacketEvent = new Listener<>(p_Event ->
    {
        if (p_Event.get_packet() instanceof CPacketPlayer && PitchSpoof.get_value(true))
        {
            if (!mc.player.isElytraFlying())
                return;

            if (p_Event.get_packet() instanceof CPacketPlayer.PositionRotation && PitchSpoof.get_value(true))
            {
                CPacketPlayer.PositionRotation rotation = (CPacketPlayer.PositionRotation) p_Event.get_packet();

                mc.getConnection().sendPacket(new CPacketPlayer.Position(rotation.x, rotation.y, rotation.z, rotation.onGround));
                p_Event.cancel();
            }
            else if (p_Event.get_packet() instanceof CPacketPlayer.Rotation && PitchSpoof.get_value(true))
            {
                p_Event.cancel();
            }
        }
    });
}
