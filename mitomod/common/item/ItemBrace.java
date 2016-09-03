package com.mito.mitomod.common.item;

import java.util.List;

import com.mito.mitomod.BraceBase.BB_EnumTexture;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.BraceBase.Brace.Render.BB_TypeResister;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBrace extends ItemSet {

	public byte key = 0;
	public static int colorMax = 16, sizeMax = 100;

	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;

	public static final String[] colorName = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white" };
	public static final String[] color_name = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };

	public ItemBrace() {
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
	public String getItemStackDisplayName(ItemStack itemstack) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		int isize = nbt.getInteger("size");
		return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(itemstack) + ".name") + " x" + isize).trim();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean b) {
		super.addInformation(itemstack, player, list, b);
		NBTTagCompound nbt = getTagCompound(itemstack);
		list.add("size : " + nbt.getInteger("size"));
		list.add("type : " + this.getType(itemstack));
		list.add("texture : " + this.getMaterial(itemstack).getTextureName(this.getColor(itemstack)));

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i2 = 0; i2 < BB_EnumTexture.values().length; ++i2) {
			for (int i3 = 0; i3 < (BB_EnumTexture.values()[i2].hasColor ? 16 : 1); i3++) {
				for (int i1 = 0; i1 < BB_TypeResister.shapeList.size(); ++i1) {
					ItemStack itemstack = new ItemStack(item, 1, i3);
					NBTTagCompound nbt = new NBTTagCompound();
					itemstack.setTagCompound(nbt);
					this.setSize(itemstack, 5);
					this.setType(itemstack, BB_TypeResister.shapeList.get(i1));
					nbt.setInteger("material", i2);
					list.add(itemstack);
				}
			}
		}
		for (int i1 = 0; i1 < BB_TypeResister.patternList.size(); ++i1) {
			ItemStack itemstack = new ItemStack(item, 1, 0);
			NBTTagCompound nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
			this.setSize(itemstack, 5);
			this.setType(itemstack, BB_TypeResister.patternList.get(i1));
			nbt.setInteger("material", 0);
			list.add(itemstack);
		}
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

	public String getType(ItemStack itemstack) {
		String ret = "square";
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("stype")) {
			ret = itemstack.getTagCompound().getString("stype");
		}
		return ret;
	}

	public BB_EnumTexture getMaterial(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("material")) {
			return BB_EnumTexture.values()[itemstack.getTagCompound().getInteger("material")];
		} else {
			return BB_EnumTexture.IRON;
		}
	}

	public ItemStack setSize(ItemStack itemstack, int i) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		nbt.setInteger("size", i);
		return itemstack;
	}

	public ItemStack setType(ItemStack itemstack, String i) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		nbt.setString("stype", i);
		return itemstack;
	}

	public ItemStack setMaterial(ItemStack itemstack, BB_EnumTexture e) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		nbt.setInteger("material", e.ordinal());
		return itemstack;
	}

	public int getColor(ItemStack itemstack) {
		return itemstack.getItemDamage() & (16 - 1);
	}

	public void nbtInit(NBTTagCompound nbt, ItemStack itemstack) {
		super.nbtInit(nbt, itemstack);
		nbt.setInteger("brace", -1);
	}

	public double getRayDistance(BB_Key key) {
		return key.isAltPressed() ? 3.0 : 5.0;
	}

	public void snapDegree(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt) {
		if (nbt.getBoolean("activated")) {
			Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			MitoUtil.snapByShiftKey(mop, set);
		}
	}

	public void activate(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition mop, NBTTagCompound nbt) {
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {
			nbt.setInteger("brace", ((EntityWrapperBB) mop.entityHit).base.BBID);
		}
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition movingOP, Vec3 set, Vec3 end, NBTTagCompound nbt) {
		int color = this.getColor(itemstack);
		if (MitoMath.subAbs(set, end) < 100) {
			Brace brace = new Brace(world, set, end, BB_TypeResister.getFigure(this.getType(itemstack)), this.getMaterial(itemstack), this.getColor(itemstack), this.getRealSize(itemstack));
			brace.addToWorld();
			//EntityBrace brace = new EntityBrace(world, set, end, this.getSize(itemstack), color, (byte)1);
			//world.spawnEntityInWorld(brace);
			/*if (movingOP.typeOfHit == BB_MovingObjectPosition.MovingObjectType.BRACEBASE && movingOP.braceHit != null && movingOP.braceHit.isStatic) {
				BraceBase base = movingOP.braceHit;
				brace.connectBrace(base);
			}
			BraceBase base = BB_DataLists.getWorldData(world).getBraceBaseByID(nbt.getInteger("brace"));
			if (base != null && base.isStatic) {
				brace.connectBrace(base);
			}*/
		}
		if (!player.capabilities.isCreativeMode) {
			itemstack.stackSize--;
			if (itemstack.stackSize == 0) {
				player.destroyCurrentEquippedItem();
			}
		}
	}

	@Override
	public void clientProcess(MovingObjectPosition mop, ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null && nbt.getBoolean("activated")) {
			Vec3 pos = mop.hitVec;
			BB_EnumTexture texture = getMaterial(itemstack);
			BAO_main.proxy.playSound(new ResourceLocation(texture.getBreakSound()), texture.getVolume(), texture.getPitch(), (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks, MovingObjectPosition mop) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		double size = this.getRealSize(itemstack);
		if (mop == null || !MitoUtil.canClick(player.worldObj, BAO_main.proxy.getKey(), mop))
			return false;
		Vec3 set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (nbt.getBoolean("activated")) {
			Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			rh.drawFakeBrace(player, set, end, size, partialTicks);
		} else {
			BraceBase base = null;
			if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityWrapperBB) {
				base = ((EntityWrapperBB) mop.entityHit).base;
			}
			if (base != null && base instanceof Brace && size < ((Brace) base).size) {
				rh.drawCenter(player, set, ((Brace) base).size / 2 + 0.1, partialTicks);
			} else {
				rh.drawBox(player, set, size, partialTicks);
			}
		}

		return true;

	}

	public ResourceLocation getResourceLocation(ItemStack itemstack) {
		return this.getMaterial(itemstack).getResourceLocation(this.getColor(itemstack));
	}

	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		if (key.isShiftPressed()) {
			ItemBrace brace = (ItemBrace) BAO_main.ItemBrace;
			int w = dwheel / 120;
			int size = brace.getSize(stack) + w;
			if (size > brace.sizeMax) {
				size = brace.sizeMax;
			} else if (size < 1) {
				size = 1;
			}

			brace.setSize(stack, size);
			return true;
		}
		return false;
	}

}
