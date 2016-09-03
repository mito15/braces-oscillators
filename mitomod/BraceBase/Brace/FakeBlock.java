package com.mito.mitomod.BraceBase.Brace;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mito.mitomod.BraceBase.BB_BlockAccess;
import com.mito.mitomod.BraceBase.BB_GUIHandler;
import com.mito.mitomod.BraceBase.BB_PacketProcessor;
import com.mito.mitomod.BraceBase.BB_PacketProcessor.Mode;
import com.mito.mitomod.BraceBase.BraceBase;
import com.mito.mitomod.client.RenderHighLight;
import com.mito.mitomod.common.BAO_main;
import com.mito.mitomod.common.PacketHandler;
import com.mito.mitomod.common.item.ItemBar;
import com.mito.mitomod.utilities.Line;
import com.mito.mitomod.utilities.MitoUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class FakeBlock extends BraceBase {

	public Block contain = null;
	public BB_BlockAccess access;

	@SideOnly(Side.CLIENT)
	public RenderBlocks renderblocks;
	public int meta;

	public FakeBlock(World world) {
		super(world);
	}

	public FakeBlock(World world, Vec3 pos, Block block, int meta) {
		super(world, pos);
		contain = block;
		this.meta = meta;
	}

	@Override
	public boolean addToWorld() {
		if (worldObj == null) {
			return false;
		}
		this.access = new BB_BlockAccess(this);
		access.setBlock(0, 0, 0, contain);
		if (worldObj.isRemote) {
			this.clientInit();
			return dataworld.addBraceBase(this, true);
		} else {
			boolean ret;
			if (pos != null) {
				ret = dataworld.addBraceBase(this, true);
			} else {
				ret = dataworld.addBraceBase(this, false);
			}
			if (ret) {
				PacketHandler.INSTANCE.sendToAll(new BB_PacketProcessor(Mode.SUGGEST, this));
			}
			return ret;
		}
	}

	@SideOnly(Side.CLIENT)
	private void clientInit() {
		this.renderblocks = new RenderBlocks(access);
		renderblocks.renderAllFaces = true;
	}

	@Override
	public void onUpdate() {
		if (this.access != null && this.access.te != null) {
			access.te.updateEntity();
			;
		}
	}

	@Override
	protected void readBraceBaseFromNBT(NBTTagCompound nbt) {
		contain = Block.getBlockById(nbt.getInteger("block"));
		meta = nbt.getInteger("metadata");
	}

	@Override
	protected void writeBraceBaseToNBT(NBTTagCompound nbt) {
		nbt.setInteger("block", Block.getIdFromBlock(contain));
		nbt.setInteger("metadata", meta);
	}

	public void dropItem() {

		float f = this.random.nextFloat() * 0.2F + 0.1F;
		float f1 = this.random.nextFloat() * 0.2F + 0.1F;
		float f2 = this.random.nextFloat() * 0.2F + 0.1F;

		ItemStack itemstack1 = new ItemStack(BAO_main.ItemBrace, 1, this.meta);

		NBTTagCompound nbt = itemstack1.getTagCompound();
		itemstack1.setTagCompound(nbt);
		nbt.setBoolean("activated", false);
		nbt.setDouble("setX", 0.0D);
		nbt.setDouble("setY", 0.0D);
		nbt.setDouble("setZ", 0.0D);
		nbt.setBoolean("useFlag", false);
		EntityItem entityitem = new EntityItem(worldObj, (double) ((float) this.pos.xCoord + f), (double) ((float) this.pos.yCoord + f1), (double) ((float) this.pos.zCoord + f2), itemstack1);

		float f3 = 0.05F;
		entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
		entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
		entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
		worldObj.spawnEntityInWorld(entityitem);
	}

	public void drawHighLight(double partialticks) {
		GL11.glPushMatrix();
		if (this.isStatic) {
			GL11.glTranslated(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord);
		} else {
			double x = this.prevPos.xCoord + (this.pos.xCoord - this.prevPos.xCoord) * (double) partialticks;
			double y = this.prevPos.yCoord + (this.pos.yCoord - this.prevPos.yCoord) * (double) partialticks;
			double z = this.prevPos.zCoord + (this.pos.zCoord - this.prevPos.zCoord) * (double) partialticks;
			GL11.glTranslated(x, y, z);
		}

		GL11.glLineWidth(1.0F);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);

		RenderHighLight rh = RenderHighLight.INSTANCE;
		rh.renderBox(1.0, false);
		GL11.glPopMatrix();
	}

	@Override
	public boolean interactWithAABB(AxisAlignedBB boundingBox) {
		boolean ret = false;
		if (MitoUtil.createAabbBySize(pos, 1.0).addCoord(0, 1.0, 0).intersectsWith(boundingBox)) {
			ret = true;
		}
		return ret;
	}

	@Override
	public Vec3 interactWithLine(Vec3 s, Vec3 e) {
		AxisAlignedBB aabb = contain.getCollisionBoundingBoxFromPool(access, 0, 0, 0);
		if (aabb != null) {
			return aabb.getOffsetBoundingBox(pos.xCoord - 0.5, pos.yCoord - 0.5, pos.zCoord - 0.5).calculateIntercept(s, e).hitVec;
		}
		return null;
	}

	@Override
	public Line interactWithRay(Vec3 set, Vec3 end) {
		AxisAlignedBB aabb = contain.getCollisionBoundingBoxFromPool(access, 0, 0, 0);
		if (aabb != null) {
			MovingObjectPosition v = aabb.getOffsetBoundingBox(pos.xCoord - 0.5, pos.yCoord - 0.5, pos.zCoord - 0.5).calculateIntercept(set, end);
			if (v != null && v.hitVec != null) {
				return new Line(v.hitVec, v.hitVec);
			}
		}
		return null;
	}

	public void breakBrace(EntityPlayer player) {
		if (!player.worldObj.isRemote) {
			//Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(contain.stepSound.getBreakSound()), contain.stepSound.getVolume(), contain.stepSound.getPitch(), (float) pos.xCoord,
					//(float) pos.yCoord, (float) pos.zCoord));
			if (!player.capabilities.isCreativeMode) {
				this.dropItem();
			}
			this.setDead();
			/*for (int n = 0; n < this.bindBraces.size(); n++) {
				this.bindBraces.get(n).setDead();
			}*/
		} else {
			//破壊時パーティクル
			//this.setDead();
			/*int b0 = (int) (this.size * 4) + 1;
			;
			Vec3 center = MitoMath.vectorRatio(this.end, this.pos, 0.5);
			int div = (int) (MitoMath.subAbs(this.pos, this.end) * 4) + 1;
			Vec3 vec = MitoMath.vectorSub(this.end, this.pos);

			for (int i1 = 0; i1 < b0; ++i1) {
				for (int j1 = 0; j1 < b0; ++j1) {
					for (int k1 = 0; k1 < div; ++k1) {
						double d0 = this.pos.xCoord + vec.xCoord * (double) k1 / (double) div + ((double) j1 * size) / (double) b0 - (size / 2);
						double d1 = this.pos.yCoord + vec.yCoord * (double) k1 / (double) div + ((double) i1 * size) / (double) b0 - (size / 2);
						double d2 = this.pos.zCoord + vec.zCoord * (double) k1 / (double) div + ((double) j1 * size) / (double) b0 - (size / 2);
						Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - center.xCoord, d1 - center.yCoord, d2 - center.zCoord, this.texture.getBlock(), this.color))
								.applyColourMultiplier((int) center.xCoord, (int) center.yCoord, (int) center.zCoord));
					}
				}
			}*/
		}
	}

	public boolean leftClick(EntityPlayer player, ItemStack itemstack) {
		if (player.capabilities.isCreativeMode) {
			this.breakBrace(player);
			return true;
		} else if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		}
		return false;
	}

	public boolean rightClick(EntityPlayer player, Vec3 pos, ItemStack itemstack) {
		/*if (itemstack != null && itemstack.getItem() instanceof ItemBar) {
			this.breakBrace(player);
			return true;
		}
		return false;*/
		BB_GUIHandler.flag = true;
		return contain.onBlockActivated(access, 0, 0, 0, player, 0, (float) pos.xCoord, (float) pos.yCoord, (float) pos.zCoord);
	}

	public void addCollisionBoxesToList(World world, AxisAlignedBB aabb, List collidingBoundingBoxes, Entity entity) {
		/*collidingBoundingBoxes.add(MitoUtil.createAabbBySize(pos, 1.0));
		AxisAlignedBB axisalignedbb1 = contain.getCollisionBoundingBoxFromPool(access, 0, 0, 0).getOffsetBoundingBox(pos.xCoord - 0.5, pos.yCoord - 0.5, pos.zCoord - 0.5);

		if (axisalignedbb1 != null && aabb.intersectsWith(axisalignedbb1)) {
			collidingBoundingBoxes.add(axisalignedbb1);
		}*/

		if (access != null) {
			List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
			contain.addCollisionBoxesToList(access, 0, 0, 0, aabb.getOffsetBoundingBox(-pos.xCoord + 0.5, -pos.yCoord + 0.5, -pos.zCoord + 0.5), list, entity);
			for (int n = 0; n < list.size(); n++) {
				AxisAlignedBB axisalignedbb1 = list.get(n).getOffsetBoundingBox(pos.xCoord - 0.5, pos.yCoord - 0.5, pos.zCoord - 0.5);
				if (axisalignedbb1 != null && aabb.intersectsWith(axisalignedbb1)) {
					collidingBoundingBoxes.add(axisalignedbb1);
				}
			}
		}
	}

	public Block[] getBlocks() {
		return new Block[] { this.contain };
	}

	public byte[] getBytes() {
		return new byte[] { (byte) this.meta };
	}

}
