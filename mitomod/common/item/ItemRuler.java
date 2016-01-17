package com.mito.mitomod.common.item;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.ItemUsePacketProcessor;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.common.entity.EntityFake;
import com.mito.mitomod.utilities.MitoMath;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemRuler extends ItemBraceBase {

	public byte key = 0;

	@SideOnly(Side.CLIENT)
	private IIcon icon;

	public ItemRuler() {
		super();
		this.setMaxDamage(0);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {

		if (itemstack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
		}
		itemstack.getTagCompound().setByte("pressedKey", (byte) 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return icon;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return super.getUnlocalizedName() + this.getDiv(itemstack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iiconregister) {
		this.icon = iiconregister.registerIcon("mitomod:ruler");
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	public int getDiv(ItemStack itemstack) {
		return (itemstack.getItemDamage() & (16 - 1)) + 1;
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
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);

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

					Vec3 end = coord;
					Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));

					//shiftKey軸平行
					if (key.isShiftPressed()) {

						end = MitoUtil.conversionByShiftKey(player, end, itemstack);

					}
					int divine = this.getDiv(itemstack);

					if (MitoMath.subAbs(set, end) < 100) {
						this.spawnEntityRuler(world, set, end, divine);
					}

					if (!player.capabilities.isCreativeMode) {

						itemstack.stackSize--;
						if (itemstack.stackSize == 0) {
							player.destroyCurrentEquippedItem();
						}
					}

					this.nbtInit(nbt, itemstack);

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

	public void spawnEntityRuler(World world, Vec3 v1, Vec3 v2, int div) {
		Vec3 partV12 = MitoMath.vectorDiv(v2.addVector(-v1.xCoord, -v1.yCoord, -v1.zCoord), (double) div);
		for (int n = 0; n < (div + 1); n++) {
			EntityFake fake = new EntityFake(world, v1.xCoord + (double) n * partV12.xCoord, v1.yCoord + (double) n * partV12.yCoord, v1.zCoord + (double) n * partV12.zCoord);
			world.spawnEntityInWorld(fake);
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
