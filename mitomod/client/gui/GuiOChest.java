package com.mito.mitomod.client.gui;

import com.mito.mitomod.common.ContainerOscillatorChest;
import com.mito.mitomod.common.tile.TileOscillatorChest;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiOChest extends GuiContainer {

	private TileOscillatorChest tileentity;

	private static final ResourceLocation GUITEXTURE = new ResourceLocation("mitomod", "textures/gui/OChest.png");

	/** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int guiLeft;
	/** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int guiTop;

	public GuiOChest(EntityPlayer player, TileOscillatorChest tileentity) {
		super(new ContainerOscillatorChest(player, tileentity));
		this.tileentity = tileentity;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
		this.mc.renderEngine.bindTexture(GUITEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
