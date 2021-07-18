package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;

public class BrightnessModule extends WurstplusHack {
    public BrightnessModule() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "Brightness";
        this.tag = "Brightness";
        this.description = "Brightness";
    }

    float old;

    WurstplusSetting Mode = create("Mode", "Mode", "Gamma", combobox("Gamma", "Potion"));

    public void enable(){
        old = mc.gameSettings.gammaSetting;
    }

    public void update(){
        if (Mode.in("Gamma")) {
            mc.gameSettings.gammaSetting = 666f;
            mc.player.removePotionEffect(Potion.getPotionById(16));
        } else if (Mode.in("Potion")) {
            final PotionEffect potionEffect = new PotionEffect(Potion.getPotionById(16), 123456789, 5);
            potionEffect.setPotionDurationMax(true);
            mc.player.addPotionEffect(potionEffect);
        }
    }

    public void disable(){
        mc.gameSettings.gammaSetting = old;
        mc.player.removePotionEffect(Potion.getPotionById(16));
    }
}
