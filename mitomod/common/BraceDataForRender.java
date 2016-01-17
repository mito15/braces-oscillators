package com.mito.mitomod.common;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.util.AxisAlignedBB;

public class BraceDataForRender {

	private int cooltime = 0;
	private List<BraceData> list = new ArrayList<BraceData>();

	public void addBrace(BraceData ent) {
		for (int n = 0; n < list.size(); n++) {
			if (ent.equals(list.get(n))) {
				list.remove(n);
			}
		}
		list.add(ent);
	}

	public List getBraceData(int x, int y, int z) {
		List<BraceData> retlist = new ArrayList<BraceData>();
		for (int n = 0; n < list.size(); n++) {
			if (list.get(n).getPos().xCoord >= (double) x && list.get(n).getPos().xCoord < (double) x + 16.0) {
				if (list.get(n).getPos().yCoord >= (double) y && list.get(n).getPos().yCoord < (double) y + 16.0) {
					if (list.get(n).getPos().zCoord >= (double) z && list.get(n).getPos().zCoord < (double) z + 16.0) {
						retlist.add(list.get(n));
					}
				}
			}
		}
		return retlist;
	}
	
	public List getBraceData(AxisAlignedBB aabb) {
		List<BraceData> retlist = new ArrayList<BraceData>();
		for (int n = 0; n < list.size(); n++) {
			if (list.get(n).getPos().xCoord >= aabb.minX && list.get(n).getPos().xCoord <= aabb.maxX) {
				if (list.get(n).getPos().yCoord >= aabb.minY && list.get(n).getPos().yCoord < aabb.maxY) {
					if (list.get(n).getPos().zCoord >= aabb.minZ && list.get(n).getPos().zCoord < aabb.maxZ) {
						retlist.add(list.get(n));
					}
				}
			}
		}
		return retlist;
	}

	public void tickEvent(TickEvent.PlayerTickEvent e) {
		cooltime++;
		if (cooltime >= 200) {
			cooltime = 0;
			double x = e.player.posX;
			double y = e.player.posY;
			double z = e.player.posZ;
			double range = 33 * 16;
			for (int n = 0; n < list.size(); n++) {
				if(list.get(n).bindEntity.isDead){
					list.remove(n);
					n--;
				} else if (list.get(n).getPos().xCoord < x - range || list.get(n).getPos().xCoord > x + range) {
					if (list.get(n).getPos().yCoord < y - range || list.get(n).getPos().yCoord > y + range) {
						if (list.get(n).getPos().zCoord < z - range || list.get(n).getPos().zCoord > z + range) {
							list.remove(n);
							n--;
						}
					}
				}
			}
		}
	}

}
