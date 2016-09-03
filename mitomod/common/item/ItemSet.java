package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSet extends ItemBraceBase {

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.none;
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
		nbt.setBoolean("activated", false);
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);
	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition mop, BB_Key key, boolean p_77663_5_) {
		MovingObjectPosition mop1 = this.getMovingOPWithKey(itemstack, world, player, key, mop, 1.0);
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}
		if (!world.isRemote) {
			if (mop != null && (MitoUtil.canClick(world, key, mop))) {
				if (nbt.getBoolean("activated")) {
					Vec3 end = MitoMath.copyVec3(mop.hitVec);
					Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
					this.onActiveClick(world, player, itemstack, mop1, set, end, nbt);
					this.nbtInit(nbt, itemstack);
				} else {
					nbt.setDouble("setX", mop.hitVec.xCoord);
					nbt.setDouble("setY", mop.hitVec.yCoord);
					nbt.setDouble("setZ", mop.hitVec.zCoord);
					nbt.setBoolean("activated", true);
					this.activate(world, player, itemstack, mop1, nbt);
				}
			}
		} else {
			if (mop != null && (MitoUtil.canClick(world, key, mop))) {
				clientProcess(mop, itemstack);
			}
		}
	}

	public void clientProcess(MovingObjectPosition mop, ItemStack itemstack) {
	}

	public void activate(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition movingOP, NBTTagCompound nbt) {
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition movingOP, Vec3 set, Vec3 end, NBTTagCompound nbt) {
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {

		NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt != null) {
			if (nbt.getBoolean("activated")) {
				if (entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					if (player.getCurrentEquippedItem() != itemstack) {
						this.nbtInit(nbt, itemstack);
					}
				} else {
					this.nbtInit(nbt, itemstack);
				}
			}
		} else {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null && nbt.getBoolean("activated")) {
			this.nbtInit(nbt, itemstack);
		}
		return true;
	}
}
