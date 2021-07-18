package me.travis.wurstplus.mixins;

import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventBus;
import me.travis.wurstplus.wurstplustwo.event.events.RenderItemEvent;
import me.travis.wurstplus.wurstplustwo.hacks.render.NoRender;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class WurstplusMixinItemRenderer {

    @Redirect(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V"))
    public void transformRedirect(ItemRenderer renderer, EnumHandSide hand, float y) {
        RenderItemEvent event = new RenderItemEvent(0.56F, -0.52F + y * -0.6F, -0.72F, -0.56F, -0.52F + y * -0.6F, -0.72F,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                1.0, 1.0, 1.0,
                1.0, 1.0, 1.0
        );
        WurstplusEventBus.EVENT_BUS.post(event);
        if (hand == EnumHandSide.RIGHT) {
            GlStateManager.translate((float)event.getMainX(), (float)event.getMainY(), (float)event.getMainZ());
            GlStateManager.scale(event.getMainHandScaleX(), event.getMainHandScaleY(), event.getMainHandScaleZ());
            GlStateManager.rotate((float) event.getMainRAngel(), (float) event.getMainRx(), (float) event.getMainRy(), (float) event.getMainRz());
        } else {
            GlStateManager.translate((float)event.getOffX(), (float)event.getOffY(), (float)event.getOffZ());
            GlStateManager.scale(event.getOffHandScaleX(), event.getOffHandScaleY(), event.getOffHandScaleZ());
            GlStateManager.rotate((float) event.getOffRAngel(), (float) event.getOffRx(), (float) event.getOffRy(), (float) event.getOffRz());
        }
    }

    @Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
    public void renderOverlays(float partialTicks, CallbackInfo ci){
        if (Wurstplus.get_hack_manager().get_module_with_tag("NoRender").is_active() && NoRender.INSTANCE.hurtCam.get_value(true)) {
            ci.cancel();
        }
    }

}
