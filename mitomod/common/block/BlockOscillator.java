package com.mito.mitomod.common.block;

import com.mito.mitomod.common.mitomain;
import com.mito.mitomod.common.tile.TileOscillator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOscillator extends BlockContainer {

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

	public BlockOscillator() {
		super(Material.rock);
		setBlockName("BlockOscillator");
		setBlockTextureName("mitomod:frame");

		setCreativeTab(mitomain.tab);
        setHardness(1.5F);
        setResistance(1.0F);
        setStepSound(Block.soundTypeMetal);
        setLightLevel(0.8F);
        setLightOpacity(1);
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

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int a) {

		return new TileOscillator();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {

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
		this.Icon = par1IconRegister.registerIcon("mitomod:framem");
		this.InsideIcon = par1IconRegister.registerIcon("mitomod:oscillator");
		this.Icon40 = par1IconRegister.registerIcon("mitomod:frame40");
		this.Icon60 = par1IconRegister.registerIcon("mitomod:frame60");
		this.Icon80 = par1IconRegister.registerIcon("mitomod:frame80");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		if (par2 == 0) {
			return Icon;
		} else if (par2 == 1) {
			return Icon40;
		} else if (par2 == 2) {
			return Icon60;
		} else if (par2 == 3) {
			return Icon80;
		} else {
			return InsideIcon;
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {

		return mitomain.OscillatorRenderType;
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	//階段やハーフブロックみるといいかも
	public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

}
