package team.lodestar.fufo.client.ui.spellinventory;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.lodestar.fufo.FufoMod;
import team.lodestar.fufo.client.ui.component.FlexBox;
import team.lodestar.fufo.client.ui.constraint.PixelConstraint;
import team.lodestar.fufo.unsorted.util.GuiHelper;
import team.lodestar.fufo.unsorted.util.MathHelper;
import net.minecraft.client.gui.Font;

public class SpellInventoryScreen extends Screen {

	private static int imageWidth = 176;
	private static int imageHeight = 166;
	
	private ResourceLocation texture;
	private SpellContainer container;
	
//	private static ResourceLocation SPELL_INVENTORY = new ResourceLocation(FufoMod.FUFO, "/textures/ui/spellinventory/gui_blank.png");
	
	public SpellInventoryScreen(String texName, Component title, SpellContainer container, Player player) {
		super(title);
		texture = new ResourceLocation("/textures/ui/spellinventory/" + texName);
		this.container = container;
	}
	
	FlexBox screen;
	
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
		screen = new FlexBox().withWidth(new PixelConstraint(width)).withHeight(new PixelConstraint(height)).withAxis(FlexBox.Axis.HORIZONTAL);
		screen.jerk();
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
			if (isHovering(pMouseX, pMouseY, container.getSlot(i))) {
				// Draw square
			}
		}
	}
}