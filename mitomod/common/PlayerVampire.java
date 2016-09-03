package com.mito.mitomod.common;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;

public class PlayerVampire {

	public static final NewDamageSource sunBurn = new NewDamageSource("was killed by the sun");

	public static void sunFire(TickEvent.PlayerTickEvent event) {
		if (BAO_main.INSTANCE.vampmode) {
			WorldServer overworldInst = MinecraftServer.getServer().worldServers[0];
			boolean daytime = overworldInst.isDaytime();
			boolean rain = overworldInst.isRaining();
			EntityPlayer par3EntityPlayer = event.player;
			Vec3 playerPosi = par3EntityPlayer.getPosition(0);
			int x = (int) playerPosi.xCoord, y = (int) playerPosi.yCoord, z = (int) playerPosi.zCoord;

			boolean isServer = event.side.isServer();

			if (isServer) {
				y = y + 1;
			}
			boolean sky = overworldInst.canBlockSeeTheSky(x, y, z);

			boolean playerInWater = par3EntityPlayer.isInWater();

			if (sky && !rain && daytime) {
				par3EntityPlayer.attackEntityFrom(sunBurn, 1.0F);

				if (!playerInWater) {
					par3EntityPlayer.setFire(1);
				}
			}
		}
	}
}
