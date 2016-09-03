package com.mito.mitomod.BraceBase;

import java.io.IOException;
import java.util.Iterator;

import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;

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
import net.minecraftforge.common.DimensionManager;

public class BB_PacketProcessor implements IMessage, IMessageHandler<BB_PacketProcessor, IMessage> {

	public enum Mode {
		REQUEST, REQUEST_CHUNK, ADD, INFORM, INFORM_COMP, DELETE, SUGGEST, BIND, SYNC;

		private Mode() {
		}
	}

	public BraceBase base;
	public int id;
	public int id2;
	public int location;
	public NBTTagCompound nbt;
	public int xChunkCoord;
	public int zChunkCoord;
	public Mode mode;
	public int DimensionID;
	//IDってNBTにはいってるんだっけ？入ってないなら・・・

	public BB_PacketProcessor() {
	}

	//mode : ADD DELETE INFORM INFORM_COMP
	public BB_PacketProcessor(Mode mode, BraceBase base) {
		this.base = base;
		this.id = base.BBID;
		this.mode = mode;
		this.xChunkCoord = MathHelper.floor_double(base.pos.xCoord / 16.0D);
		this.zChunkCoord = MathHelper.floor_double(base.pos.zCoord / 16.0D);
		this.DimensionID = base.worldObj.provider.dimensionId;
	}

	//REQUEST_CHUNK
	public BB_PacketProcessor(Mode mode, int i, int j) {
		if (mode == Mode.REQUEST_CHUNK) {
			this.mode = Mode.REQUEST_CHUNK;
			this.xChunkCoord = i;
			this.zChunkCoord = j;
		} else if (mode == Mode.BIND) {
			this.mode = Mode.BIND;
			this.id = i;
			this.id2 = j;
			this.location = 0;
		}
	}

	public BB_PacketProcessor(Mode mode, int i, int j, int id) {
		if (mode == Mode.BIND) {
			this.mode = Mode.BIND;
			this.id = i;
			this.id2 = j;
			this.location = id;
		}
	}

	//REQUEST DELETE
	public BB_PacketProcessor(Mode mode, int id) {
		this.mode = mode;
		this.id = id;
	}

	@Override
	public IMessage onMessage(BB_PacketProcessor message, MessageContext ctx) {

		if (ctx.side == Side.CLIENT) {
			World world = BAO_main.proxy.getClientWorld();
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
					BB_DataChunk chunkData = BB_DataLists.getChunkData(world, i, j);
					Iterator iterator = chunkData.braceList.iterator();
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
						dataworld.bindhelper.call(base1);
					}
				}
				break;
			case INFORM:
				break;
			case INFORM_COMP:
				break;
			case DELETE:
				BraceBase base = dataworld.getBraceBaseByID(message.id);
				mitoLogger.info("delete phase 7 client   (id : " + dataworld.toString());
				if (base != null) {
					mitoLogger.info("delete phase 8 client");
					base.removeFromWorld();
				}
				break;
				//suggest要らないかも
			case SUGGEST:
				if (world.provider.dimensionId == message.DimensionID && world.getChunkProvider().chunkExists(message.xChunkCoord, message.zChunkCoord)) {
					PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.REQUEST, message.id));
				}
				break;
			case BIND:
				mitoLogger.info("bind 2 ");
				if (world.provider.dimensionId == message.DimensionID) {
					mitoLogger.info("bind 3 ");
					BraceBase base2 = dataworld.getBraceBaseByID(message.id);
					BraceBase base3 = dataworld.getBraceBaseByID(message.id2);
					if (base2 == null || base3 == null) {
						mitoLogger.info("bind 4 ");
						dataworld.bindhelper.register(message.id, message.id2, message.location);
					} else {
						base2.addBrace(base3, message.location);
						mitoLogger.info("bind 4 not null");
					}
				}
				break;
			case SYNC:
				if (message.nbt != null) {
					message.base = BB_ResisteredList.syncBraceBaseFromNBT(message.nbt, world, message.id);
					if (message.base == null)
						mitoLogger.info("brace sync null");
				} else {
					mitoLogger.info("brace sync skipped");
				}
				break;
			default:
				break;
			}
		} else {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			World world = DimensionManager.getWorld(player.dimension);
			switch (message.mode) {
			case REQUEST:
				if (BB_DataLists.getWorldData(world).BBIDMap.containsItem(message.id)) {
					PacketHandler.INSTANCE.sendTo(new BB_PacketProcessor(Mode.ADD, (BraceBase) BB_DataLists.getWorldData(world).BBIDMap.lookup(message.id)), player);
				}
				break;
			case REQUEST_CHUNK:
				if (BB_DataLists.isChunkExist(world, message.xChunkCoord, message.zChunkCoord)) {
					Iterator iterator = BB_DataLists.getChunkData(world, message.xChunkCoord, message.zChunkCoord).braceList.iterator();
					while (iterator.hasNext()) {
						BraceBase base = (BraceBase) iterator.next();
						PacketHandler.INSTANCE.sendTo(new BB_PacketProcessor(Mode.ADD, base), player);
						mitoLogger.info("request chunk all data");
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
			case SYNC:
				if (message.nbt != null) {
					message.base = BB_ResisteredList.syncBraceBaseFromNBT(message.nbt, world, message.id);
					if (message.base == null)
						mitoLogger.info("brace sync null");
				} else {
					mitoLogger.info("brace sync skipped");
				}
				PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.ADD, message.base));
				break;
			default:
				break;
			}
		}
		return null;

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = Mode.values()[(int) buf.readByte()];

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
				this.base = BB_ResisteredList.createBraceBaseFromNBT(nbt, BAO_main.proxy.getClientWorld(), this.id);
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
		case BIND:
			this.id = buf.readInt();
			this.id2 = buf.readInt();
			this.location = buf.readInt();
			break;
		case SYNC:
			this.id = buf.readInt();
			try {
				PacketBuffer pb = new PacketBuffer(buf);
				this.nbt = pb.readNBTTagCompoundFromBuffer();
			} catch (IOException e) {
				mitoLogger.info("brace sync error");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.mode.ordinal());
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
		case BIND:
			buf.writeInt(this.id);
			buf.writeInt(this.id2);
			buf.writeInt(this.location);
			break;
		case SYNC:
			buf.writeInt(this.base.BBID);
			PacketBuffer pb1 = new PacketBuffer(buf);
			NBTTagCompound nbt1 = new NBTTagCompound();
			this.base.writeToNBTOptional(nbt1);
			try {
				pb1.writeNBTTagCompoundToBuffer(nbt1);
			} catch (IOException e) {
				mitoLogger.warn("brace sync error");
			}
			break;
		default:
			break;
		}
	}

}
