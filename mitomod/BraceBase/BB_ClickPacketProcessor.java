package com.mito.mitomod.BraceBase;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

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
			World world = DimensionManager.getWorld(player.dimension);
			BB_DataWorld data = BB_DataLists.getWorldData(world);
			BraceBase base = data.getBraceBaseByID(message.id);
			if (base != null && player != null) {
				switch (message.mode) {
				case CLICK_RIGHT:
					base.rightClick(player, message.pos, player.getCurrentEquippedItem());
					break;
				case CLICK_LEFT:
					base.leftClick(player, player.getCurrentEquippedItem());
					break;
				default:
					break;
				}
			}
		} finally {
		}
		return null;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = Mode.CreateMode((int) buf.readByte());
		this.id = buf.readInt();
		this.pos = Vec3.createVectorHelper(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.mode.code);
		buf.writeInt(this.id);
		buf.writeFloat((float)this.pos.xCoord);
		buf.writeFloat((float)this.pos.yCoord);
		buf.writeFloat((float)this.pos.zCoord);
	}

}
