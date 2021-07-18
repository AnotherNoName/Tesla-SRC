package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.material.Material;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class NoRender extends WurstplusHack {
    public NoRender() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "NoRender";
        this.tag = "NoRender";
        this.description = "Render NoRender";

        INSTANCE = this;

    }

    public static NoRender INSTANCE;

    public WurstplusSetting armor = create("Armor", "Armor", false);
    WurstplusSetting fire = create("Fire", "Fire", false);
    WurstplusSetting blind = create("Blind", "Blind", false);
    WurstplusSetting nausea = create("Nausea", "Nausea", false);
    WurstplusSetting noOverlay = create("NoOverlay", "NoOverlay", false);
    public WurstplusSetting hurtCam = create("HurtCam", "HurtCam", false);

    public void update(){
        if(blind.get_value(true) && mc.player.isPotionActive(MobEffects.BLINDNESS)) mc.player.removePotionEffect(MobEffects.BLINDNESS);
        if(nausea.get_value(true) && mc.player.isPotionActive(MobEffects.NAUSEA)) mc.player.removePotionEffect(MobEffects.NAUSEA);
    }

    @EventHandler
    public Listener<RenderBlockOverlayEvent> blockOverlayEventListener = new Listener<>(event -> {
        if(fire.get_value(true) && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) event.setCanceled(true);
    });

    // Disable Water and lava fog
    @EventHandler
    private final Listener<EntityViewRenderEvent.FogDensity> fogDensityListener = new Listener<>(event -> {
        if (noOverlay.get_value(true)) {
            if (event.getState().getMaterial().equals(Material.WATER)
                    || event.getState().getMaterial().equals(Material.LAVA)) {
                event.setDensity(0);
                event.setCanceled(true);
            }
        }
    });

    // Disable screen overlays Overlays
    @EventHandler
    private final Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener = new Listener<>(event -> {
        event.setCanceled(true);
    });

    @EventHandler
    private final Listener<RenderGameOverlayEvent> renderGameOverlayEventListener = new Listener<>(event -> {
        if (noOverlay.get_value(true)) {
            if (event.getType().equals(RenderGameOverlayEvent.ElementType.HELMET)){
                event.setCanceled(true);
            }
            if (event.getType().equals(RenderGameOverlayEvent.ElementType.PORTAL)){
                event.setCanceled(true);
            }
        }
    });

    public void enable(){
        WurstplusEventBus.EVENT_BUS.subscribe(this);
    }

    public void disable(){
        WurstplusEventBus.EVENT_BUS.unsubscribe(this);
    }
}
