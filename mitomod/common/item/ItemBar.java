package com.mito.mitomod.common.item;

import com.mito.mitomod.BraceBase.BB_PacketProcessor;
import com.mito.mitomod.BraceBase.BB_PacketProcessor.Mode;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.entity.EntityWrapperBB;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBar extends ItemBraceBase {

	int size = 2;
	public double[] sizeArray = { 0.2, 1.0, 3.0, 0.005 };

	public ItemBar() {
		super();
		this.setTextureName("mitomod:bar");
		this.setCreativeTab(BAO_main.tab);
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

	public void RightClick(ItemStack itemstack, World world, EntityPlayer player, BB_Key key, boolean p_77663_5_) {

		mitoLogger.info("cul! x : " + MitoMath.rot(Vec3.createVectorHelper(0, 1, 0), 30, Vec3.createVectorHelper(0, 0, 1)));
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
	public boolean drawHighLightBox(ItemStack itemstack, EntityPlayer player, double partialticks, MovingObjectPosition mop) {
		return this.drawHighLightBrace(player, partialticks, mop);
	}

	public boolean wheelEvent(EntityPlayer player, ItemStack stack, BB_Key key, int dwheel) {
		if (key.isShiftPressed()) {
			MovingObjectPosition m2 = this.getMovingOPWithKey(stack, player.worldObj, player, key, Minecraft.getMinecraft().objectMouseOver, 1.0);
			if (m2 != null) {
				if (MitoUtil.isBrace(m2) && ((EntityWrapperBB)m2.entityHit).base instanceof Brace) {
					Brace brace = (Brace) ((EntityWrapperBB)m2.entityHit).base;
					int w = dwheel / 120;
					double div = brace.getRoll() + (double)w * 15;
					if (div < 0) {
						div = div + 360;
					} else if (div > 360) {
						div = div - 360;
					}
					brace.setRoll(div);
					PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, brace));
					return true;
				}
			}
		}
		return false;
	}

}
