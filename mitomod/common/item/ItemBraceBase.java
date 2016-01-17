package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.utilities.BB_MovingObjectPosition;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBraceBase extends Item {

	public ItemBraceBase() {
		super();

	}

	public boolean isDamageable() {
		return false;
	}

	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		return itemstack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int i) {

	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {

	}

	public BB_MovingObjectPosition getMovingOPWithKey(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, double partialticks) {
		return null;
	}

	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTick) {
		return false;
	}

	public NBTTagCompound getNBT(ItemStack itemstack){
		NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}

		return nbt;
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
	}

}
