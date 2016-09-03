package com.mito.mitomod.BraceBase;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public enum BB_EnumTexture {
	IRON("mitomod:textures/blocks/brace_", ".png", "iron", Blocks.iron_block),
	STONE("textures/blocks/stone.png", "stone", Blocks.stone),
	COBBLESTONE("textures/blocks/cobblestone.png", "cobblestone", Blocks.cobblestone),
	WOOD_OAK("textures/blocks/planks_oak.png", "oak", Blocks.planks),
	WOOD_SPRUCE("textures/blocks/planks_spruce.png", "spruce", Blocks.planks),
	WOOD_BIRCH("textures/blocks/planks_birch.png", "birch", Blocks.planks),
	WOOD_JUNGLE("textures/blocks/planks_jungle.png", "jungle", Blocks.planks),
	WOOD_ACACIA("textures/blocks/planks_acacia.png", "acacia", Blocks.planks),
	WOOD_DARKOAK("textures/blocks/planks_big_oak.png", "darkoak", Blocks.planks),
	WOOL("textures/blocks/wool_colored_", ".png", "wool", Blocks.wool),
	HARDENED_CLAY("textures/blocks/hardened_clay_stained_", ".png", "hardened clay", Blocks.stained_hardened_clay),
	;

	private final Block[] materials = new Block[] { Blocks.stone, Blocks.cobblestone, Blocks.wool, Blocks.stained_hardened_clay, Blocks.planks, };
	public static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };
	private String name;
	private String location;
	private String extension;
	private ResourceLocation rl;
	public boolean hasColor;
	private Block block;

	BB_EnumTexture(String location, String name, Block sound) {
		this.rl = new ResourceLocation(location);
		this.location = location;
		this.name = name;
		this.hasColor = false;
		this.setBlock(sound);
	}

	BB_EnumTexture(String location, String extension, String name, Block sound) {
		this.location = location;
		this.extension = extension;
		this.name = name;
		this.hasColor = true;
		this.setBlock(sound);
	}

	public ResourceLocation getResourceLocation(int color) {
		if (this.hasColor) {
			return new ResourceLocation(this.location + color_name[color] + this.extension);
		}
		return rl;
	}

	public static BB_EnumTexture getTexture(ItemStack itemstack) {
		if (itemstack.getItem() == Item.getItemFromBlock(Blocks.stone)) {
			return STONE;
		} else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.cobblestone)) {
			return COBBLESTONE;
		} else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.planks)) {
			switch (itemstack.getItemDamage()) {
			case 0:
				return WOOD_OAK;
			case 1:
				return WOOD_SPRUCE;
			case 2:
				return WOOD_BIRCH;
			case 3:
				return WOOD_JUNGLE;
			case 4:
				return WOOD_ACACIA;
			case 5:
				return WOOD_DARKOAK;
			default:
				return WOOD_OAK;
			}
		} else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.wool)) {
			return WOOL;
		} else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
			return HARDENED_CLAY;
		}
		return null;
	}

	public String getBreakSound() {
		return this.getBlock().stepSound.getBreakSound();
	}

	public String getStepResourcePath() {
		return this.getBlock().stepSound.getStepResourcePath();
	}

	public float getVolume() {
		return this.getBlock().stepSound.volume;
	}

	public float getPitch() {
		return this.getBlock().stepSound.getPitch();
	}

	public String getTextureName(int color) {
		if(this.hasColor){
			return color_name[color] + " " + this.name;
		} else {
			return this.name;
		}
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

}
