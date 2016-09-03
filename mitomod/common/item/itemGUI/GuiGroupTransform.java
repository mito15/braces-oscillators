package com.mito.mitomod.common.item.itemGUI;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.client.BB_SelectedGroup;
import com.mito.mitomod.common.BAO_main;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiGroupTransform extends GuiScreen {

	static final ResourceLocation texture = new ResourceLocation("mitomod:textures/gui/transform.png");
	private BB_SelectedGroup sel = BAO_main.proxy.sg;
	protected int xSize = 176;
	protected int ySize = 166;

	private GuiTextField size;
	private int isize = 100;
	
	private GuiTextField rot;
	private int irot = 0;
	
	public static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light blue", "magenta", "orange", "white", "none" };

	public GuiGroupTransform() {
		this.xSize = 256;
		this.ySize = 95;
	}

	@Override
	public void initGui() {
		super.initGui();
		//make buttons
		//id, x, y, width, height, text
		buttonList.add(new GuiButton(1, this.width / 2 - 120, this.height / 2 + 60, 30, 20, I18n.format("gui.back", new Object[0])));
		buttonList.add(new GuiButton(2, this.width / 2 - 120, this.height / 2 + 82, 30, 20, I18n.format("gui.done", new Object[0])));

		buttonList.add(new GuiButton(101, this.width / 2 - 80, this.height / 2 + 60, 30, 20, "apply"));
		buttonList.add(new GuiButton(102, this.width / 2 - 80, this.height / 2 + 60, 30, 20, "cancel"));

		this.buttonList.add(new GuiButton(201, this.width / 2 - 40, this.height / 2 + 60, 20, 20, "<"));
		this.buttonList.add(new GuiButton(202, this.width / 2 + 20, this.height / 2 + 60, 20, 20, ">"));
		this.rot = this.setTextField(this.width / 2 - 18, this.height / 2 + 60, 34, 20, String.valueOf(sel.rot));
		this.irot = sel.rot;

		this.buttonList.add(new GuiButton(203, this.width / 2 - 40, this.height / 2 + 88, 20, 20, "<"));
		this.buttonList.add(new GuiButton(204, this.width / 2 + 20, this.height / 2 + 88, 20, 20, ">"));
		this.size = this.setTextField(this.width / 2 - 18, this.height / 2 + 88, 34, 20, String.valueOf(sel.size));
		this.isize = sel.size;
		/*int is = this.sel.getSize();
		if(is != -1){
			this.isize = is;
			size.setText(String.valueOf(isize));
		}*/
	}

	protected GuiTextField setTextField(int xPos, int yPos, int w, int h, String text) {
		GuiTextField field = new GuiTextField(this.fontRendererObj, xPos, yPos, w, h);
		field.setMaxStringLength(32767);
		field.setFocused(false);
		field.setText(text);
		return field;
	}

	protected void actionPerformed(GuiButton guibutton) {
		//id is the id you give your button
		switch (guibutton.id) {
		case 1:
			this.mc.displayGuiScreen(new GuiItemSelectTool());
			break;
		case 2:
			//sel.applyProperty(selectedTex, icolor, shape);
			this.mc.thePlayer.closeScreen();
			break;
		case 101:
			//sel.applyProperty(selectedTex, icolor, shape);
			break;
		case 102:
			//sel.applyProperty(selectedTex, icolor, shape);
			sel.applyGroupSize(100);
			sel.applyGroupRot(0);
			this.mc.displayGuiScreen(new GuiItemSelectTool());
			break;
		case 203:
			this.isize -= 10;
			if (isize <= 0) {
				isize = 10000;
			}
			size.setText(String.valueOf(isize));
			sel.applyGroupSize(isize);
			break;
		case 204:
			this.isize += 10;
			if (isize > 10000) {
				isize = 0;
			}
			size.setText(String.valueOf(isize));
			sel.applyGroupSize(isize);
			break;
		case 201:
			this.irot -= 15;
			if (irot < 0) {
				irot = 345;
			}
			rot.setText(String.valueOf(irot));
			sel.applyGroupRot(irot);
			break;
		case 202:
			this.irot += 15;
			if (irot >= 360) {
				irot = 0;
			}
			rot.setText(String.valueOf(irot));
			sel.applyGroupRot(irot);
			break;
		}
		//Packet code here
		//PacketDispatcher.sendPacketToServer(packet); //send packet
	}

	public void drawScreen(int var1, int ver2, float ver3) {
		this.drawDefaultBackground();
		FontRenderer fontrenderer = this.fontRendererObj;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2 + 100;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		rot.drawTextBox();
		size.drawTextBox();
		this.fontRendererObj.drawString("rotation", this.width / 2 - 18, this.height / 2 + 52, 0xFFFFFF);
		this.fontRendererObj.drawString("size", this.width / 2 - 18, this.height / 2 + 80, 0xFFFFFF);
		super.drawScreen(var1, ver2, ver3);
	}

	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
			this.mc.thePlayer.closeScreen();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRendererObj;
	}

	@Override
	public void drawDefaultBackground() {
	}

}
