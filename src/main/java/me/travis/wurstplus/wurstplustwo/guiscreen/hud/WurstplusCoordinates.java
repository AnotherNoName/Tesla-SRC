package me.travis.wurstplus.wurstplustwo.guiscreen.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.pinnables.WurstplusPinnable;


public class WurstplusCoordinates extends WurstplusPinnable {
	ChatFormatting dg = ChatFormatting.DARK_GRAY;
	ChatFormatting db = ChatFormatting.DARK_BLUE;
	ChatFormatting dr = ChatFormatting.DARK_RED;

	public WurstplusCoordinates() {
		super("Coordinates", "Coordinates", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String x = ChatFormatting.GRAY + "[" + Wurstplus.r + Integer.toString((int) (mc.player.posX)) + ChatFormatting.GRAY + "]" + Wurstplus.r;
		String y = ChatFormatting.GRAY + "[" + Wurstplus.r + Integer.toString((int) (mc.player.posY)) + ChatFormatting.GRAY + "]" + Wurstplus.r;
		String z = ChatFormatting.GRAY + "[" + Wurstplus.r + Integer.toString((int) (mc.player.posZ)) + ChatFormatting.GRAY + "]" + Wurstplus.r;

		String x_nether = ChatFormatting.GRAY + "[" + Wurstplus.r + Long.toString(Math.round(mc.player.dimension != -1 ? (mc.player.posX / 8) : (mc.player.posX * 8))) + ChatFormatting.GRAY + "]" + Wurstplus.r;
		String z_nether = ChatFormatting.GRAY + "[" + Wurstplus.r + Long.toString(Math.round(mc.player.dimension != -1 ? (mc.player.posZ / 8) : (mc.player.posZ * 8))) + ChatFormatting.GRAY + "]" + Wurstplus.r;

		String line = "XYZ " + x + y + z + " XZ " + x_nether + z_nether;

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width"));
		this.set_height(this.get(line, "height") + 2);
	}
}