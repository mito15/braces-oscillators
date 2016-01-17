package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.ItemUsePacketProcessor;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitomain;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemSelectTool extends ItemBraceBase {

	public byte key = 0;

	public ItemSelectTool() {
		super();
		this.setTextureName("mitomod:select");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.none;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {

		player.setItemInUse(itemstack, 71999);
		return itemstack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int i) {

		NBTTagCompound nbt = itemstack.getTagCompound();

		//if (true || !nbt.getBoolean("activated")) {
		if (world.isRemote) {
			PacketHandler.INSTANCE.sendToServer(new ItemUsePacketProcessor(mitomain.proxy.getKey(), player.inventory.currentItem));

		}

		player.clearItemInUse();

	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {

		nbt.setBoolean("activated", false);
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);
		nbt.setInteger("selectNum", 0);

	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

		/*NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt != null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}

		if (!world.isRemote) {
			Vec3 coord;
			boolean cKey = key.isControlPressed();
			boolean canAir = false;
			boolean hitEntity;
			MovingObjectPosition movingOP;

			if (key.isAltPressed()) {

				movingOP = MitoUtil.rayTraceIncludeBrace(player, 3.0, 1.0f, cKey);
				coord = movingOP.hitVec;
				canAir = true;

			} else {

				movingOP = MitoUtil.rayTraceIncludeBrace(player, 5.0, 1.0f, cKey);
				coord = movingOP.hitVec;
				canAir = (movingOP.typeOfHit == MovingObjectType.ENTITY);
			}

			hitEntity = (movingOP.typeOfHit == MovingObjectType.ENTITY);

			if (canAir || !player.worldObj.isAirBlock(movingOP.blockX, movingOP.blockY, movingOP.blockZ)) {

				if (nbt.getBoolean("activated")) {

					Vec3 end = coord;
					Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));

				} else {

					nbt.setDouble("setX", coord.xCoord);
					nbt.setDouble("setY", coord.yCoord);
					nbt.setDouble("setZ", coord.zCoord);
					nbt.setBoolean("activated", true);
				}
			}
		}*/
	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {

		//mitoLogger.info("onUpdate"+world.getWorldTime());

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
