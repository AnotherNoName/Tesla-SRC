package me.travis.wurstplus.wurstplustwo.hacks.render;

import me.travis.wurstplus.wurstplustwo.event.events.RenderItemEvent;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPlayerJump;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class ViewModel extends WurstplusHack {
    public ViewModel() {
        super(WurstplusCategory.WURSTPLUS_RENDER);

        this.name        = "ViewModel";
        this.tag         = "ViewModel";
        this.description = "change viewmodel";
    }

    WurstplusSetting mainX = create("mainX", "mainX", 10.0, 0.0, 60.0);
    WurstplusSetting mainY = create("mainY", "mainY", -9.5, -30.0, 30.0);
    WurstplusSetting mainZ = create("mainZ", "mainZ", -14.5, -50.0, 50.0);
    WurstplusSetting offX = create("offX", "offX", -12.0, -60.0, 0.0);
    WurstplusSetting offY = create("offY", "offY", -9.5, -30.0, 30.0);
    WurstplusSetting offZ = create("offZ", "offZ", -14.5, -50.0, 50.0);
    WurstplusSetting mainAngel = create("mainAngle", "mainAngle", 0, 0, 360);
    WurstplusSetting mainRx = create("mainRotationPointX", "mainRotationPointX", 0.0, -1.0, 1.0);
    WurstplusSetting mainRy = create("mainRotationPointY", "mainRotationPointY", 0.0, -1.0, 1.0);
    WurstplusSetting mainRz = create("mainRotationPointZ", "mainRotationPointZ", 0.0, -1.0, 1.0);
    WurstplusSetting offAngel = create("offAngle", "offAngle", 0, 0, 360);
    WurstplusSetting offRx = create("offRotationPointX", "offRotationPointX", 0.0, -1.0, 1.0);
    WurstplusSetting offRy = create("offRotationPointY", "offRotationPointY", 0.0, -1.0, 1.0);
    WurstplusSetting offRz = create("offRotationPointZ", "offRotationPointZ", 0.0, -1.0, 1.0);
    WurstplusSetting mainScaleX = create("mainScaleX", "mainScaleX", 1.0, -5.0, 10.0);
    WurstplusSetting mainScaleY = create("mainScaleY", "mainScaleY", 1.0, -5.0, 10.0);
    WurstplusSetting mainScaleZ = create("mainScaleZ", "mainScaleZ", 1.0, -5.0, 10.0);
    WurstplusSetting offScaleX = create("offScaleX", "offScaleX", 1.0, -5.0, 10.0);
    WurstplusSetting offScaleY = create("offScaleY", "offScaleY", 1.0, -5.0, 10.0);
    WurstplusSetting offScaleZ = create("offScaleZ", "offScaleZ", 1.0, -5.0, 10.0);

    // Like look at all this shit xd

    @EventHandler
    private Listener<RenderItemEvent> onItemRender = new Listener<>(event -> {
        event.setMainX((double)mainX.get_value(1) / 10.0);
        event.setMainY((double)mainY.get_value(1) / 10.0);
        event.setMainZ((double)mainZ.get_value(1) / 10.0);

        event.setOffX((double)offX.get_value(1) / 10.0);
        event.setOffY((double)offY.get_value(1) / 10.0);
        event.setOffZ((double)offZ.get_value(1) / 10.0);

        event.setMainRAngel(mainAngel.get_value(1));
        event.setMainRx(mainRx.get_value(1));
        event.setMainRy(mainRy.get_value(1));
        event.setMainRz(mainRz.get_value(1));

        event.setOffRAngel(offAngel.get_value(1));
        event.setOffRx(offRx.get_value(1));
        event.setOffRy(offRy.get_value(1));
        event.setOffRz(offRz.get_value(1));

        event.setMainHandScaleX(mainScaleX.get_value(1));
        event.setMainHandScaleY(mainScaleY.get_value(1));
        event.setMainHandScaleZ(mainScaleZ.get_value(1));

        event.setOffHandScaleX(offScaleX.get_value(1));
        event.setOffHandScaleY(offScaleY.get_value(1));
        event.setOffHandScaleZ(offScaleZ.get_value(1));
    });


}
