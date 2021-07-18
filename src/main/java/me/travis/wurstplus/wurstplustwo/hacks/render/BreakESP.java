package me.travis.wurstplus.wurstplustwo.hacks.render;

import io.netty.util.internal.ConcurrentSet;
import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BreakESP extends WurstplusHack {
    public BreakESP() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name = "BreakESP";
        this.tag = "BreakESP";
        this.description = "Render BreakESP";

        this.alphaMap.put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);

    }

    WurstplusSetting ignoreSelf = create("IgnoreSelf", "IgnoreSelf", false);
    WurstplusSetting onlyObby = create("OnlyObi", "OnlyObi", true);
    WurstplusSetting alpha = create("Alpha", "Alpha", 50, 0, 255);
    WurstplusSetting fade = create("Fade", "Fade", false);
    WurstplusSetting red = create("Red", "Red", 255, 0, 255);
    WurstplusSetting green = create("Green", "Green", 255, 0, 255);
    WurstplusSetting blue = create("Blue", "Blue", 255, 0, 255);
    private ConcurrentSet test = new ConcurrentSet();
    public ConcurrentSet breaking = new ConcurrentSet();
    float inc;
    BlockPos pos;
    public static BreakESP INSTANCE;
    private Map alphaMap = new HashMap();
    private ArrayList options;

    public void render(WurstplusEventRender var1) {
        mc.renderGlobal.damagedBlocks.forEach((var1x, var2) -> {
            if (var2 != null) {
                if (!(Boolean)this.ignoreSelf.get_value(true) || mc.world.getEntityByID(var1x) != mc.player) {
                    if (!(Boolean)this.onlyObby.get_value(true) || mc.world.getBlockState(var2.getPosition()).getBlock() == Blocks.OBSIDIAN) {
                        int var3 = (Boolean)this.fade.get_value(true) ? (Integer)this.alphaMap.get(var2.getPartialBlockDamage()) : (Integer)this.alpha.get_value(1);
                        WurstplusRenderUtil.prepare(7);
                        this.render_block(var2.getPosition(), var3);
                        //WurstplusRenderUtil.drawBox(var2.getPosition(), (Integer) this.red.get_value(1), (Integer) this.green.get_value(1), (Integer) this.blue.get_value(1), var3, 63);
                        WurstplusRenderUtil.release();

                    }
                }
            }
        });
    }

    public void render_block(BlockPos pos, int alpha) {
        BlockPos render_block = pos;

        float h = (float) 1.0;
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    red.get_value(1), green.get_value(1), blue.get_value(1), alpha,
                    "all"
            );
            RenderHelp.release();
    }
}
