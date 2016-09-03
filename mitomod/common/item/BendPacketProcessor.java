package com.mito.mitomod.common.item;

import com.mito.mitomod.BraceBase.BB_DataLists;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.mitoLogger;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.Vec3;

public class BendPacketProcessor implements IMessage, IMessageHandler<BendPacketProcessor, IMessage> {

	public int id;
	public double x;
	public double y;
	public double z;
	public boolean isSetCP;

	public BendPacketProcessor() {
	}

	public BendPacketProcessor(Brace brace, Vec3 v, boolean isSet) {

		this.id = brace.BBID;
		this.x = v.xCoord;
		this.y = v.yCoord;
		this.z = v.zCoord;
		this.isSetCP = isSet;
	}

	@Override
	public IMessage onMessage(BendPacketProcessor message, MessageContext ctx) {
		Vec3 end = Vec3.createVectorHelper(message.x, message.y, message.z);
		BraceBase base = BB_DataLists.getWorldData(BAO_main.proxy.getClientWorld()).getBraceBaseByID(message.id);
		if (base != null && base.isStatic && base instanceof Brace) {
			Brace brace = (Brace) base;
			if (message.isSetCP) {
				//brace.offCurvePoints1 = end;
				mitoLogger.info("bend set");
			} else {
				//brace.offCurvePoints2 = end;
				mitoLogger.info("bender end");
			}
			//brace.hasCP = true;
			brace.shouldUpdateRender = true;
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.id = buf.readInt();
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.isSetCP = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeBoolean(this.isSetCP);
	}

}
