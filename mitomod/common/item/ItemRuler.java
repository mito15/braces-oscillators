package com.mito.mitomod.common.item;

import java.util.List;

import com.mito.mitomod.BraceBase.Brace.Scale;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemRuler extends ItemSet {

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
	public String getItemStackDisplayName(ItemStack itemstack) {
		int isize = this.getDiv(itemstack);
		return ("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(itemstack) + ".name") + " /" + isize).trim();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean b) {
		super.addInformation(itemstack, player, list, b);
		list.add("scale : " + this.getDiv(itemstack));

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iiconregister) {
		this.icon = iiconregister.registerIcon("mitomod:ruler");
	}

	public int getDiv(ItemStack itemstack) {
		return (itemstack.getItemDamage() & (16 - 1)) + 1;
	}

	public double getRayDistance(BB_Key key){
		return key.isAltPressed() ? 3.0 : 5.0;
	}

	public void snapDegree(MovingObjectPosition mop, ItemStack itemstack, World world, EntityPlayer player, BB_Key key, NBTTagCompound nbt){
		if (nbt.getBoolean("activated")) {
			Vec3 set = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			MitoUtil.snapByShiftKey(mop, set);
		}
	}

	public void onActiveClick(World world, EntityPlayer player, ItemStack itemstack, MovingObjectPosition movingOP, Vec3 set, Vec3 end, NBTTagCompound nbt) {
		if (MitoMath.subAbs(set, end) < 100) {
			int divine = this.getDiv(itemstack);
			if (MitoMath.subAbs(set, end) < 100) {
				this.spawnEntityRuler(world, set, end, divine);
			}
		}
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialTicks, MovingObjectPosition mop) {
		NBTTagCompound nbt = getTagCompound(itemstack);
		if (mop == null)
			return false;
		Vec3 set = mop.hitVec;

		RenderHighLight rh = RenderHighLight.INSTANCE;
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {
			Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
			rh.drawRuler(player, set, end, this.getDiv(itemstack), partialTicks);
		} else {
			rh.drawCenter(player, set, partialTicks);
		}

		return true;

	}

	public void spawnEntityRuler(World world, Vec3 v1, Vec3 v2, int div) {
		Vec3 partV12 = MitoMath.vectorDiv(v2.addVector(-v1.xCoord, -v1.yCoord, -v1.zCoord), (double) div);
		for (int n = 0; n < (div + 1); n++) {
			Scale scale = new Scale(world, v1.xCoord + (double) n * partV12.xCoord, v1.yCoord + (double) n * partV12.yCoord, v1.zCoord + (double) n * partV12.zCoord);
			scale.addToWorld();
		}

	}

	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		if (key.isShiftPressed()) {
			int w = dwheel / 120;
			int div = stack.getItemDamage() + w;
			if (div < 0) {
				div = 128;
			} else if (div > 128) {
				div = 0;
			}

			stack.setItemDamage(div);
			return true;
		}
		return false;
	}

}
