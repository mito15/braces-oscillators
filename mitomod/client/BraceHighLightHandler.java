package com.mito.mitomod.client;

import java.util.List;

import com.mito.mitomod.common.entity.EntityBrace;
import com.mito.mitomod.common.item.ItemBraceBase;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class BraceHighLightHandler {

	mitoClientProxy proxy;

	public BraceHighLightHandler(mitoClientProxy p) {

		this.proxy = p;
	}

	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
		if (e.currentItem != null && (e.currentItem.getItem() instanceof ItemBraceBase)) {
			ItemBraceBase itembrace = (ItemBraceBase)e.currentItem.getItem();
			boolean flag = itembrace.drawHighLightBox(e.currentItem, e.player, e.partialTicks);
			if(flag){
				if (e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		}
			/*Vec3 set;
			boolean airBlockSelect = false;

			MovingObjectPosition movingOP;
			boolean isCancel;
			boolean hitEntity;

			if (this.proxy.isAltKeyDown()) {

				movingOP = MitoUtil.rayTraceIncludeBrace(e.player, 3.0, e.partialTicks, this.proxy.isControlKeyDown());
				//double distance = 3.0;
				//Vec3 vec3 = e.player.getPosition(e.partialTicks);
				//Vec3 vec31 = e.player.getLook(e.partialTicks);
				//set = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
				set = movingOP.hitVec;
				airBlockSelect = true;
				isCancel = true;

			} else {

				movingOP = MitoUtil.rayTraceIncludeBrace(e.player, 5.0, e.partialTicks, this.proxy.isControlKeyDown());
				set = movingOP.hitVec;
				airBlockSelect = (movingOP.typeOfHit == MovingObjectType.ENTITY);
				isCancel = movingOP.typeOfHit == MovingObjectType.ENTITY;

			}
			hitEntity = movingOP.typeOfHit == MovingObjectType.ENTITY;

			if (!this.proxy.isControlKeyDown() && !hitEntity) {

				set = MitoUtil.conversionByControlKey(e.player, set);
			}
			//アイテムがBraceかBar、空気ブロックじゃない時描画
			ItemStack itemstack = e.currentItem;
			NBTTagCompound nbt = itemstack.getTagCompound();
			if (nbt == null) {
				nbt = new NBTTagCompound();
				;
			}
			Item item = itemstack.getItem();
			if ((!e.player.worldObj.isAirBlock(e.target.blockX, e.target.blockY, e.target.blockZ)) || airBlockSelect) {
				if (item instanceof ItemBrace) {

					ItemBrace brace = (ItemBrace) itemstack.getItem();
					double size = brace.getRealSize(itemstack);
					MovingObjectPosition mop = brace.getMovingOPWithKey(itemstack, e.player.worldObj, e.player, mitomain.proxy.getKeyByte(), true);
					if(mop == null)return;
					set = mop.hitVec;

					if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {
						Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
						drawFakeBrace(e, set, end, size);
					} else {
						drawBox(e, set, size);
					}
					if (e.isCancelable() && isCancel) {
						e.setCanceled(true);
					}
					this.transparentize(size, set, e.player.worldObj, (byte) 1);

				} else if (item instanceof ItemBar) {

					ItemBar itembar = (ItemBar) item;
					double size;
					if (itembar.getDamage(itemstack) >= itembar.sizeArray.length) {
						size = itembar.sizeArray[0];
					} else {
						size = itembar.sizeArray[itembar.getDamage(itemstack)];
					}
					boolean onlyOne = itembar.getDamage(itemstack) == 3;

					drawBox(e, set, size);
					this.highlightWithBar(size, set, e, (byte) 2, onlyOne);

					if (e.isCancelable() && isCancel) {
						e.setCanceled(true);
					}

				} else if (item instanceof ItemBender) {
					ItemBender itembender = (ItemBender) item;

					if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {

						Entity entity1 = e.player.worldObj.getEntityByID(nbt.getInteger("entityID"));
						Vec3 end;
						if (entity1 != null) {
							end = Vec3.createVectorHelper(entity1.posX, entity1.posY, entity1.posZ);
						} else {
							end = Vec3.createVectorHelper(0, 0, 0);
						}
						drawFakeBrace(e, set, end, 0.2);
					} else {

						drawBox(e, set, 0.2);
						this.highlightWithBar(0.5, set, e, (byte) 2, true);

						if (e.isCancelable() && isCancel) {
							e.setCanceled(true);
						}
					}

					if (e.isCancelable() && isCancel) {
						e.setCanceled(true);
					}
				} else if (item instanceof ItemWall) {
					ItemWall itembender = (ItemWall) item;

					if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {

						//軸平行
						if (this.proxy.isShiftKeyDown()) {
							set = MitoUtil.conversionByShiftKey(e.player, set, itemstack);
						}

						Entity entity1 = e.player.worldObj.getEntityByID(nbt.getInteger("setEntity"));
						Vec3 end;
						if (entity1 != null) {
							end = Vec3.createVectorHelper(entity1.posX, entity1.posY, entity1.posZ);
						} else {
							end = Vec3.createVectorHelper(0, 0, 0);
						}

						drawFakeBrace(e, set, end, 0.2);
						//drawCenter(e, set, brace.size + 0.03);
					} else {

						drawBox(e, set, 0.2);
						//drawCenter(e, set, brace.size + 0.03);

						if (e.isCancelable() && isCancel) {
							e.setCanceled(true);
						}
					}
					this.highlightWithBar(0.5, set, e, (byte) 2, true);

					if (e.isCancelable() && isCancel) {
						e.setCanceled(true);
					}
				} else if (item instanceof ItemRuler) {

					ItemRuler brace = (ItemRuler) itemstack.getItem();

					if (itemstack.getTagCompound() != null && itemstack.getTagCompound().getBoolean("activated")) {

						//軸平行
						if (this.proxy.isShiftKeyDown()) {
							set = MitoUtil.conversionByShiftKey(e.player, set, itemstack);
						}

						Vec3 end = Vec3.createVectorHelper(nbt.getDouble("setX"), nbt.getDouble("setY"), nbt.getDouble("setZ"));
						double div = brace.getDiv(itemstack);
						drawRuler(e, set, end, div);
						//drawCenter(e, set, brace.size + 0.03);
					} else {

						drawCenter(e, set);
						//drawCenter(e, set, brace.size + 0.03);

						if (e.isCancelable() && isCancel) {
							e.setCanceled(true);
						}
					}

					this.transparentize(0.1, set, e.player.worldObj, (byte) 1);

				}
			}
		}*/
	}


	private void transparentize(double s, Vec3 c, World world, byte f) {
		List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, MitoUtil.createAabbBySize(c, s));

		for (int n = 0; n < list.size(); n++) {
			if (list.get(n) instanceof EntityBrace) {
				EntityBrace ent = (EntityBrace) list.get(n);

				if (MitoMath.subAbs(MitoMath.getNearPoint(ent.getSet(), ent.getEnd(), c), c) < 0.5) {
					ent.flags = (byte) (ent.flags | f);
				}
			}
		}
	}

}
