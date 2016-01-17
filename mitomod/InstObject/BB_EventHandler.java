package com.mito.mitomod.InstObject;

import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.utilities.BB_MovingObjectPosition;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class BB_EventHandler {

	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load e) {
		if (e.world.isRemote) {
			LoadClientWorldHandler.INSTANCE.onLoadWorld(e);
		} else {
			LoadWorldHandler.INSTANCE.onLoadWorld(e);
		}
	}

	@SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload e) {
		if (e.world.isRemote) {
			LoadClientWorldHandler.INSTANCE.onUnloadWorld(e);
		} else {
			LoadWorldHandler.INSTANCE.onUnloadWorld(e);
		}
	}

	@SubscribeEvent
	public void onChunkDataSave(ChunkDataEvent.Save e) {
		if (e.world.isRemote) {
		} else {
			LoadWorldHandler.INSTANCE.onChunkDataSave(e);
		}
	}

	@SubscribeEvent
	public void onChunkDataLoad(ChunkDataEvent.Load e) {
		if (e.world.isRemote) {
		} else {
			LoadWorldHandler.INSTANCE.onChunkDataLoad(e);
		}

	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ServerTickEvent e) {
		if (e.phase == Phase.END) {
			LoadWorldHandler.INSTANCE.onUpdate(e);
		}
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.PlayerTickEvent e) {
		if (e.phase == Phase.END) {
			if (e.player.worldObj.isRemote)
				LoadClientWorldHandler.INSTANCE.onUpdate(e);
		}
	}

	@SubscribeEvent
	public void onWorldTickEvent(TickEvent.WorldTickEvent e) {
		if (e.phase == Phase.END) {
			LoadWorldHandler.INSTANCE.onWorldTickEvent(e);
		}
		if (e.side == Side.CLIENT)
			mitoLogger.info("" + e.getPhase());
	}

	// 重複については未処理  unload -> save

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load e) {
		if (e.world.isRemote) {
			LoadClientWorldHandler.INSTANCE.onChunkLoad(e);
		} else {
			LoadWorldHandler.INSTANCE.onChunkLoad(e);
		}
	}

	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload e) {
		if (e.world.isRemote) {
			LoadClientWorldHandler.INSTANCE.onChunkUnload(e);
		} else {
			LoadWorldHandler.INSTANCE.onChunkUnload(e);
		}
	}

	@SubscribeEvent
	public void PlayerInteractEvent(PlayerInteractEvent e) {
		if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			Vec3 start = MitoUtil.getPlayerEyePosition(e.entityPlayer, 1.0);
			Vec3 startCopy = MitoUtil.getPlayerEyePosition(e.entityPlayer, 1.0);
			double distance = 5.0;
			Vec3 vec31 = e.entityPlayer.getLook(1.0f);
			Vec3 end = start.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
			MovingObjectPosition pre = e.entityPlayer.worldObj.func_147447_a(startCopy, end, false, false, true);
			BB_MovingObjectPosition m2 = MitoUtil.rayTraceBrace(e.entityPlayer, start, end, 1.0f);
			if (m2 != null) {
				if (MitoMath.subAbs(start, pre.hitVec) + 0.05 > MitoMath.subAbs(start, m2.hitVec)) {
					e.setCanceled(true);
					if (m2.typeOfHit == BB_MovingObjectPosition.MovingObjectType.BRACEBASE) {
						m2.braceHit.rightClick(e.entityPlayer, m2.hitVec);
						PacketHandler.INSTANCE.sendToServer(new BB_ClickPacketProcessor(BB_ClickPacketProcessor.Mode.CLICK_RIGHT, m2.braceHit.BBID, m2.hitVec));
					}
				}

			}
		}
	}

}
