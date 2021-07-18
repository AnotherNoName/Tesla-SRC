package me.travis.wurstplus.wurstplustwo.guiscreen.hud;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.pinnables.WurstplusPinnable;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTextureHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WurstplusLogo extends WurstplusPinnable {
    
    public WurstplusLogo() {
        super("Logo", "Logo", 1, 0, 0);
    }

    ResourceLocation logo = new ResourceLocation("minecraft:solotestla.png");

    int red = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
    int green = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
    int blue = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
    int alpha = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

    @Override
	public void render() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glColor4f((float)red/255, (float)green/255, (float)blue/255, 1);
        mc.getTextureManager().bindTexture(logo);

        Gui.drawScaledCustomSizeModalRect(this.get_x(), this.get_y(), 0,0,512,512,50,50,512,512);
        mc.getTextureManager().deleteTexture(logo);
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
		this.set_width(50);
		this.set_height(50);
	}


}