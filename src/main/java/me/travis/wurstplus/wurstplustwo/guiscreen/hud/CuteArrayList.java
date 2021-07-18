package me.travis.wurstplus.wurstplustwo.guiscreen.hud;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.pinnables.WurstplusPinnable;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.RainbowUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import me.travis.wurstplus.wurstplustwo.util.font.FontUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Comparator;

public class CuteArrayList extends WurstplusPinnable {

    public CuteArrayList() {
        super("CuteArrayList", "CuteArrayList", 1, 50, 50);
    }

    int modCount;

    @Override
    public void render() {

        int red = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
        int green = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
        int blue = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
        int alpha = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);
        boolean rainbow = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDRainbow").get_value(true);

        modCount = 0;
        int[] counter = {1};
        final ScaledResolution resolution = new ScaledResolution(mc);
        //int theoffset = offset.getValue();
        Wurstplus.get_hack_manager().get_array_hacks()
                .stream()
                .filter(WurstplusHack::is_active)
                .sorted(Comparator.comparing(module -> FontUtils.getStringWidth(true, module.get_name()) * (-1)))
                .forEach(m -> {
                    //float huee = astolfoHue.getValue()/100;
                    //Color colorr;
                    //colorr = RainbowUtil.astolfo((theoffset * -modCount), 10, 0.6f, -huee, 1f);
                    int x = resolution.getScaledWidth();
                    boolean changeY = false;
                    boolean changeX = false;
                    if(this.get_y() > (resolution.getScaledHeight() / 2)) {
                        changeY = true;
                    }
                    if(this.get_x() > (resolution.getScaledWidth() / 2)) {
                        changeX = true;
                    }
                    int lWidth = FontUtils.getStringWidth(true, m.get_name());
                    WurstplusRenderUtil.drawRecta((get_x() + 50)  - 5 + (changeX ? - lWidth : - 50), get_y() + (changeY ? 50 + (modCount * -10) : (modCount * 10)), lWidth + 5, FontUtils.getFontHeight(true) + 2, new Color(0, 0, 0, 100).getRGB());
                    FontUtils.drawStringWithShadow(true, m.get_name(), (get_x() + 50)  - 4 + (changeX ? - lWidth : - 50), get_y() + (changeY ? 50 + (modCount * -10) : (modCount * 10)), rainbow ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red, green, blue, alpha).getRGB());
                    WurstplusRenderUtil.drawRecta((get_x() + 50) + (changeX ? -2 : -57), get_y() + (changeY ? 50 + (modCount * -10) : (modCount * 10)), 2, FontUtils.getFontHeight(true) + 2, rainbow ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red, green, blue, alpha).getRGB());
                    modCount++;
                    counter[0]++;
                });

        this.set_width(50);
        this.set_height(50);
    }
}
