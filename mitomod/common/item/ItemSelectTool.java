package com.mito.mitomod.common.item;

import java.util.List;

import com.mito.mitomod.BraceBase.BB_DataLists;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.client.BB_SelectedGroup;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.common.item.GroupPacketProcessor.EnumGroupMode;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemSelectTool extends ItemBraceBase {

	public ItemSelectTool() {
		super();
		this.setTextureName("mitomod:select");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		if (world.isRemote) {
			NBTTagCompound nbt = getTagCompound(itemstack);
			MovingObjectPosition movingOP = Minecraft.getMinecraft().objectMouseOver;
			boolean flag = MitoUtil.isBrace(movingOP);
			BB_SelectedGroup sel = BAO_main.proxy.sg;
			MovingObjectPosition mop = this.getMovingOPWithKey(itemstack, world, player, BAO_main.proxy.getKey(), Minecraft.getMinecraft().objectMouseOver, 1.0);

			if (sel.ismove) {
				if (player.isSneaking()) {
					sel.delete();
				} else {
					if (!sel.getList().isEmpty()) {
						Vec3 pos = sel.getDistance(mop);
						double yaw = 0;
						PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.COPY, sel.getList(), pos, yaw));
						sel.breakGroup();
						sel.delete();
					}
				}
				sel.setmove(false);
				sel.activated = false;
			} else if (sel.iscopy()) {
				if (player.isSneaking()) {
					sel.delete();
				} else {
					if (!sel.getList().isEmpty()) {
						Vec3 pos = sel.getDistance(mop);
						double yaw = 0;
						PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.COPY, sel.getList(), pos, yaw));
					}
				}
				sel.setcopy(false);
				sel.activated = false;
			} else {
				if (sel.activated) {
					Vec3 set = mop.hitVec;
					if (MitoMath.subAbs(sel.set, set) < 5000) {
						AxisAlignedBB aabb = MitoUtil.createAABBByVec3(sel.set, set);
						List<BraceBase> list = BB_DataLists.getWorldData(world).getBraceBaseWithAABB(aabb);
						if (player.isSneaking()) {
							sel.addShift(list);
						} else {
							sel.replace(list);
						}
						sel.activated = false;
					}
				} else {
					if (flag) {
						BraceBase base = ((EntityWrapperBB) movingOP.entityHit).base;
						if (player.isSneaking()) {
							sel.addShift(base);
						} else {
							if (sel.getList().contains(base)) {
								//GUI
								if (BAO_main.proxy.getKey().isControlPressed()) {
									sel.set = mop.hitVec;
									sel.setcopy(true);
									//PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.COPY, sel.getList()));
								} else {
									sel.set = mop.hitVec;
									PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.GUI, sel.getList()));
								}
							} else {
								sel.replace(base);
							}
						}
						sel.activated = false;
					} else {
						if (player.isSneaking()) {
							sel.delete();
						} else {
							Vec3 set = mop.hitVec;
							sel.set = set;
							sel.activated = true;
						}
					}
				}
			}
		}
		return itemstack;
	}

	public double getRayDistance(BB_Key key) {
		return key.isAltPressed() ? 3.0 : 5.0;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int i) {
	}

	@Override
	public boolean drawHighLightBox(ItemStack itemStack, EntityPlayer player, double partialticks, MovingObjectPosition mop) {
		if (Minecraft.getMinecraft().currentScreen == null) {
			BB_SelectedGroup sel = BAO_main.proxy.sg;
			if (mop == null)
				return false;
			Vec3 set = mop.hitVec;
			if (sel.iscopy() || sel.ismove) {
				sel.drawHighLightCopy(player, partialticks, mop);
			}
			sel.drawHighLightGroup(player, partialticks);
			RenderHighLight rh = RenderHighLight.INSTANCE;
			if (sel.activated && MitoUtil.canClick(player.worldObj, BAO_main.proxy.getKey(), mop)) {
				Vec3 end = sel.set;
				rh.drawBox(player, set, end, partialticks);
				return true;
			} else {
				return this.drawHighLightBrace(player, partialticks, mop);
			}
		}
		return false;
	}

	@Override
	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		BB_SelectedGroup sel = BAO_main.proxy.sg;
		if (sel.iscopy() && key.isShiftPressed()) {
			int w = dwheel / 120;
			double div = sel.pasteNum + w;
			if (sel.pasteNum < 0) {
				sel.pasteNum = 50000;
			}
			return true;
		}
		return false;
	}
}
