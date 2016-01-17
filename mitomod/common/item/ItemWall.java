package com.mito.mitomod.common.item;

import java.util.List;

import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.ItemUsePacketProcessor;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitomain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemWall extends ItemBraceBase {

	public byte key = 0;
	public static int typeMax = 9, colorMax = 27, sizeMax = 100;
	public static int typeByte = (int) Math.pow(2, 12), colorByte = (int) Math.pow(2, 7), sizeByte = (int) Math.pow(2, 15);

	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public static final String[] colorName = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white" };
	public static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white", "oak", "spruce", "birch",
			"jungle", "acacia", "big_oak", "stone", "cobblestone", "stonebrick", "quartzbrock", "end_stone" };
	public static final String[] type_name = new String[] { "", "C", "noJ", "noJC ", "H", "thin1", "thin2", "thin3", "thin4" };

	public ItemWall() {
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return iconArray[par1 % iconArray.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iiconregister) {
		this.iconArray = new IIcon[16];

		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = iiconregister.registerIcon("mitomod:brace_" + this.colorName[i]);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		int color = itemstack.getItemDamage();
		if (color < 0 || color >= this.color_name.length) {
			color = 0;
		}
		return super.getUnlocalizedName() + "_" + color_name[color];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i2 = 0; i2 < colorMax; ++i2) {
			int damage1 = i2;
			ItemStack itemstack = new ItemStack(item, 1, damage1);
			list.add(itemstack);
		}
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	public int getColor(ItemStack itemstack) {
		return itemstack.getItemDamage() & (colorByte - 1);
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

		if (!world.isRemote && nbt != null) {
			PacketHandler.INSTANCE.sendToServer(new ItemUsePacketProcessor(mitomain.proxy.getKey(), player.inventory.currentItem));
		}
		player.clearItemInUse();

	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {

		nbt.setBoolean("activated", false);
		nbt.setInteger("setEntity", -1);

	}

	public void getMovingOP(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

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
			boolean hitEntity;
			MovingObjectPosition movingOP;

			movingOP = MitoUtil.rayTraceIncludeBrace(player, 5.0, 1.0f, false);
			coord = movingOP.hitVec;

			hitEntity = (movingOP.typeOfHit == MovingObjectType.ENTITY);

			if (hitEntity) {
				EntityBrace ent = null;
				boolean flag3 = false;
				if (movingOP.entityHit instanceof EntityBrace) {
					ent = (EntityBrace) movingOP.entityHit;
					flag3 = true;
				} else if (movingOP.entityHit instanceof EntityFake) {
					ent = ((EntityFake) movingOP.entityHit).host;
					flag3 = true;
				}
				if (flag3) {
					if (nbt.getBoolean("activated")) {

						EntityBrace set = (EntityBrace) world.getEntityByID(nbt.getInteger("setEntity"));

						int color = this.getColor(itemstack);

						EntityWall wall = new EntityWall(world, set, ent, color);
						world.spawnEntityInWorld(wall);

						if (!player.capabilities.isCreativeMode) {

							itemstack.stackSize--;
							if (itemstack.stackSize == 0) {
								player.destroyCurrentEquippedItem();
							}
						}

						this.nbtInit(nbt, itemstack);

					} else {

						nbt.setInteger("setEntity", ent.getEntityId());

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
