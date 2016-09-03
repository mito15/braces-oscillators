package com.mito.mitomod.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BB_DataLists;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.BraceBase.Brace.Brace;
import com.mito.mitomod.common.entity.EntityBrace;
import com.mito.mitomod.common.entity.EntityFake;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.utilities.MitoMath;
import com.mito.mitomod.utilities.MitoUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class RenderHighLight {

	public static RenderHighLight INSTANCE = new RenderHighLight();

	public void drawRuler(EntityPlayer player, Vec3 v1, Vec3 v2, int div, double partialTicks) {
		Vec3 partV12 = MitoMath.vectorDiv(v2.addVector(-v1.xCoord, -v1.yCoord, -v1.zCoord), (double) div);

		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		GL11.glTranslated(v1.xCoord, v1.yCoord, v1.zCoord);
		for (int n = 0; n < (div + 1); n++) {
			this.renderLine(0.0, 0.0, 0.1);
			this.renderLine(0.0, 0.0, -0.1);
			this.renderLine(0.0, 0.1, 0.0);
			this.renderLine(0.0, -0.1, 0.0);
			this.renderLine(0.1, 0.0, 0.0);
			this.renderLine(-0.1, 0.0, 0.0);
			GL11.glTranslated(partV12.xCoord, partV12.yCoord, partV12.zCoord);
		}

		GL11.glPopMatrix();

	}

	public void highlightWithBar(EntityPlayer player, double s, Vec3 c, byte f, boolean b) {
		List list = player.worldObj.getEntitiesWithinAABBExcludingEntity((Entity) null, MitoUtil.createAabbBySize(c, s));
		List<EntityBrace> list1 = new ArrayList<EntityBrace>();

		for (int n = 0; n < list.size(); n++) {
			if (list.get(n) instanceof EntityFake) {
				EntityBrace ent = ((EntityFake) list.get(n)).host;
				if (ent != null) {
					if (b) {
						list1.add(ent);
					} else {

						ent.flags = (byte) (ent.flags | f);
					}
				}

			}
		}

		if (list1.size() > 0 && player.getCurrentEquippedItem().getTagCompound() != null) {
			EntityBrace ent;
			if (player.getCurrentEquippedItem().getItem() instanceof ItemBar) {
				ent = list1.get(player.getCurrentEquippedItem().getTagCompound().getInteger("selectNum") % list1.size());
			} else {
				ent = list1.get(0);
			}

			ent.flags = (byte) (ent.flags | f);
		}

	}

	public void drawCenter(EntityPlayer player, Vec3 set, double partialTicks) {
		drawCenter(player, set, 0.1, partialTicks);
	}

	public void drawCenter(EntityPlayer player, Vec3 set, double size, double partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		GL11.glTranslated(set.xCoord, set.yCoord, set.zCoord);
		this.renderLine(0.0, 0.0, size);
		this.renderLine(0.0, 0.0, -size);
		this.renderLine(0.0, size, 0.0);
		this.renderLine(0.0, -size, 0.0);
		this.renderLine(size, 0.0, 0.0);
		this.renderLine(-size, 0.0, 0.0);
		GL11.glPopMatrix();
	}

	public void drawLine(EntityPlayer player, Vec3 set, Vec3 end, double partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		GL11.glTranslated(set.xCoord, set.yCoord, set.zCoord);
		this.renderLine(MitoMath.vectorSub(end, set));
		GL11.glPopMatrix();
	}

	public void drawBox(EntityPlayer player, Vec3 set, double size, double partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		GL11.glTranslated(set.xCoord, set.yCoord, set.zCoord);
		this.renderBox(size);
		GL11.glPopMatrix();
	}

	public void drawBox(EntityPlayer player, Vec3 set, Vec3 end, double partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		this.renderBox(set, end);
		GL11.glPopMatrix();
	}

	public void drawFakeBrace(EntityPlayer player, Vec3 set, Vec3 end, double size, double partialTicks) {

		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		GL11.glPushMatrix();

		GL11.glTranslated(set.xCoord, set.yCoord, set.zCoord);
		this.renderBox(size);

		GL11.glPopMatrix();

		GL11.glPushMatrix();

		GL11.glTranslated(end.xCoord, end.yCoord, end.zCoord);
		this.renderBox(size);

		GL11.glPopMatrix();

		GL11.glPushMatrix();

		GL11.glTranslated((end.xCoord + set.xCoord) / 2, (end.yCoord + set.yCoord) / 2, (end.zCoord + set.zCoord) / 2);
		this.renderBrace(set, end, size);

		GL11.glPopMatrix();

		GL11.glPopMatrix();

	}

	public void renderBox(double s, boolean b) {
		double size = s / 2.0;
		this.renderBox(size, size, size, b);
	}

	public void renderBox(double s) {
		double size = s / 2.0;
		this.renderBox(size, size, size);
	}

	private void renderBox(Vec3 set, Vec3 end) {
		GL11.glTranslated((set.xCoord + end.xCoord) / 2, (set.yCoord + end.yCoord) / 2, (set.zCoord + end.zCoord) / 2);

		this.renderBox(Math.abs((set.xCoord - end.xCoord)) / 2 + 0.01, Math.abs((set.yCoord - end.yCoord)) / 2 + 0.01, Math.abs((set.zCoord - end.zCoord)) / 2 + 0.01, false);
	}

	private void renderBox(double x, double y, double z) {
		renderBox(x, y, z, true);
	}

	private void renderBox(double x, double y, double z, boolean b) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if (b) {
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		}
		GL11.glLineWidth(2.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBegin(1);

		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x, -y, z);
		GL11.glVertex3d(x, y, -z);
		GL11.glVertex3d(x, -y, -z);
		GL11.glVertex3d(-x, y, z);
		GL11.glVertex3d(-x, -y, z);
		GL11.glVertex3d(-x, y, -z);
		GL11.glVertex3d(-x, -y, -z);

		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x, y, -z);
		GL11.glVertex3d(x, -y, z);
		GL11.glVertex3d(x, -y, -z);
		GL11.glVertex3d(-x, y, z);
		GL11.glVertex3d(-x, y, -z);
		GL11.glVertex3d(-x, -y, z);
		GL11.glVertex3d(-x, -y, -z);

		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(-x, y, z);
		GL11.glVertex3d(x, y, -z);
		GL11.glVertex3d(-x, y, -z);
		GL11.glVertex3d(x, -y, z);
		GL11.glVertex3d(-x, -y, z);
		GL11.glVertex3d(x, -y, -z);
		GL11.glVertex3d(-x, -y, -z);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	public void renderLine(double x, double y, double z) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		GL11.glLineWidth(2.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBegin(1);

		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(x, y, z);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	public void renderLine(Vec3 c) {

		renderLine(c.xCoord, c.yCoord, c.zCoord);

	}

	public void renderLine(Vec3 c, Vec3 p, double size) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
		GL11.glLineWidth((float) size);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBegin(1);

		GL11.glVertex3d(c.xCoord, c.yCoord, c.zCoord);
		GL11.glVertex3d(p.xCoord, p.yCoord, p.zCoord);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	public void renderBrace(Vec3 set, Vec3 end, double size) {

		double l = MitoMath.subAbs(set, end);

		double x1, y1, z1;
		float a1, a2;

		double xabs = set.xCoord - end.xCoord;
		double xzabs = Math.sqrt(Math.pow(set.xCoord - end.xCoord, 2) + Math.pow(set.zCoord - end.zCoord, 2));
		xzabs = set.xCoord - end.xCoord >= 0 ? -xzabs : xzabs;

		a1 = (float) (Math.atan((set.yCoord - end.yCoord) / xzabs) / Math.PI * 180);
		a2 = (float) (Math.atan((set.zCoord - end.zCoord) / xabs) / Math.PI * 180);

		GL11.glRotatef(-a2, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(a1, 0.0F, 0.0F, -1.0F);

		renderBox(l / 2, size / 2, size / 2);
	}

	private static void drawGrid(EntityPlayer player, double partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		//GL11.glTranslated((float) aEvent.target.blockX + 0.5F, (float) aEvent.target.blockY + 0.5F, (float) aEvent.target.blockZ + 0.5F);
		//int side = aEvent.target.sideHit;//Rotation.sideRotations[aEvent.target.sideHit].glApply();
		//MitoUtil.rotation(side);
		GL11.glTranslated(0.0D, -0.501D, 0.0D);
		GL11.glLineWidth(2.0F);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
		//GL11.glEnable(GL11.GL_BLEND);
		GL11.glBegin(1);
		GL11.glVertex3d(0.5D, 0.0D, -0.25D);
		GL11.glVertex3d(-0.5D, 0.0D, -0.25D);
		GL11.glVertex3d(0.5D, 0.0D, 0.25D);
		GL11.glVertex3d(-0.5D, 0.0D, 0.25D);
		GL11.glVertex3d(0.25D, 0.0D, -0.5D);
		GL11.glVertex3d(0.25D, 0.0D, 0.5D);
		GL11.glVertex3d(-0.25D, 0.0D, -0.5D);
		GL11.glVertex3d(-0.25D, 0.0D, 0.5D);
		GL11.glEnd();
		GL11.glPopMatrix();

		//GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawFakeBraceBend(EntityPlayer player, Vec3 end, NBTTagCompound nbt, double partialTicks) {
		BraceBase base = BB_DataLists.getWorldData(player.worldObj).getBraceBaseByID(nbt.getInteger("brace"));

		if (base != null && base instanceof Brace) {
			boolean flag = nbt.getBoolean("isPos");
			Brace brace = (Brace) base;
			/*Vec3 v1 = brace.pos;
			Vec3 v2 = flag ? end : MitoMath.vectorPul(v1, brace.offCurvePoints1);
			Vec3 v4 = brace.end;
			Vec3 v3 = flag ? MitoMath.vectorPul(v4, brace.offCurvePoints2) : end;
			drawBezier(player, v1, v2, v3, v4, 4, partialTicks);*/
		}
	}

	public void drawBezier(EntityPlayer player, Vec3 d1, Vec3 d2, Vec3 d3, Vec3 d4, double size, double partialTicks) {

		GL11.glPushMatrix();
		GL11.glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks),
				-(player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks),
				-(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks));
		GL11.glPushMatrix();
		Vec3 v1;
		Vec3 v2;
		for (int i = 0; i < 20; i++) {
			v1 = MitoMath.vectorBezier(d1, d2, d3, d4, (double) i * 0.05);
			v2 = MitoMath.vectorBezier(d1, d2, d3, d4, (double) (i + 1) * 0.05);
			renderLine(v1, v2, size);
		}

		GL11.glPopMatrix();

		GL11.glPopMatrix();

	}

}
