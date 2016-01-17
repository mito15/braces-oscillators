package com.mito.mitomod.common.entity;

import com.mito.mitomod.common.BraceData;
import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BracePacketProcessor implements IMessage, IMessageHandler<BracePacketProcessor, IMessage> {

	public int braceid, fakeid0, fakeid1, tex, size, type;
	public Vec3 set = Vec3.createVectorHelper(0, 0, 0), setCP = Vec3.createVectorHelper(0, 0, 0), endCP = Vec3.createVectorHelper(0, 0, 0), end = Vec3.createVectorHelper(0, 0, 0);
	public boolean isBent;

	public BracePacketProcessor() {
	}

	public BracePacketProcessor(EntityBrace entity) {

		BraceData data = entity.getData();
		if (data == null) {
			this.braceid = entity.getEntityId();
			this.type = -1;
			mitoLogger.info("data is null");
		} else {

			this.braceid = entity.getEntityId();
			if (entity.pair[0] == null || entity.pair[1] == null) {

			}
			this.fakeid0 = entity.pair[0].getEntityId();
			this.fakeid1 = entity.pair[1].getEntityId();
			this.set = data.getSet();
			this.setCP = data.getSetCP();
			this.end = data.getEnd();
			this.endCP = data.getEndCP();
			this.tex = data.getTexture();
			this.size = data.getSize();
			this.type = data.getType();
			this.isBent = entity.isBent;
		}

	}

	@Override
	public IMessage onMessage(BracePacketProcessor m, MessageContext ctx) {

		EntityBrace ent;
		Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(m.braceid);

		if (entity != null && entity instanceof EntityBrace) {
			ent = (EntityBrace) entity;
			if (m.type == -1) {
			} else {
				BraceData data = new BraceData(ent, m.set, m.setCP, m.endCP, m.end, m.tex, m.type, m.size);
				ent.setData(data);
				ent.isBent = m.isBent;
				ent.setSizeAndPos(m.set, m.end, m.size);
				//LoadWorldHandler.INSTANCE.addBrace(data);
				int x = MathHelper.floor_double((m.set.xCoord + m.end.xCoord) / 2);
				int y = MathHelper.floor_double((m.set.yCoord + m.end.yCoord) / 2);
				int z = MathHelper.floor_double((m.set.zCoord + m.end.zCoord) / 2);
				Minecraft.getMinecraft().theWorld.markBlockRangeForRenderUpdate(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
			}
		}

		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.braceid = buf.readInt();
		this.fakeid0 = buf.readInt();
		this.fakeid1 = buf.readInt();
		this.tex = buf.readInt();
		this.size = buf.readInt();
		this.type = buf.readInt();
		this.set.xCoord = buf.readDouble();
		this.set.yCoord = buf.readDouble();
		this.set.zCoord = buf.readDouble();
		this.setCP.xCoord = buf.readDouble();
		this.setCP.yCoord = buf.readDouble();
		this.setCP.zCoord = buf.readDouble();
		this.end.xCoord = buf.readDouble();
		this.end.yCoord = buf.readDouble();
		this.end.zCoord = buf.readDouble();
		this.endCP.xCoord = buf.readDouble();
		this.endCP.yCoord = buf.readDouble();
		this.endCP.zCoord = buf.readDouble();
		this.isBent = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.braceid);
		buf.writeInt(this.fakeid0);
		buf.writeInt(this.fakeid1);
		buf.writeInt(this.tex);
		buf.writeInt(this.size);
		buf.writeInt(this.type);
		buf.writeDouble(set.xCoord);
		buf.writeDouble(set.yCoord);
		buf.writeDouble(set.zCoord);
		buf.writeDouble(setCP.xCoord);
		buf.writeDouble(setCP.yCoord);
		buf.writeDouble(setCP.zCoord);
		buf.writeDouble(end.xCoord);
		buf.writeDouble(end.yCoord);
		buf.writeDouble(end.zCoord);
		buf.writeDouble(endCP.xCoord);
		buf.writeDouble(endCP.yCoord);
		buf.writeDouble(endCP.zCoord);
		buf.writeBoolean(this.isBent);
	}

}
