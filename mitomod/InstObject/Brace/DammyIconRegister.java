package com.mito.mitomod.InstObject.Brace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class DammyIconRegister implements IIconRegister {

	public String dom;
	public IIconRegister register;

	public DammyIconRegister() {
		register = Minecraft.getMinecraft().getTextureMapBlocks();
	}

	@Override
	public IIcon registerIcon(String d) {
		dom = d;
		return register.registerIcon(d);
	}

}
