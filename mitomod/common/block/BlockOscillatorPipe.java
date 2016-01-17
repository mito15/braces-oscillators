package com.mito.mitomod.common.block;

import java.util.Random;

import com.mito.mitomod.common.mitoLogger;
import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.common.tile.TileOscillator;
import com.mito.mitomod.common.tile.TileOscillatorPipe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOscillatorPipe extends BlockOscillator {

	@SideOnly(Side.CLIENT)
	private IIcon Icon;

	@SideOnly(Side.CLIENT)
	private IIcon Icon40;

	@SideOnly(Side.CLIENT)
	private IIcon Icon60;

	@SideOnly(Side.CLIENT)
	private IIcon Icon80;

	@SideOnly(Side.CLIENT)
	private IIcon InsideIcon;

	private final Random random = new Random();

	public BlockOscillatorPipe() {
		super();
		setBlockName("BlockOscillatorPipe");
		setBlockTextureName("mitomod:frame");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX,
			float posY, float posZ) {

		TileOscillator tile = (TileOscillator) world.getTileEntity(x, y, z);

		if (tile.freqPec == 1.1) {

			tile.freqPec = 0.9;

		} else if (tile.freqPec == 1.0) {

			tile.freqPec = 1.1;

		} else if (tile.freqPec == 0.9) {

			tile.freqPec = 1;

		}

		if(world.isRemote){

			tile.freqPecRe = tile.freqPec;

		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int a) {

		return new TileOscillatorPipe();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {

		TileOscillatorPipe tile = (TileOscillatorPipe) world.getTileEntity(x, y, z);

		if (tile != null) {
			for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
				ItemStack itemstack = tile.getStackInSlot(i1);

				mitoLogger.info("tileEntity is good");

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
		super.registerBlockIcons(par1IconRegister);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return super.getIcon(par1, par2);
	}

	@Override
    public boolean isOpaqueCube(){
        return false;
    }

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType()
	{
		return mitomain.PipeRenderType;
	}

	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	//階段やハーフブロックみるといいかも
	public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

}
