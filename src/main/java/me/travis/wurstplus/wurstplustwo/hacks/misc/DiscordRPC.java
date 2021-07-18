package me.travis.wurstplus.wurstplustwo.hacks.misc;

import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.Discord;

public class DiscordRPC extends WurstplusHack {
    public DiscordRPC() {
        super(WurstplusCategory.WURSTPLUS_MISC);

        this.name = "DiscordRPC";
        this.tag = "DiscordRPC";
        this.description = "DiscordRPC";
    }

    public void enable() {
        Discord.startRPC();
    }

    public void disable() {
        Discord.stopRPC();
    }
}
