package com.mito.mitomod.InstObject;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BB_ClickPacketProcessor implements IMessage, IMessageHandler<BB_ClickPacketProcessor, IMessage> {

	public enum Mode {
		CLICK_RIGHT(0), CLICK_LEFT(1);
		private final byte code;
		private static final Mode[] array = { Mode.CLICK_RIGHT, Mode.CLICK_LEFT };

		private Mode(int mode) {
			this.code = (byte) mode;
		}

		public static Mode CreateMode(int mode) {
			return array[mode];
		}
	}

	public int id;
	public Vec3 pos;
	public Mode mode;

	public BB_ClickPacketProcessor() {
	}

	public BB_ClickPacketProcessor(Mode mode, int id, Vec3 pos) {
		this.mode = mode;
		this.id = id;
		this.pos = pos;
	}

	@Override
	public IMessage onMessage(BB_ClickPacketProcessor message, MessageContext ctx) {

		try {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			World world = player.worldObj;
			BraceBase base = DataLists.getWorldData(world).getBraceBaseByID(message.id);
			switch (message.mode) {
			case CLICK_RIGHT:
				base.rightClick(player, message.pos);
				break;
			case CLICK_LEFT:
				base.leftClick(player, message.pos);
				break;
			default:
				break;
			}
		} finally {
		}
		return null;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = Mode.CreateMode((int) buf.readByte());
		this.id = buf.readInt();
		this.pos = Vec3.createVectorHelper(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.mode.code);
		buf.writeInt(this.id);
		buf.writeDouble(this.pos.xCoord);
		buf.writeDouble(this.pos.yCoord);
		buf.writeDouble(this.pos.zCoord);
	}

}
