package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.ItemUsePacketProcessor;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitomain;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBar extends ItemBraceBase {

	int size = 2;
	public double[] sizeArray = { 0.2, 1.0, 3.0, 0.005 };

	public ItemBar() {
		super();
		this.setTextureName("mitomod:bar");
		this.setCreativeTab(mitomain.tab);
		this.maxStackSize = 1;
		this.setMaxDamage(3);

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

		player.setItemInUse(itemstack, 71999);
		return itemstack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int i) {

		NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			nbt.setInteger("selectNum", 0);
		}

		if (world.isRemote) {
			PacketHandler.INSTANCE.sendToServer(new ItemUsePacketProcessor(mitomain.proxy.getKey(), player.inventory.currentItem));

		}
		//}
		player.clearItemInUse();

	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

		/*NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			nbt.setInteger("selectNum", 0);
		}

		if (!world.isRemote) {

			Vec3 coord;
			boolean canAir = false;
			boolean hitEntity = false;
			MovingObjectPosition movingOP;

			boolean cKey = key.isControlPressed();

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

				//吸着
				if (!cKey && !hitEntity) {

					coord = MitoUtil.conversionByControlKey(player, coord);
				}

				double size = sizeArray[this.getDamage(itemstack)];
				boolean onlyOne = (this.getDamage(itemstack) == 3);
				boolean onlySizex1 = (this.getDamage(itemstack) == 4);
				if (this.getDamage(itemstack) >= sizeArray.length) {
					size = sizeArray[0];
				} else {
					size = sizeArray[this.getDamage(itemstack)];
				}

				//world.getBlock(movingOP.blockX, movingOP.blockY, movingOP.blockZ).setLightLevel(1.0f);

				List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, MitoUtil.createAabbBySize(coord, size));
				List<EntityFake> list1 = new ArrayList<EntityFake>();

				for (int n = 0; n < list.size(); n++) {

					if (list.get(n) instanceof EntityFake) {

						EntityFake ent = (EntityFake) list.get(n);
						if (!ent.isRuler) {
							if (onlyOne) {
								list1.add(ent);
							} else if (onlySizex1) {
								if (ent.size == 0.05) {
									ent.delete(!player.capabilities.isCreativeMode);
								}
							} else {
								ent.delete(!player.capabilities.isCreativeMode);
							}
						}

					}
				}
				if (list1.size() > 0) {
					list1.get(nbt.getInteger("selectNum") % list1.size()).delete(!player.capabilities.isCreativeMode);
				}
			}

		}*/

	}

	public void onUpdate(ItemStack itemstack, World world, Entity entity, int meta, boolean p_77663_5_) {
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks) {
		/*MovingObjectPosition mop = this.getMovingOPWithKey(itemstack, player.worldObj, player, mitomain.proxy.getKey(), true);
		if (mop == null)
			return false;
		Vec3 set = mop.hitVec;
		double size;
		if (this.getDamage(itemstack) >= this.sizeArray.length) {
			size = this.sizeArray[0];
		} else {
			size = this.sizeArray[this.getDamage(itemstack)];
		}
		boolean onlyOne = this.getDamage(itemstack) == 3;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		rh.drawBox(player, set, size, partialTicks);
		rh.highlightWithBar(player, size, set, (byte) 2, onlyOne);*/

		return true;
	}

}
