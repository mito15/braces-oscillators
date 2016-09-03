package com.mito.mitomod.common.item.itemGUI;

import java.util.List;

import com.mito.mitomod.BraceBase.Brace.Render.BB_TypeResister;
import com.mito.mitomod.client.render.model.IDrawBrace;
import com.mito.mitomod.client.render.model.IDrawable;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

public class BB_GuiScrollingList2 extends GuiScrollingList {

	private GuiBraceProperty parent;
	private List<IDrawBrace> shapes;

	public BB_GuiScrollingList2(GuiBraceProperty parent, List<IDrawBrace> names, int listWidth) {
		super(parent.mc, listWidth, 90, 82, 172, parent.width - 10 - listWidth, 35);
		this.parent = parent;
		this.shapes = names;
		this.shapes.add(null);
	}

	@Override
	protected int getSize() {
		return shapes.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2) {
		this.parent.selectShapeIndex(var1);
	}

	@Override
	protected boolean isSelected(int var1) {
		return this.parent.shapeIndexSelected(var1);
	}

	@Override
	protected void drawBackground() {
		this.parent.drawDefaultBackground();
	}

	@Override
	protected int getContentHeight() {
		return (this.getSize()) * 35 + 1;
	}

	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5) {
		IDrawBrace tex = shapes.get(listIndex);
		if (tex != null) {
			if (tex instanceof IDrawable) {
				parent.mc.getTextureManager().bindTexture(this.parent.texture);
				int dx = 20;
				int dy = 20;
				double dz = -10;
				float f = 0.00390625F;
				float f1 = 0.00390625F;
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV((double) (this.left + 0), (double) (var3 + dy), dz, (double) ((float) (this.left + 0) * f), (double) ((float) (var3 + dy) * f1));
				tessellator.addVertexWithUV((double) (this.left + dx), (double) (var3 + dy), dz, (double) ((float) (this.left + dx) * f), (double) ((float) (var3 + dy) * f1));
				tessellator.addVertexWithUV((double) (this.left + dx), (double) (var3 + 0), dz, (double) ((float) (this.left + dx) * f), (double) ((float) (var3 + 0) * f1));
				tessellator.addVertexWithUV((double) (this.left + 0), (double) (var3 + 0), dz, (double) ((float) (this.left + 0) * f), (double) ((float) (var3 + 0) * f1));
				tessellator.draw();
				((IDrawable)tex).renderAt(Vec3.createVectorHelper(this.left, var3, 0), 0.0, 0.0, 30, 1.0, 1.0F);
			}
			String name = BB_TypeResister.getName(tex);
			String[] names = name.split(":");
			if (names.length == 1) {
				this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(names[0], listWidth - 10), this.left + 23, var3 + 2, 0xFFFFFF);
			} else if (names.length == 2) {
				this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(names[1], listWidth - 10), this.left + 23, var3 + 2, 0xFFFFFF);
				this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(" " + names[0], listWidth - 10), this.left + 23, var3 + 12, 0xCCCCCC);
			}
			//this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(tex.getTextureName(0), listWidth - 10), this.left + 3, var3 + 12, 0xCCCCCC);
		} else {
			this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth("none", listWidth - 10), this.left + 18, var3 + 2, 0xFFFFFF);
			//this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth("", listWidth - 10), this.left + 3, var3 + 12, 0xCCCCCC);
		}
		/*this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(tex.getMetadata() != null ? tex.getMetadata().getChildModCountString() : "Metadata not found", listWidth - 10), this.left + 3, var3 + 22,
				0xCCCCCC);*/

	}

}
