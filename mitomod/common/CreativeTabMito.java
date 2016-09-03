package com.mito.mitomod.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabMito extends CreativeTabs {

	public CreativeTabMito(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return BAO_main.ItemBar;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return "Braces&Oscillators";
	}


}
