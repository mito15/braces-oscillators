package com.mito.mitomod.InstObject;

import java.io.IOException;
import java.util.Iterator;

import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.mitomain;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BB_PacketProcessor implements IMessage, IMessageHandler<BB_PacketProcessor, IMessage> {

	public enum Mode {
		REQUEST(0), REQUEST_CHUNK(1), ADD(2), INFORM(3), INFORM_COMP(4), DELETE(5), SUGGEST(6);
		private final byte code;
		private static final Mode[] array = { Mode.REQUEST, Mode.REQUEST_CHUNK, Mode.ADD, Mode.INFORM, Mode.INFORM_COMP, Mode.DELETE, Mode.SUGGEST };

		private Mode(int mode) {
			this.code = (byte) mode;
		}

		public static Mode CreateMode(int mode) {
			return array[mode];
		}
	}

	public BraceBase base;
	public int id;
	public NBTTagCompound nbt;
	public int xChunkCoord;
	public int zChunkCoord;
	public Mode mode;
	public int DimensionID;
	//IDってNBTにはいってるんだっけ？入ってないなら・・・

	public BB_PacketProcessor() {
	}

	//mode : ADD DELETE INFORM INFORM_COMP
	public BB_PacketProcessor(Mode mode, BraceBase fo) {
		this.base = fo;
		this.id = fo.BBID;
		this.mode = mode;
		this.xChunkCoord = MathHelper.floor_double(fo.pos.xCoord / 16.0D);
		this.zChunkCoord = MathHelper.floor_double(fo.pos.zCoord / 16.0D);
		this.DimensionID = fo.worldObj.provider.dimensionId;
	}

	//REQUEST_CHUNK
	public BB_PacketProcessor(Mode mode, int i, int j) {
		this.mode = Mode.REQUEST_CHUNK;
		this.xChunkCoord = i;
		this.zChunkCoord = j;
	}

	//REQUEST DELETE
	public BB_PacketProcessor(Mode mode, int id) {
		this.mode = mode;
		this.id = id;
	}

	@Override
	public IMessage onMessage(BB_PacketProcessor message, MessageContext ctx) {

		if (ctx.side == Side.CLIENT) {
			World world = mitomain.proxy.getClientWorld();
			BB_DataWorld dataworld = LoadClientWorldHandler.INSTANCE.data;
			switch (message.mode) {
			case REQUEST:
				break;
			case REQUEST_CHUNK:
				break;
			case ADD:
				BraceBase base1 = message.base;
				if (base1 != null) {
					int i = MathHelper.floor_double(base1.pos.xCoord / 16.0D);
					int j = MathHelper.floor_double(base1.pos.zCoord / 16.0D);
					BB_DataChunk chunkData = DataLists.getChunkData(world, i, j);
					Iterator iterator = chunkData.list.iterator();
					boolean flag = true;
					while (iterator.hasNext()) {
						BraceBase fobj = (BraceBase) iterator.next();
						if (fobj.BBID == message.id) {
							mitoLogger.info("on sync chohuku");
							flag = false;
							break;
						}
					}
					if (flag) {
						base1.addToWorld();
					}
				}
				break;
			case INFORM:
				break;
			case INFORM_COMP:
				break;
			case DELETE:
				BraceBase base = dataworld.getBraceBaseByID(id);
				if (base != null) {
					base.setDead();
				}
				break;
			case SUGGEST:
				if (world.provider.dimensionId == message.DimensionID && world.getChunkProvider().chunkExists(message.xChunkCoord, message.zChunkCoord)) {
					PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.REQUEST, message.id));
				}
				break;
			default:
				break;
			}
		} else

		{
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			World world = player.worldObj;
			switch (message.mode) {
			case REQUEST:
				if (BraceBase.BBIDMap.containsItem(message.id)) {
					PacketHandler.INSTANCE.sendTo(new BB_PacketProcessor(Mode.ADD, (BraceBase) BraceBase.BBIDMap.lookup(message.id)), player);
				}
				break;
			case REQUEST_CHUNK:
				if (DataLists.isChunkExist(world, message.xChunkCoord, message.zChunkCoord)) {
					Iterator iterator = DataLists.getChunkData(world, message.xChunkCoord, message.zChunkCoord).list.iterator();
					while (iterator.hasNext()) {
						BraceBase base = (BraceBase) iterator.next();
						PacketHandler.INSTANCE.sendTo(new BB_PacketProcessor(Mode.ADD, base), player);
					}
				}
				break;
			case ADD:
				break;
			case INFORM:
				break;
			case INFORM_COMP:
				break;
			case DELETE:
				break;
			case SUGGEST:
				break;
			default:
				break;
			}
		}
		return null;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = Mode.CreateMode((int) buf.readByte());

		switch (this.mode) {
		case REQUEST:
			this.id = buf.readInt();
			break;
		case REQUEST_CHUNK:
			this.xChunkCoord = buf.readInt();
			this.zChunkCoord = buf.readInt();
			break;
		case ADD:
			this.id = buf.readInt();
			try {
				PacketBuffer pb = new PacketBuffer(buf);
				this.nbt = pb.readNBTTagCompoundFromBuffer();
			} catch (IOException e) {
				mitoLogger.info("brace sync error");
			}
			if (this.nbt != null) {
				this.base = BB_ResisteredList.createBraceBaseFromNBT(nbt, mitomain.proxy.getClientWorld(), this.id);
				if (this.base == null)
					mitoLogger.info("brace sync null");
			} else {
				mitoLogger.info("brace sync skipped");
			}
			break;
		case INFORM:
			break;
		case INFORM_COMP:
			break;
		case DELETE:
			this.id = buf.readInt();
			break;
		case SUGGEST:
			this.id = buf.readInt();
			this.DimensionID = buf.readInt();
			this.xChunkCoord = buf.readInt();
			this.zChunkCoord = buf.readInt();
			break;
		default:
			break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.mode.code);
		switch (this.mode) {
		case REQUEST:
			buf.writeInt(this.id);
			break;
		case REQUEST_CHUNK:
			buf.writeInt(this.xChunkCoord);
			buf.writeInt(this.zChunkCoord);
			break;
		case ADD:
			buf.writeInt(this.base.BBID);
			PacketBuffer pb = new PacketBuffer(buf);
			NBTTagCompound nbt = this.base.getNBTTagCompound();
			Iterator iterator = nbt.func_150296_c().iterator();
			try {
				pb.writeNBTTagCompoundToBuffer(nbt);
			} catch (IOException e) {
				mitoLogger.info("brace sync error");
			}
			break;
		case INFORM:
			buf.writeInt(this.id);
			break;
		case INFORM_COMP:
			buf.writeInt(this.id);
			break;
		case DELETE:
			buf.writeInt(this.id);
			break;
		case SUGGEST:
			buf.writeInt(this.id);
			buf.writeInt(this.DimensionID);
			buf.writeInt(this.xChunkCoord);
			buf.writeInt(this.zChunkCoord);
			break;
		default:
			break;
		}
	}

}
