package com.mito.mitomod.common.entity;

import com.mito.mitomod.common.PacketHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class BraceSyncCall implements IMessage, IMessageHandler<BraceSyncCall, IMessage> {

	public int braceid;

	public BraceSyncCall() {
	}

	public BraceSyncCall(EntityBrace entity) {

		this.braceid = entity.getEntityId();
	}

	@Override
	public IMessage onMessage(BraceSyncCall m, MessageContext ctx) {
		
		Entity entity = ctx.getServerHandler().playerEntity.worldObj.getEntityByID(m.braceid);

		
		if (entity != null && entity instanceof EntityBrace) {
			PacketHandler.INSTANCE.sendToAll(new BracePacketProcessor((EntityBrace)entity));
		}

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.braceid = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.braceid);
	}

}
