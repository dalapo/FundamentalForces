package team.lodestar.fufo.client.ui.spellinventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.unsorted.util.GuiHelper;
import team.lodestar.fufo.unsorted.util.MathHelper;
import team.lodestar.lodestone.systems.ui.screen.LodestoneScreen;
import net.minecraft.client.gui.Font;

public class SpellInventoryScreen extends Screen {

	private static int imageWidth = 176;
	private static int imageHeight = 166;
	
	private ResourceLocation texture;
	private SpellContainer container;
	
//	private static ResourceLocation SPELL_INVENTORY = new ResourceLocation(FufoMod.FUFO, "/textures/ui/spellinventory/gui_blank.png");
	
	public SpellInventoryScreen(String texName, Component title, SpellContainer container, Player player) {
		super(title);
		texture = new ResourceLocation(FufoMod.FUFO, "/textures/ui/spellinventory/" + texName);
		this.container = container;
	}
	
	private static boolean isHovering(int mouseX, int mouseY, SpellSlot slot) {
		int x = slot.getX();
		int y = slot.getY();
		return MathHelper.isInRange(mouseX, x, x+SpellSlot.SIZE) && MathHelper.isInRange(mouseY, y, y+SpellSlot.SIZE);
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	public SpellContainer getContainer() {
		return container;
	}
	
	@Override
	protected void init() {
		super.init();
	}
	
	protected void drawSlotHighlight(PoseStack posestack, int x, int y, int colour, int blitOffset) {
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		fillGradient(posestack, x, y, x + 16, y + 16, colour, colour, blitOffset);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableDepthTest();
	}
	
	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		GuiHelper.bindTexture(texture);
		int left = (this.width - imageWidth) / 2;
		int top = (this.height - imageHeight) / 2;
		this.blit(pPoseStack, left, top, 0, 0, imageWidth, imageHeight);
		font.draw(pPoseStack, title, left+10, top+10, 0x404040);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		
		for (int i=0; i<container.getSlotCount(); i++) {
			SpellSlot slot = container.getSlot(i);
			if (isHovering(pMouseX, pMouseY, slot)) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				drawSlotHighlight(pPoseStack, slot.getX(), slot.getY(), 0x80808080, getBlitOffset());
			}
		}
	}
}