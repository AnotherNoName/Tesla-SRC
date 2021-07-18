package me.travis.wurstplus.wurstplustwo.hacks.movement;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class NoFallModule extends WurstplusHack {
    public NoFallModule() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);

        this.name        = "NoFallModule";
        this.tag         = "NoFallModule";
        this.description = "NoFallModule baby!";
    }

    WurstplusSetting mode = create("Mode", "Modes", "Packet", combobox("None", "Packet", "Anti", "Bucket"));
    WurstplusSetting NoVoid = create("NoVoid", "NoVoid", false);

    private boolean SendElytraPacket = false;
    private boolean SendInvPackets = false;
    private int ElytraSlot = -1;

    @Override
    public void enable()
    {
        SendElytraPacket = false;
        SendInvPackets = false;
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }

    @Override
    public void disable() {
        WurstplusEventBus.EVENT_BUS.unsubscribe(this);
    }

    @EventHandler
    private Listener<WurstplusEventMotionUpdate> OnPlayerUpdate = new Listener<>(p_Event ->
    {
        if (p_Event.stage != 0)
            return;

        if (NoVoid.get_value(true))
        {
            if (mc.player.posY <= 5.0f)
            {
                final RayTraceResult l_Trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0, mc.player.posZ), false, false, false);

                if (l_Trace == null || l_Trace.typeOfHit != RayTraceResult.Type.BLOCK)
                {
                    mc.player.setVelocity(0, 0, 0);
                }
            }
        }

        if (mc.player.fallDistance >= 3.0f)
        {
            int l_CollisionHeight = -1;

            int l_DistanceCheck = mode.in("Bucket") ? 5 : 8;

            for (int l_I = (int) mc.player.posY; l_I > mc.player.posY - l_DistanceCheck; --l_I)
            {
                if (mc.world.isAirBlock(new BlockPos(mc.player.posX, l_I, mc.player.posZ)))
                    continue;

                if (mc.world.getBlockState(new BlockPos(mc.player.posX, l_I, mc.player.posZ)).getMaterial() == Material.WATER)
                    continue;

                l_CollisionHeight = l_I;
                break;
                // getBlockState(pos).getBlock().isAir(this.getBlockState(pos), this, pos);
            }

            if (l_CollisionHeight != -1)
            {
                if (mode.in("Bucket"))
                {
                    if (mc.player.getHeldItemMainhand().getItem() != Items.WATER_BUCKET)
                    {
                        for (int l_I = 0; l_I < 9; ++l_I)
                        {
                            if (mc.player.inventory.getStackInSlot(l_I).getItem() == Items.WATER_BUCKET)
                            {
                                mc.player.inventory.currentItem = l_I;
                                mc.playerController.updateController();
                                break;
                            }
                        }
                    }

                    mc.player.rotationPitch = 90;
                    mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                }
            }
        }
        else
        {
            SendInvPackets = false;
            SendElytraPacket = false;
        }
    });

    @EventHandler
    private Listener<WurstplusEventPacket.SendPacket> send_listener = new Listener<>(p_Event -> {
        if (p_Event.get_packet() instanceof CPacketPlayer)
        {
            if (mc.player.isElytraFlying())
                return;

            final CPacketPlayer packet = (CPacketPlayer) p_Event.get_packet();

            if(mode.in("Packet")) {
                if (mc.player.fallDistance > 3.0f)
                    packet.onGround = true;
            } else if(mode.in("Anti")) {
                if (mc.player.fallDistance > 3.0f)
                    packet.y = mc.player.posY + 0.1f;
            }
        }
    });
}
