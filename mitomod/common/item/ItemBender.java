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

public class ItemBender extends ItemBraceBase {

	public byte key = 0;

	public ItemBender() {
		super();
		this.setTextureName("mitomod:bender");
		this.setMaxDamage(0);
		this.maxStackSize = 1;
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


		if (world.isRemote) {

			PacketHandler.INSTANCE.sendToServer(new ItemUsePacketProcessor(mitomain.proxy.getKey(), player.inventory.currentItem));

		}

		player.clearItemInUse();

	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {

		nbt.setBoolean("activated", false);
		nbt.setInteger("entityID", -1);
		nbt.setInteger("selectNum", 0);

	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

		/*NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
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

				//ctrlキー
				if (!cKey && !hitEntity) {

					coord = MitoUtil.conversionByControlKey(player, coord);
				}

				if (nbt.getBoolean("activated")) {

					mitoLogger.info("active");

					Vec3 end = coord;
					EntityFake ent = (EntityFake) world.getEntityByID(nbt.getInteger("entityID"));
					if (ent != null) {

						Vec3 set = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);

						//shiftKey軸平行
						if (key.isShiftPressed()) {

							end = MitoUtil.conversionByShiftKey(player, end, itemstack);

						}

						if (MitoMath.subAbs(set, end) < 100) {

							if (ent.host.pair[0] == ent) {
								ent.host.getData().setSetCP(end);
								PacketHandler.INSTANCE.sendToAll(new BendPacketProcessor(ent, end, true));
								mitoLogger.info("set set control point");
							} else {
								ent.host.getData().setEndCP(end);
								PacketHandler.INSTANCE.sendToAll(new BendPacketProcessor(ent, end, false));
								mitoLogger.info("set end control point");
							}
							ent.host.isBent = true;
							nbt.setBoolean("activated", false);
						}

					}

				} else {

					List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, MitoUtil.createAabbBySize(coord, 0.1));
					List<EntityFake> list1 = new ArrayList<EntityFake>();

					for (int n = 0; n < list.size(); n++) {
						if (list.get(n) instanceof EntityFake) {
							EntityFake ent = (EntityFake) list.get(n);
							if (!ent.isRuler) {
								list1.add(ent);
							}
						}
					}
					if (list1.size() > 0) {
						nbt.setInteger("entityID", list1.get(nbt.getInteger("selectNum") % list1.size()).getEntityId());
						nbt.setBoolean("activated", true);
					}
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
