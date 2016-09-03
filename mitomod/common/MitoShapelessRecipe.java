package com.mito.mitomod.common;

import com.mito.mitomod.BraceBase.BB_EnumTexture;
import com.mito.mitomod.BraceBase.Brace.Render.BB_TypeResister;
import com.mito.mitomod.common.item.ItemBrace;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class MitoShapelessRecipe implements IRecipe {

	//private ItemStack recipeItem = new ItemStack(mitomain.ArrayItemBrace);
	private ItemStack outItem = new ItemStack(BAO_main.ItemBrace);
	private final Block[] materials = new Block[] { Blocks.stone, Blocks.cobblestone, Blocks.wool, Blocks.stained_hardened_clay, Blocks.planks, };

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		boolean allEmpty = true;
		int dyeNum = 0;
		int braceNum = 0;
		boolean ism = false;
		BB_EnumTexture material = null;
		for (int h = 0; h < 3; h++) {
			for (int w = 0; w < 3; w++) {
				ItemStack current = inv.getStackInRowAndColumn(h, w);

				if (current == null) {
					allEmpty &= true;
					continue;
				} else {
					allEmpty &= false;
				}

				if (current.getItem() instanceof ItemBrace) {
					ItemBrace itembrace = (ItemBrace) current.getItem();
					if (material != null && material != itembrace.getMaterial(current)) {
						return false;
					}
					material = itembrace.getMaterial(current);
					braceNum++;
				} else if (current.getItem() == Items.dye) {
					if (dyeNum != 0) {
						return false;
					}
					dyeNum++;
				} else if (this.isMaterial(current)) {
					if (dyeNum != 0) {
						return false;
					}
					ism = true;
					dyeNum++;
				} else {
					return false;
				}
			}
		}
		if(!ism && dyeNum == 1 && material != null && !material.hasColor){
			return false;
		}
		boolean flag1 = (dyeNum == 1 && braceNum == 0);
		return (!allEmpty && !flag1);
	}

	private boolean isMaterial(ItemStack stack) {
		Item item = stack.getItem();
		for (int i = 0; i < this.materials.length; i++) {
			if (item == Item.getItemFromBlock(this.materials[i])) {
				return true;
			}
		}
		return false;

	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int braceNum = 0;
		int isize = 0;
		String mode = "";
		int dye = -1;
		BB_EnumTexture material = null;
		int materialColor = 0;
		BB_EnumTexture braceMaterial = null;
		int braceColor = -1;
		int totalSize = 0;
		int place = 1;
		ItemBrace brace = (ItemBrace) BAO_main.ItemBrace;
		ItemStack itemstack;

		for (int h = 0; h < 3; h++) {
			for (int w = 0; w < 3; w++) {
				if (inv.getStackInRowAndColumn(h, w) != null) {
					itemstack = inv.getStackInRowAndColumn(h, w);
					if (inv.getStackInRowAndColumn(h, w).getItem() instanceof ItemBrace) {
						isize = brace.getSize(itemstack);
						mode = brace.getType(itemstack);
						braceColor = brace.getColor(itemstack);
						braceMaterial = brace.getMaterial(itemstack);
						braceNum++;
						totalSize += isize;
						place = h * 3 + w;
					} else if (inv.getStackInRowAndColumn(h, w).getItem() == Items.dye) {
						dye = brace.getColor(itemstack);
					} else if (this.isMaterial(inv.getStackInRowAndColumn(h, w))) {
						material = BB_EnumTexture.getTexture(itemstack);
						materialColor = itemstack.getItemDamage();
					}
				}
			}
		}

		if (dye != -1) {
			ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, braceNum, dye);
			brace.setMaterial(itemstack1, braceMaterial);
			brace.setSize(itemstack1, isize);
			brace.setType(itemstack1, mode);
			return itemstack1;
		} else if (material != null) {
			ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, braceNum, materialColor);
			brace.setMaterial(itemstack1, material);
			brace.setSize(itemstack1, isize);
			brace.setType(itemstack1, mode);
			return itemstack1;
		} else if (braceNum == 1) {
			String mode1 = BB_TypeResister.shapeList.get(place%BB_TypeResister.shapeList.size());//(mode + place >= brace.typeMax) ? mode + place - brace.typeMax : mode + place;
			ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, braceNum, braceColor);
			brace.setMaterial(itemstack1, braceMaterial);
			brace.setSize(itemstack1, isize);
			brace.setType(itemstack1, mode1);
			return itemstack1;
		} else {
			ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, braceNum, braceColor);
			if (totalSize > brace.sizeMax) {
				totalSize = brace.sizeMax;
			} else if (totalSize < -1) {
				totalSize = 0;
			}
			brace.setMaterial(itemstack1, braceMaterial);
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
