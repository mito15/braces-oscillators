package com.mito.mitomod.common.item;

import java.util.List;

import com.mito.mitomod.InstObject.Brace.Brace;
import com.mito.mitomod.InstObject.Brace.Figures;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.ItemUsePacketProcessor;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.utilities.BB_MovingObjectPosition;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBrace extends ItemBraceBase {

	public byte key = 0;
	public static int typeMax = 4, colorMax = 16, sizeMax = 100;
	public static int typeByte = (int) Math.pow(2, 12), colorByte = (int) Math.pow(2, 7), sizeByte = (int) Math.pow(2, 15);

	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public static final String[] colorName = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white" };
	public static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };
	public static final String[] type_name = new String[] { "square", "vertical", "horizontal", "H-section", "H", "thin1", "thin2", "thin3", "thin4" };

	public ItemBrace() {
		super();
		this.setMaxDamage(0);
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
		int isize = nbt.getInteger("size");
		int type = nbt.getInteger("type");
		int color = itemstack.getItemDamage();
		if (type < 0 || type >= this.type_name.length) {
			type = 0;
		}
		if (color < 0 || color >= this.color_name.length) {
			color = 0;
		}
		return super.getUnlocalizedName() + this.type_name[type] + "size" + isize + "_" + color_name[color];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i2 = 0; i2 < colorMax; ++i2) {
			for (int i = 0; i < 5; ++i) {
				for (int i1 = 0; i1 < typeMax; ++i1) {
					int damage1 = i2;
					ItemStack itemstack = new ItemStack(item, 1, damage1);
					NBTTagCompound nbt = new NBTTagCompound();
					itemstack.setTagCompound(nbt);
					int size = i == 4 ? 20 : (int) Math.pow(2, i);
					nbt.setInteger("size", size);
					nbt.setInteger("type", i1);
					nbt.setByte("pressedKey", (byte) 0);
					list.add(itemstack);
				}
			}
		}
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	public double getRealSize(ItemStack itemstack) {

		return convToDoubleSize(getSize(itemstack));
	}

	public double convToDoubleSize(int isize) {

		return (double) isize * 0.05;
	}

	public int getSize(ItemStack itemstack) {
		int ret = 1;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("size")) {
			ret = itemstack.getTagCompound().getInteger("size");
		}
		return ret;
	}

	public int getType(ItemStack itemstack) {
		int ret = 0;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("type")) {
			ret = itemstack.getTagCompound().getInteger("type");
		}
		return ret;
	}

	public ItemStack setSize(ItemStack itemstack, int i) {
		if (itemstack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
		}
		itemstack.getTagCompound().setInteger("size", i);
		return itemstack;
	}

	public ItemStack setType(ItemStack itemstack, int i) {
		if (itemstack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
		}
		itemstack.getTagCompound().setInteger("type", i);
		return itemstack;
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

	public BB_MovingObjectPosition getMovingOPWithKey(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, double partialticks) {

		NBTTagCompound nbt = this.getNBT(itemstack);

		Vec3 start = MitoUtil.getPlayerEyePosition(player, partialticks);
		Vec3 startCopy = Vec3.createVectorHelper(start.xCoord, start.yCoord, start.zCoord);
		double distance = key.isAltPressed() ? 3.0 : 5.0;
		Vec3 vec31 = player.getLook((float) partialticks);
		Vec3 end = startCopy.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);

		MovingObjectPosition pre = player.worldObj.func_147447_a(startCopy, end, false, false, true);
		BB_MovingObjectPosition m1 = pre == null ? null : new BB_MovingObjectPosition(pre);
		BB_MovingObjectPosition m2 = MitoUtil.rayTraceBrace(player, start, end, (float) partialticks);

		BB_MovingObjectPosition mop;

		if (m1 == null && m2 == null) {
			return null;
		} else if (m1 == null) {
			mop = m2;
		} else if (m2 == null) {
			mop = m1;
		} else {
			if (MitoMath.subAbs(start, m1.hitVec) + 0.05 < MitoMath.subAbs(start, m2.hitVec) && !(world.isAirBlock(m1.blockX, m1.blockY, m1.blockZ))) {
				mop = m1;
			} else {
				mop = m2;
			}
		}

		if (mop.typeOfHit != BB_MovingObjectPosition.MovingObjectType.BLOCK || !player.worldObj.isAirBlock(mop.blockX, mop.blockY, mop.blockZ) || key.isAltPressed()) {
			if (!key.isControlPressed()) {
				if (mop.typeOfHit == BB_MovingObjectPosition.MovingObjectType.BLOCK) {
					mop.hitVec = Vec3.createVectorHelper(Math.floor(mop.hitVec.xCoord * 2 + 0.5) / 2, Math.floor(mop.hitVec.yCoord * 2 + 0.5) / 2, Math.floor(mop.hitVec.zCoord * 2 + 0.5) / 2);
				}
				if (mop.typeOfHit == BB_MovingObjectPosition.MovingObjectType.BRACEBASE) {
					//各BraceBaseに振り分け用関数を用意
					if (mop.braceHit instanceof Brace) {
						Brace brace = (Brace) mop.braceHit;
						double r = MitoMath.subAbs(brace.pos, mop.hitVec) / MitoMath.subAbs(brace.pos, brace.end);
						//absは絶対値なので厳密にはバグがある
						if (r < 0.3333) {
							mop.hitVec = brace.pos;
						} else if (r > 0.6666) {
							mop.hitVec = brace.end;
						} else {
							mop.hitVec = MitoMath.vectorRatio(brace.pos, brace.end, 0.5);
						}
					}
				}
			}
			if (key.isShiftPressed() && nbt.getBoolean("activated")) {
				Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
				MitoUtil.conversionByShiftKey(mop, set);
			}
		} else

		{
			mop = null;
		}

		return mop;

	}

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

		NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.nbtInit(nbt, itemstack);
		}

		if (!world.isRemote) {

			BB_MovingObjectPosition movingOP = this.getMovingOPWithKey(itemstack, world, player, key, 1.0);

			if (movingOP != null) {

				if (nbt.getBoolean("activated")) {

					Vec3 end = MitoMath.copyVec3(movingOP.hitVec);
					Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));

					int type = this.getType(itemstack);
					int isize = this.getSize(itemstack);
					int color = this.getColor(itemstack);

					if (MitoMath.subAbs(set, end) < 100) {
						//EntityBrace brace = new EntityBrace(world, set, end, isize, color, (byte) type);
						//world.spawnEntityInWorld(brace);
						new Brace(world, set, end, Figures.getFigure(this.type_name[type]), (double) isize * 0.05).addToWorld();
					}

					if (!player.capabilities.isCreativeMode) {
						itemstack.stackSize--;
						if (itemstack.stackSize == 0) {
							player.destroyCurrentEquippedItem();
						}
					}

					this.nbtInit(nbt, itemstack);

				} else {

					nbt.setDouble("setX", movingOP.hitVec.xCoord);
					nbt.setDouble("setY", movingOP.hitVec.yCoord);
					nbt.setDouble("setZ", movingOP.hitVec.zCoord);

					nbt.setBoolean("activated", true);
				}
			}
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		double size = this.getRealSize(itemstack);
		BB_MovingObjectPosition mop = this.getMovingOPWithKey(itemstack, player.worldObj, player, mitomain.proxy.getKey(), partialTicks);
		if (mop == null)
			return false;
		Vec3 set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {
			Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			rh.drawFakeBrace(player, set, end, size, partialTicks);
		} else

		{
			rh.drawBox(player, set, size, partialTicks);
		}

		return true;

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
