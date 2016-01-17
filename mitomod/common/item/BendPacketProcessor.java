package com.mito.mitomod.common.item;

import com.mito.mitomod.common.entity.EntityFake;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class BendPacketProcessor implements IMessage, IMessageHandler<BendPacketProcessor, IMessage> {

	public int entityID;
	public double x;
	public double y;
	public double z;
	public boolean isSetCP;

	public BendPacketProcessor() {
	}

	public BendPacketProcessor(Entity entity, Vec3 v, boolean isSet) {

		this.entityID = entity.getEntityId();
		this.x = v.xCoord;
		this.y = v.yCoord;
		this.z = v.zCoord;
		this.isSetCP = isSet;
	}

	@Override
	public IMessage onMessage(BendPacketProcessor message, MessageContext ctx) {
		EntityFake ent;
		Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityID);

		if (entity != null && entity instanceof EntityFake) {
			ent = (EntityFake) entity;
			if (message.isSetCP) {
				ent.host.setSetCP(Vec3.createVectorHelper(message.x, message.y, message.z));
			} else {
				ent.host.setEndCP(Vec3.createVectorHelper(message.x, message.y, message.z));
			}
			ent.host.isBent = true;
		}

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.isSetCP = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeBoolean(this.isSetCP);
	}

}
