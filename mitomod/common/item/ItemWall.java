package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemWall extends ItemSet {

	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public ItemWall() {
		super();
		this.setTextureName("mitomod:wall");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return iconArray[par1 % iconArray.length];
	}

	public int getColor(ItemStack itemstack) {
		return itemstack.getItemDamage();
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
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
			if (mop != null && (MitoUtil.canClick(world, key, mop1))) {
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
		}
	}

}
