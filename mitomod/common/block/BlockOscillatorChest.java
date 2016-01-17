package com.mito.mitomod.common.block;

import java.util.Random;

import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.common.tile.TileOscillator;
import com.mito.mitomod.common.tile.TileOscillatorChest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOscillatorChest extends BlockOscillator {

	@SideOnly(Side.CLIENT)
	private IIcon Icon;

	@SideOnly(Side.CLIENT)
	private IIcon SideIcon;

	@SideOnly(Side.CLIENT)
	private IIcon ForwardIcon;

	private final Random random = new Random();

	public BlockOscillatorChest() {
		super();
		setBlockName("BlockOscillatorChest");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {

		player.openGui(mitomain.INSTANCE, mitomain.GUI_ID, world, x, y, z);
        return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int a) {

		return new TileOscillatorChest();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {

		TileOscillatorChest tile = (TileOscillatorChest) world.getTileEntity(x, y, z);

		if (tile != null) {
			for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
				ItemStack itemstack = tile.getStackInSlot(i1);

				if (itemstack != null) {

					float f = this.random.nextFloat() * 0.8F + 0.1F;
					float f1 = this.random.nextFloat() * 0.8F + 0.1F;
					float f2 = this.random.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = this.random.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
						entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
						world.spawnEntityInWorld(entityitem);
					}
				}
			}

			world.func_147453_f(x, y, z, p_149749_5_);
		}

		super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
	}

	//@Override
	//public int getRenderType() {
	//	return mitomain.OscillatorRenderType;
	//}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {

		TileOscillator tile = (TileOscillator) world.getTileEntity(x, y, z);

		tile.init = true;
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		this.Icon = par1IconRegister.registerIcon("mitomod:chestframe");
		this.SideIcon = par1IconRegister.registerIcon("mitomod:chestframeS");
		this.ForwardIcon = par1IconRegister.registerIcon("mitomod:chestframeF");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		if (par1 < 2) {
			return Icon;
		} else if (par1 == par2 + 2) {
			return ForwardIcon;
		} else {
			return SideIcon;
		}
	}

	@Override
    public boolean isOpaqueCube(){
        return false;
    }

	public boolean renderAsNormalBlock()
	{
		return true;
	}


	@Override
	public int getRenderType()
	{
		return 0;
	}


	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item)
    {
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int i1 = world.getBlockMetadata(x, y, z) & 4;

        if (l == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 0 | i1, 2);
        }

        if (l == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3 | i1, 2);
        }

        if (l == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 1 | i1, 2);
        }

        if (l == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2 | i1, 2);
        }
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
	/*
	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
        return side != 0 && (side == 1 || (double)hitY <= 0.5D) ? meta : meta | 4;
    }
    */

}
