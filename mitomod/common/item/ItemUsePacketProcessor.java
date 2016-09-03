package com.mito.mitomod.common.item;

import com.mito.mitomod.BraceBase.BB_DataLists;
import com.mito.mitomod.client.BB_Key;
import com.mito.mitomod.common.entity.EntityWrapperBB;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemUsePacketProcessor implements IMessage, IMessageHandler<ItemUsePacketProcessor, IMessage> {

	public int slot;
	public BB_Key key;
	public MovingObjectPosition mop;
	public boolean wrap = false;
	public int id = -1;
	public Vec3 vec;

	public ItemUsePacketProcessor() {
	}

	public ItemUsePacketProcessor(BB_Key keyPressed, int slot) {
		this.key = keyPressed;
		this.slot = slot;
		this.mop = Minecraft.getMinecraft().objectMouseOver;
	}

	@Override
	public IMessage onMessage(ItemUsePacketProcessor message, MessageContext ctx) {
		ItemStack itemstack = null;
		if (message.slot > -1 && message.slot < 9) {
			itemstack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.slot);
		}
		World world = ctx.getServerHandler().playerEntity.worldObj;
		if (itemstack != null) {
			if (itemstack.getItem() != null && itemstack.getItem() instanceof ItemBraceBase) {
				MovingObjectPosition mop1 = message.mop;
				if (mop1 == null && message.id != -1 && message.vec != null) {
					Entity ent;
					if (message.wrap) {
						ent = new EntityWrapperBB(world, BB_DataLists.getWorldData(world).getBraceBaseByID(message.id));
					} else {
						ent = world.getEntityByID(message.id);
					}
					if (ent != null)
						mop1 = new MovingObjectPosition(ent, message.vec);
				}
				ItemBraceBase item = (ItemBraceBase) itemstack.getItem();
				item.RightClick(itemstack, ctx.getServerHandler().playerEntity.worldObj, ctx.getServerHandler().playerEntity, mop1, message.key, true);
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = new BB_Key(buf.readByte());
		this.slot = buf.readInt();
		if (buf.readBoolean()) {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			Vec3 vec3 = Vec3.createVectorHelper(x, y, z);
			MovingObjectType type = MovingObjectType.values()[buf.readInt()];
			switch (type) {
			case BLOCK:
				int i = buf.readInt();
				int i0 = buf.readInt();
				int i1 = buf.readInt();
				int i2 = buf.readInt();
				this.mop = new MovingObjectPosition(i, i0, i1, i2, vec3);
				break;
			case ENTITY:
				this.wrap = buf.readBoolean();
				this.id = buf.readInt();
				this.vec = vec3;
				this.mop = null;
				break;
			case MISS:
				int i3 = buf.readInt();
				int i4 = buf.readInt();
				int i5 = buf.readInt();
				int i6 = buf.readInt();
				this.mop = new MovingObjectPosition(i3, i4, i5, i6, vec3, false);
				break;
			default:
				this.mop = null;
				break;
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.key.ikey);
		buf.writeInt(this.slot);
		buf.writeBoolean(mop != null);
		if (mop != null) {
			buf.writeDouble(mop.hitVec.xCoord);
			buf.writeDouble(mop.hitVec.yCoord);
			buf.writeDouble(mop.hitVec.zCoord);
			buf.writeInt(mop.typeOfHit.ordinal());
			switch (mop.typeOfHit) {
			case BLOCK:
				buf.writeInt(mop.blockX);
				buf.writeInt(mop.blockY);
				buf.writeInt(mop.blockZ);
				buf.writeInt(mop.sideHit);
				break;
			case ENTITY:
				if (mop.entityHit != null) {
					if (mop.entityHit instanceof EntityWrapperBB) {
						buf.writeBoolean(true);
						if (((EntityWrapperBB) mop.entityHit).base != null) {
							buf.writeInt(((EntityWrapperBB) mop.entityHit).base.BBID);
						} else {
							buf.writeInt(-1);
						}
					} else {
						buf.writeBoolean(false);
						buf.writeInt(mop.entityHit.getEntityId());
					}
				} else {
					buf.writeBoolean(false);
					buf.writeInt(-1);
				}
				break;
			case MISS:
				buf.writeInt(mop.blockX);
				buf.writeInt(mop.blockY);
				buf.writeInt(mop.blockZ);
				buf.writeInt(mop.sideHit);
				break;
			default:
				break;
			}
		}
	}

}
