package com.mito.mitomod.common.item;

import java.util.List;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBlockSetter extends ItemSet {

	public ItemBlockSetter() {
		super();
		this.setMaxStackSize(1);
		this.setTextureName("mitomod:sack");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		NBTTagCompound nbt = getTagCompound(itemStack);
		if (player.isSneaking() && !nbt.getBoolean("activated")) {
			if (!world.isRemote)
				player.openGui(BAO_main.INSTANCE, BAO_main.INSTANCE.GUI_ID_BBSetter, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		} else {
			player.setItemInUse(itemStack, 71999);
		}
		/*
		BB_MovingObjectPosition movingOP = this.getMovingOPWithKey(itemStack, world, player, new BB_Key(false, false, false), 1.0);
		mitoLogger.info("coordinate  x : " + movingOP.hitVec.xCoord + " y : " + movingOP.hitVec.yCoord + " z : " + movingOP.hitVec.zCoord);*/
		return itemStack;
	}

	public MovingObjectPosition getMovingOPWithKey(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, MovingObjectPosition mop, double partialticks) {
		NBTTagCompound nbt = this.getNBT(itemstack);

		if (mop != null && MitoUtil.canClick(world, key, mop)) {
			MitoUtil.snapBlockOffset(mop);
			if (!key.isControlPressed()) {
				mop = this.snap(mop, itemstack, world, player, key, nbt);
			}
			if (key.isShiftPressed()) {
				this.snapDegree(mop, itemstack, world, player, key, nbt);
			}
		}

		return mop;
	}

	public double getRayDistance(BB_Key key) {
		return key.isAltPressed() ? 3.0 : 5.0;
	}

	public MovingObjectPosition snap(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt) {
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {
			this.snapBraceBase(mop, itemstack, world, player, key);
		}
		return mop;
	}

	public void snapDegree(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt) {
		if (nbt.getBoolean("activated")) {
			Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			MitoUtil.snapByShiftKey(mop, set);
		}
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition movingOP, Vec3 set, Vec3 end, NBTTagCompound nbt) {
		if (!world.isRemote) {
			ItemStack currentItem;
			ItemStack[] items = new ItemStack[54];
			NBTTagList tags = (NBTTagList) itemstack.getTagCompound().getTag("Items");
			if (tags == null)
				tags = new NBTTagList();

			for (int i = 0; i < tags.tagCount(); i++) {
				NBTTagCompound tagCompound = tags.getCompoundTagAt(i);
				int slot = tagCompound.getByte("Slot");
				if (slot >= 0 && slot < items.length) {
					items[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
				}
			}
			int slot = 0;

			if (MitoMath.subAbs(set, end) < 100) {
				if (items[slot] != null && items[slot].getItem() == BAO_main.ItemBar) {
					List<int[]> list = MitoUtil.getBlocksOnLine(world, set, end, null, 1.0, true);
					for (int n = 0; n < list.size(); n++) {
						int[] aint = list.get(n);
						world.setBlock(aint[0], aint[1], aint[2], Blocks.air);
					}
					return;
				}
				List<int[]> list = MitoUtil.getBlocksOnLine(world, set, end, null, 0.98);
				for (int n = 0; n < list.size(); n++) {
					int[] aint = list.get(n);
					boolean flag = true;
					while (flag) {
						if (slot > 53) {
							n = list.size();
							break;
						} else
							if (items[slot] == null) {
							slot++;
							continue;
						}
						Block block = Block.getBlockById(Item.getIdFromItem(items[slot].getItem()));
						if (block != null && items[slot].stackSize > 0) {
							world.setBlock(aint[0], aint[1], aint[2], block, items[slot].getItemDamage() % 16, 3);
							if (!player.capabilities.isCreativeMode) {
								items[slot].stackSize--;
								if (items[slot].stackSize == 0) {
									items[slot] = null;
									slot++;
								}
							}
							break;
						} else {
							slot++;
							continue;
						}
					}
				}
			}

			NBTTagList tagList = new NBTTagList();
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					NBTTagCompound compound = new NBTTagCompound();
					compound.setByte("Slot", (byte) i);
					items[i].writeToNBT(compound);
					tagList.appendTag(compound);
				}
			}
			itemstack.setTagCompound(new NBTTagCompound());
			itemstack.getTagCompound().setTag("Items", tagList);
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks, MovingObjectPosition mop) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		if (mop == null || !MitoUtil.canClick(player.worldObj, BAO_main.proxy.getKey(), mop))
			return false;
		Vec3 set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (nbt.getBoolean("activated")) {
			Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			List<int[]> list = MitoUtil.getBlocksOnLine(player.worldObj, end, set, null, 0.98);
			for (int n = 0; n < list.size(); n++) {
				int[] aint = list.get(n);
				Vec3 v = Vec3.createVectorHelper(0.5 + aint[0], 0.5 + aint[1], 0.5 + aint[2]);
				rh.drawBox(player, v, 0.95, partialTicks);
			}
		} else {
			/*if (mop.braceHit != null && mop.braceHit instanceof Brace && 1.0 < ((Brace) mop.braceHit).size) {
				rh.drawCenter(player, set, ((Brace) mop.braceHit).size / 2 + 0.1, partialTicks);
			} else {
			
			}*/
			//rh.drawBox(player, set, 1.0, partialTicks);
			return false;
		}

		return true;

	}
}
