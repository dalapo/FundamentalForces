package team.lodestar.fufo.unsorted.util;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.FufoMod;

public class GuiHelper {
	private GuiHelper() {}
	
	public static void bindTexture(String name) {
		ResourceLocation tex = new ResourceLocation(FufoMod.FUFO, name);
		bindTexture(tex);
	}
	
	public static void bindTexture(ResourceLocation tex) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, tex);
	}
	
	public static void drawRect(int x, int y, int w, int h, int color) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		
	}
}