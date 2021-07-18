package me.travis.wurstplus.wurstplustwo.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.RainbowUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import me.travis.wurstplus.wurstplustwo.util.font.FontUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ModuleArrayList extends WurstplusHack {
    public ModuleArrayList() {
        super(WurstplusCategory.WURSTPLUS_RENDER);
        this.name = "ArrayList";
        this.tag = "ArrayList";
        this.description = "Render ArrayList";

    }

    WurstplusSetting rainbow = create("Rainbow", "Rainbow", false);
    WurstplusSetting red = create("Red", "Red", 255, 0, 255);
    WurstplusSetting green = create("Green", "Green",  255, 0, 255);
    WurstplusSetting blue = create("Blue", "Blue", 255, 0, 255);

    int modCount;

    public void render() {

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
                    int lWidth = FontUtils.getStringWidth(true, m.get_name());
                    WurstplusRenderUtil.drawRecta(x - 5 - lWidth, 1 +(modCount * 10), lWidth + 5, FontUtils.getFontHeight(true) + 2, new Color(0, 0, 0, 100).getRGB());
                    FontUtils.drawStringWithShadow(true, m.get_name(), x - 4 - lWidth, 2 + (modCount * 10), rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
                    WurstplusRenderUtil.drawRecta(x - 2, 1 +(modCount * 10), 2, FontUtils.getFontHeight(true) + 2, rainbow.get_value(true) ? RainbowUtil.rainbow(counter[0] * 100) : new Color(red.get_value(1), green.get_value(1), blue.get_value(1), 255).getRGB());
                    modCount++;
                    counter[0]++;
                });
    }
}
