package com.mito.mitomod.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BB_EnumTexture;
import com.mito.mitomod.BraceBase.BB_PacketProcessor;
import com.mito.mitomod.BraceBase.BB_PacketProcessor.Mode;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.client.render.model.IDrawBrace;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.item.GroupPacketProcessor;
import com.mito.mitomod.common.item.GroupPacketProcessor.EnumGroupMode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class BB_SelectedGroup {

	private List<BraceBase> bases = new ArrayList<BraceBase>();
	public mitoClientProxy proxy;
	public Vec3 set = Vec3.createVectorHelper(0, 0, 0);
	public boolean activated = false;
	private boolean modecopy = false;
	public int pasteNum = 0;
	public int size = 100;
	public int rot = 0;
	public boolean ismove = false;

	public BB_SelectedGroup(mitoClientProxy px) {
		this.proxy = px;
	}

	public void initNum() {
		size = 100;
		rot = 0;
	}

	public void addShift(BraceBase... bases) {
		initNum();
		for (int n = 0; n < bases.length; n++) {
			if (this.bases.contains(bases[n])) {
				this.bases.remove(bases[n]);
			} else {
				this.bases.add(bases[n]);
			}
		}
	}

	public void addShift(List<BraceBase> bases) {
		initNum();
		for (int n = 0; n < bases.size(); n++) {
			if (this.bases.contains(bases.get(n))) {
				this.bases.remove(bases.get(n));
			} else {
				this.bases.add(bases.get(n));
			}
		}
	}

	public void replace(List<BraceBase> bases) {
		initNum();
		this.bases = bases;
	}

	public void replace(BraceBase base) {
		initNum();
		this.bases.clear();
		this.bases.add(base);
	}

	public void remove(BraceBase... bases) {
		initNum();
		for (int n = 0; n < bases.length; n++) {
			this.bases.remove(bases[n]);
		}
	}

	public void delete() {
		initNum();
		this.bases.clear();
	}

	public List<BraceBase> getList() {
		return this.bases;
	}

	public boolean drawHighLightGroup(EntityPlayer player, double partialticks) {
		if (this.bases.isEmpty()) {
			return false;
		}
		for (int n = 0; n < this.bases.size(); n++) {
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
					-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
					-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
			this.bases.get(n).drawHighLight(4.0F, partialticks);
			GL11.glPopMatrix();
		}
		return true;
	}

	public boolean drawHighLightCopy(EntityPlayer player, double partialticks, MovingObjectPosition mop) {
		if (this.bases.isEmpty()) {
			return false;
		}
		for (int n = 0; n < this.bases.size(); n++) {
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialticks),
					-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialticks),
					-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialticks));
			Vec3 pos = this.getDistance(mop);
			GL11.glTranslated(pos.xCoord, pos.yCoord, pos.zCoord);
			this.bases.get(n).drawHighLight(4.0F, partialticks);
			GL11.glPopMatrix();
		}
		return true;
	}

	public double getMaxY() {
		double ret = 0;
		for (int n = 0; n < this.bases.size(); n++) {
			double m = this.bases.get(n).getMaxY();
			if (ret < m) {
				ret = m;
			}
		}
		return ret;
	}

	public double getMinY() {
		double ret = 128;
		for (int n = 0; n < this.bases.size(); n++) {
			double m = this.bases.get(n).getMinY();
			if (ret > m) {
				ret = m;
			}
		}
		return ret;
	}

	public boolean iscopy() {
		return modecopy;
	}

	public void setcopy(boolean modecopy) {
		this.modecopy = modecopy;
	}

	public Vec3 getDistance(MovingObjectPosition mop) {
		if (bases.isEmpty()) {
			return null;
		}
		Vec3 c = getCenter();
		return mop.hitVec.addVector(-c.xCoord, -c.yCoord, -c.zCoord);
	}

	public void applyProperty(BB_EnumTexture tex, int color, IDrawBrace shape) {
		for (int n = 0; n < this.bases.size(); n++) {
			if (this.bases.get(n) instanceof Brace) {
				Brace brace = ((Brace) this.bases.get(n));
				if (tex != null) {
					brace.texture = tex;
				}
				if (shape != null) {
					brace.shape = shape;
					brace.shouldUpdateRender = true;
				}
				if (brace.texture.hasColor) {
					if (color >= 0 && color < 16) {
						brace.color = color;
					} else if (color == 16) {
					} else {
						brace.color = 0;
					}
				} else {
					brace.color = 0;
				}
				PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this.bases.get(n)));
			}
		}
	}

	public void applyColor(int color) {
		for (int n = 0; n < this.bases.size(); n++) {
			if (this.bases.get(n) instanceof Brace) {
				Brace brace = ((Brace) this.bases.get(n));
				if (brace.texture.hasColor) {
					if (color >= 0 && color < 16) {
						brace.color = color;
					} else if (color == 16) {
					} else {
						brace.color = 0;
					}
				} else {
					brace.color = 0;
				}
				PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this.bases.get(n)));
			}
		}
	}

	public void init() {
		this.delete();
		this.activated = false;
		this.modecopy = false;
		this.ismove = false;
	}

	public void applySize(int isize) {
		for (int n = 0; n < this.bases.size(); n++) {
			if (this.bases.get(n) instanceof Brace) {
				Brace brace = ((Brace) this.bases.get(n));
				brace.size = (double) isize * 0.05;
				brace.shouldUpdateRender = true;
				PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this.bases.get(n)));
			}
		}
	}

	public int getSize() {
		if (this.bases.isEmpty()) {
			return -1;
		} else if (this.bases.size() == 1 && bases.get(0) instanceof Brace) {
			return (int) (((Brace) bases.get(0)).size * 20);
		} else {
			int is = (int) (((Brace) bases.get(0)).size * 20);
			for (int n = 0; n < this.bases.size(); n++) {
				if (this.bases.get(n) instanceof Brace) {
					Brace brace = ((Brace) this.bases.get(n));
					if (is != (int) (brace.size * 20)) {
						return -1;
					}
				}
			}
			return is;
		}
	}

	public void applyRoll(int iroll) {
		for (int n = 0; n < this.bases.size(); n++) {
			if (this.bases.get(n) instanceof Brace) {
				Brace brace = ((Brace) this.bases.get(n));
				brace.setRoll(iroll);
				brace.shouldUpdateRender = true;
				PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this.bases.get(n)));
			}
		}
	}

	public void applyGroupSize(int isize) {
		Vec3 c = getCenter();
		for (int n = 0; n < this.bases.size(); n++) {
			BraceBase brace = this.bases.get(n);
			brace.resize(c, (double) isize / this.size);
			brace.shouldUpdateRender = true;
			PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this.bases.get(n)));

		}
		this.size = isize;
	}

	public void applyGroupRot(int irot) {
		Vec3 c = getCenter();
		for (int n = 0; n < this.bases.size(); n++) {
			BraceBase brace = this.bases.get(n);
			brace.rotation(c, -this.rot + irot);
			brace.shouldUpdateRender = true;
			PacketHandler.INSTANCE.sendToServer(new BB_PacketProcessor(Mode.SYNC, this.bases.get(n)));

		}
		this.rot = irot;
	}

	public Vec3 getCenter() {
		//return Vec3.createVectorHelper(bases.get(0).pos.xCoord, this.getMinY(), bases.get(0).pos.zCoord);
		return set;
	}

	public void setmove(boolean b) {
		this.ismove = b;
	}

	public void breakGroup() {
		for (int n = 0; n < getList().size(); n++) {
			BraceBase base = getList().get(n);
			base.breakBrace(BAO_main.proxy.getClientPlayer());
		}
		PacketHandler.INSTANCE.sendToServer(new GroupPacketProcessor(EnumGroupMode.DELETE, getList()));
	}

}
