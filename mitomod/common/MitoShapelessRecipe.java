package com.mito.mitomod.common;

import com.mito.mitomod.common.item.ItemBrace;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class MitoShapelessRecipe implements IRecipe {

	//private ItemStack recipeItem = new ItemStack(mitomain.ArrayItemBrace);
	private ItemStack outItem = new ItemStack(mitomain.ItemBrace);

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		boolean allEmpty = true;
		int dyeNum = 0;
		int braceNum = 0;
		for (int h = 0; h < 3; h++) {
			for (int w = 0; w < 3; w++) {
				ItemStack current = inv.getStackInRowAndColumn(h, w);
				if (current == null) {
					allEmpty &= true;
					continue;
				} else
					allEmpty &= false;
				if (current.getItem() instanceof ItemBrace) {
					braceNum++;
				} else if (current.getItem() == Items.dye) {
					if (dyeNum != 0) {

						return false;
					}
					dyeNum++;
				} else {

					return false;
				}
			}
		}
		boolean flag1 = (dyeNum == 1 && braceNum == 0);
		return (!allEmpty && !flag1);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int braceNum = 0;
		int isize = 0;
		byte mode = (byte) 0;
		int color = -1;
		int braceColor = -1;
		int totalSize = 0;
		int place = 1;
		ItemBrace brace = (ItemBrace) mitomain.ItemBrace;
		ItemStack itemstack;

		for (int h = 0; h < 3; h++) {
			for (int w = 0; w < 3; w++) {
				if (inv.getStackInRowAndColumn(h, w) != null) {
					itemstack = inv.getStackInRowAndColumn(h, w);
					if (inv.getStackInRowAndColumn(h, w).getItem() instanceof ItemBrace) {
						isize = brace.getSize(itemstack);
						mode = (byte) brace.getType(itemstack);
						braceColor = brace.getColor(itemstack);
						braceNum++;
						totalSize += isize;
						place = h * 3 + w;
					} else if (inv.getStackInRowAndColumn(h, w).getItem() == Items.dye) {
						color = brace.getColor(itemstack);
					}
				}
			}
		}

		if (color != -1) {
			ItemStack itemstack1 = new ItemStack(mitomain.ItemBrace, braceNum, color);
			brace.setSize(itemstack1, isize);
			brace.setType(itemstack1, mode);
			return itemstack1;
		} else if (braceNum == 1) {
			int mode1 = (mode + place >= brace.typeMax) ? mode + place - brace.typeMax : mode + place;
			ItemStack itemstack1 = new ItemStack(mitomain.ItemBrace, braceNum, braceColor);
			brace.setSize(itemstack1, isize);
			brace.setType(itemstack1, mode1);
			return itemstack1;
		} else {
			ItemStack itemstack1 = new ItemStack(mitomain.ItemBrace, braceNum, braceColor);
			if (totalSize > brace.sizeMax) {
				totalSize = brace.sizeMax;
			} else if (totalSize < -1) {
				totalSize = 0;
			}
			brace.setSize(itemstack1, totalSize);
			brace.setType(itemstack1, mode);
			return itemstack1;
		}

	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return outItem;
	}

}
