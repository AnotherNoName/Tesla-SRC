package me.travis.wurstplus.wurstplustwo.util.font;

import me.travis.wurstplus.Wurstplus;
import net.minecraft.client.Minecraft;

public class FontUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static float drawStringWithShadow(boolean customFont, String text, int x, int y, int color){
        if(customFont) return Wurstplus.fontRenderer.drawStringWithShadow(text, x, y, color);
        else return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    public static int getStringWidth(boolean customFont, String str){
        if(customFont) return Wurstplus.fontRenderer.getStringWidth(str);
        else return mc.fontRenderer.getStringWidth(str);
    }

    public static int getFontHeight(boolean customFont){
        if(customFont) return Wurstplus.fontRenderer.getHeight();
        else return mc.fontRenderer.FONT_HEIGHT;
    }

    public static float drawKeyStringWithShadow(boolean customFont, String text, int x, int y, int color) {
        if(customFont) return Wurstplus.fontRenderer.drawStringWithShadow(text, x, y, color);
        else return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }
}
