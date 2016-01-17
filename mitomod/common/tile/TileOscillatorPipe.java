package com.mito.mitomod.common.tile;

import java.util.List;

import com.mito.mitomod.common.LoadBCAPI;
import com.mito.mitomod.common.LoadCoFHLib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileOscillatorPipe extends TileOscillator implements IPipe {

	double j1 = 0.3;
	private ItemStack[] itemStack = new ItemStack[3];
	private String customName;
	private int transferCooldown = -1;
	public int sendSide;
	public int receiveSide;
	private boolean isPipeSided = false;
	private boolean isInvSided = false;
	private int invSide;
	private IInventory tileChest;
	private int cooltime = 0;
	public boolean doSend;
	public boolean hasItem = false;
	public Object[] sideObj = new Object[6];

	public int tex = 60;

	static final int normalSize = 2;
	static final int chestSize = 1;

	static final int normalIndex = 0;
	static final int chestIndex = normalIndex + normalSize;
	
	@Override
	public void init() {

		this.isPipeSided = false;
		this.isInvSided = false;

		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		for (int n = 0; n < 6; n++) {
			TileEntity tile = this.worldObj.getTileEntity(x + Facing.offsetsXForSide[n], y + Facing.offsetsYForSide[n], z + Facing.offsetsZForSide[n]);
			this.tileOs[n] = (tile instanceof TileOscillator) ? (TileOscillator) tile : null;
			this.sideObj[n] = (Object) tile;
			int flag = 0;

			if (Loader.isModLoaded("BuildCraft|Transport")) {
				try {
					flag = LoadBCAPI.isPipe(this.sideObj[n]) ? 1 : flag;
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			if(Loader.isModLoaded("ThermalDynamics")){
				try {
					flag = LoadCoFHLib.isDuct(this.sideObj[n]) ? 1 : flag;
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}

			if (this.tileOs[n] == null && this.sideObj[n] instanceof IInventory) {
				this.isPipeSided = false;
				this.isInvSided = true;
				this.invSide = n;
			} else if (flag == 1) {
				this.isPipeSided = true;
				this.isInvSided = false;
				this.invSide = n;
			}
		}
	}

	public int countFill(IInventory inv) {
		int fill = 0;
		for (int j = 0; j < inv.getSizeInventory(); ++j) {
			ItemStack itemstack = inv.getStackInSlot(j);
			if (itemstack != null && itemstack.stackSize != 0) {
				fill++;
			}
		}
		return fill;

	}

	public boolean isInvFull(IInventory inv) {

		boolean flag = false;

		if (inv != null && this.countFill(inv) == inv.getSizeInventory()) {

			flag = true;
		}

		return flag;

	}

	@Override
	public void updateEntity() {

		if (!this.worldObj.isRemote) {

			//レッドストーン判定

			if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {

				this.freqPecRe = this.freqPec == 1.1 ? 0.9 : this.freqPec + 0.1;

			} else {

				this.freqPecRe = this.freqPec;
			}

			//なるべくつまらないようにアイテムが入っていたら固有振動数を少し上げる
			if (this.freqPec == 1.0) {

				if (this.countFill(this) > 0) {

					this.freqPecRe += 0.01;
				}
			}

			this.time = worldObj.getTotalWorldTime();

			// 周りの振動子数、位相差の正弦の和
			int k = 0;
			sigm = 0;

			// 初期化処理
			if (init || init0) {

				init();
				init = false;
				init0 = false;

			}

			double angC;
			double angCp = 0;
			//double angCn = 0;
			this.sendSide = -1;

			//振動子の同期計算、処理のズレをtimeで消してる、差をとって-10～10に直してアイテムを送る方向を決定している
			for (int n = 0; n < 6; n++) {
				if (tileOs[n] != null) {
					k++;
					if (tileOs[n].time != this.time) {
						angC = this.ang - tileOs[n].ang;
					} else {
						angC = this.ang - tileOs[n].angPre;
					}
					if (angC >= 10) {
						angC -= 20;
					} else if (angC <= -10) {
						angC += 20;
					}
					sigm += Math.sin((angC) * Math.PI * 0.1);
					if (angC > angCp) {
						angCp = angC;
						if (tileOs[n] instanceof IInventory) {
							this.sendSide = n;
						}
					}
				}
			}

			if (this.sendSide == -1 && this.isInvSided) {

				this.sendSide = this.invSide;
			}

			this.angPre = this.ang;
			this.ang += k == 0 ? this.freqPecRe : this.freqPecRe - j1 / k * sigm;

			if (this.ang > 20) {
				this.ang = 0;
			}

			/*
			--this.transferCooldown;

			if (!this.isCooldownPlus()) {
				this.setCooldown(0);
				//this.itemTransport();
				this.sendItem();
			}

			*/

			if (this.ang == 0) {
				//this.itemTransport();
				this.sendItem();
			}

			cooltime++;

			if (cooltime == 2) {
				cooltime = 0;
				if (this.itemStack[0] != null) {

					this.hasItem = true;
				} else {

					this.hasItem = false;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}

		} else {

			// tから大きさを算出
			this.tex = (int) (3 * Math.floor(20 * this.freqPecRe + 0.5));
			double tRate = (double) ang / (double) 20;
			this.beatSize = 0.3 + Math.cos(tRate * Math.PI / 2) * 0.4;
			// mitoLogger.info(""+this.freq);

			this.ang += this.freqPecRe;

			if (this.ang > 20) {
				this.ang = 0;
			}

		}

	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.freqPec = par1NBTTagCompound.getDouble("frequency");
		this.freqPecRe = par1NBTTagCompound.getDouble("realfrequency");
		this.ang = par1NBTTagCompound.getDouble("angle");
		this.init = par1NBTTagCompound.getBoolean("initialize");
		this.sendSide = par1NBTTagCompound.getInteger("send");
		this.hasItem = par1NBTTagCompound.getBoolean("hasItem");

		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
		this.itemStack = new ItemStack[this.getSizeInventory()];

		if (par1NBTTagCompound.hasKey("CustomName", 8)) {
			this.customName = par1NBTTagCompound.getString("CustomName");
		}

		this.transferCooldown = par1NBTTagCompound.getInteger("TransferCooldown");

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.itemStack.length) {
				this.itemStack[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public int getSizeInventory() {
		return this.itemStack.length;
	}

	public boolean hasCustomInventoryName() {
		return this.itemStack != null && this.customName != null && this.customName.length() > 0;
	}

	/*
	 * こちらはNBTを書き込むメソッド。
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("frequency", this.freqPec);
		par1NBTTagCompound.setDouble("realfrequency", this.freqPecRe);
		par1NBTTagCompound.setDouble("angle", this.ang);
		par1NBTTagCompound.setBoolean("initialize", this.init);
		par1NBTTagCompound.setInteger("send", this.sendSide);
		par1NBTTagCompound.setBoolean("hasItem", this.hasItem);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.itemStack.length; ++i) {
			if (this.itemStack[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.itemStack[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		par1NBTTagCompound.setTag("Items", nbttaglist);
		par1NBTTagCompound.setInteger("TransferCooldown", this.transferCooldown);

		if (this.hasCustomInventoryName()) {
			par1NBTTagCompound.setString("CustomName", this.customName);
		}
	}

	/*
	 * パケットの送信・受信処理。 カスタムパケットは使わず、バニラのパケット送受信処理を使用。
	 */
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

	public void markDirty() {
		super.markDirty();
	}

	public ItemStack decrStackSize(int invNum, int invStackSize) {
		if (this.itemStack[invNum] != null) {
			ItemStack itemstack;

			if (this.itemStack[invNum].stackSize <= invStackSize) {
				itemstack = this.itemStack[invNum];
				this.itemStack[invNum] = null;
				return itemstack;
			} else {
				itemstack = this.itemStack[invNum].splitStack(invStackSize);

				if (this.itemStack[invNum].stackSize == 0) {
					this.itemStack[invNum] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int invNum, ItemStack itemStack) {
		this.itemStack[invNum] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	public ItemStack getStackInSlotOnClosing(int invNum) {
		if (this.itemStack[invNum] != null) {
			ItemStack itemstack = this.itemStack[invNum];
			this.itemStack[invNum] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "pipe.oscillatorPipe";
	}

	public void setCustomName(String name) {
		this.customName = name;
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}

	public void openInventory() {
	}

	public void closeInventory() {
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	public boolean itemTransport() {
		if (this.worldObj != null && !this.worldObj.isRemote) {
			if (!this.isCooldownPlus() && isAndEight(this.getBlockMetadata())) {
				boolean flag = false;

				if (!this.isItemIn()) {
					flag = this.sendItemforInv();
				}

				if (!this.isFull()) {
					flag = extractItem(this) || flag;
				}

				if (flag) {
					this.setCooldown(8);
					this.markDirty();
					return true;
				}
			}

			return false;
		} else {
			return false;
		}
	}

	public boolean sendItem() {
		if (this.worldObj != null && !this.worldObj.isRemote) {

			boolean flag = false;

			if (this.sendSide != -1) {
				if (!this.isItemIn()) {
					flag = this.sendItemforInv();
				}
			} else if (this.isPipeSided) {
				this.sendSide = this.invSide;
				flag = this.sendItemforPipe();
			}

			if (flag) {
				//this.setCooldown(8);
				this.markDirty();
				return true;
			}

			return false;
		} else {
			return false;
		}
	}

	public static boolean isAndEight(int n) {
		return (n & 8) != 8;
	}

	private boolean isItemIn() {
		ItemStack[] aitemstack = this.itemStack;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack != null) {
				return false;
			}
		}

		return true;
	}

	private boolean isFull() {
		ItemStack[] aitemstack = this.itemStack;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];

			if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
				return false;
			}
		}

		return true;
	}

	private boolean sendItemforInv() {
		IInventory iinventory = this.getSidedInventry();

		if (iinventory == null) {
			return false;
		} else {
			int i = Facing.oppositeSide[this.sendSide];

			if (this.isSidedInvFull(iinventory, i)) {
				return false;
			} else {
				for (int j = 0; j < this.getSizeInventory(); ++j) {
					if (this.getStackInSlot(j) != null) {
						ItemStack itemstack = this.getStackInSlot(j).copy();
						ItemStack itemstack1 = insertItemWithSide(iinventory, this.decrStackSize(j, 16), i);

						if (itemstack1 == null || itemstack1.stackSize == 0) {
							iinventory.markDirty();
							return true;
						}

						this.setInventorySlotContents(j, itemstack);
					}
				}

				return false;
			}
		}
	}

	private boolean sendItemforPipe() {

		boolean flag = false;

		if (Loader.isModLoaded("BuildCraft|Transport")) {
			try {
				flag = LoadBCAPI.sendItemforPipe(this);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		if (Loader.isModLoaded("ThermalDynamics")) {
			try {
				flag = LoadCoFHLib.sendItemforPipe(this);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}

		return flag;
	}

	public ItemStack getStackInSlot(int invNum) {
		return this.itemStack[invNum];
	}

	private boolean isSidedInvFull(IInventory inv, int side) {
		if (inv instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) inv;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

			for (int l = 0; l < aint.length; ++l) {
				ItemStack itemstack1 = isidedinventory.getStackInSlot(aint[l]);

				if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize()) {
					return false;
				}
			}
		} else {
			int j = inv.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				ItemStack itemstack = inv.getStackInSlot(k);

				if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize()) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean isSidedInvFillItem(IInventory inv, int side) {
		if (inv instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) inv;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

			for (int l = 0; l < aint.length; ++l) {
				if (isidedinventory.getStackInSlot(aint[l]) != null) {
					return false;
				}
			}
		} else {
			int j = inv.getSizeInventory();

			for (int k = 0; k < j; ++k) {
				if (inv.getStackInSlot(k) != null) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean extractItem(IPipe pipe) {
		IInventory iinventory = getUnderInventry(pipe);

		if (iinventory != null) {
			byte b0 = 0;

			if (isSidedInvFillItem(iinventory, b0)) {
				return false;
			}

			if (iinventory instanceof ISidedInventory && b0 > -1) {
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getAccessibleSlotsFromSide(b0);

				for (int k = 0; k < aint.length; ++k) {
					if (extractItemWithSlot(pipe, iinventory, aint[k], b0)) {
						return true;
					}
				}
			} else {
				int i = iinventory.getSizeInventory();

				for (int j = 0; j < i; ++j) {
					if (extractItemWithSlot(pipe, iinventory, j, b0)) {
						return true;
					}
				}
			}
		} else {
			EntityItem entityitem = getDropItem(pipe.getWorldObj(), pipe.getXPos(), pipe.getYPos() + 1.0D, pipe.getZPos());

			if (entityitem != null) {
				return extractDropItem(pipe, entityitem);
			}
		}

		return false;
	}

	private static boolean extractItemWithSlot(IPipe pipe, IInventory inv, int slot, int side) {
		ItemStack itemstack = inv.getStackInSlot(slot);

		if (itemstack != null && canSideExtract(inv, itemstack, slot, side)) {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = insertItemWithSide(pipe, inv.decrStackSize(slot, 1), -1);

			if (itemstack2 == null || itemstack2.stackSize == 0) {
				inv.markDirty();
				return true;
			}

			inv.setInventorySlotContents(slot, itemstack1);
		}

		return false;
	}

	public static boolean extractDropItem(IInventory inv, EntityItem entityItem) {
		boolean flag = false;

		if (entityItem == null) {
			return false;
		} else {
			ItemStack itemstack = entityItem.getEntityItem().copy();
			ItemStack itemstack1 = insertItemWithSide(inv, itemstack, -1);

			if (itemstack1 != null && itemstack1.stackSize != 0) {
				entityItem.setEntityItemStack(itemstack1);
			} else {
				flag = true;
				entityItem.setDead();
			}

			return flag;
		}
	}

	public static ItemStack insertItemWithSide(IInventory inv, ItemStack is, int side) {
		if (inv instanceof ISidedInventory && side > -1) {
			ISidedInventory isidedinventory = (ISidedInventory) inv;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

			for (int l = 0; l < aint.length && is != null && is.stackSize > 0; ++l) {
				is = insertItemInSlot(inv, is, aint[l], side);
			}
		} else {
			int j = inv.getSizeInventory();

			for (int k = 0; k < j && is != null && is.stackSize > 0; ++k) {
				is = insertItemInSlot(inv, is, k, side);
			}
		}

		if (is != null && is.stackSize == 0) {
			is = null;
		}

		return is;
	}

	private static boolean canSideInsert(IInventory inv, ItemStack is, int slot, int side) {
		return !inv.isItemValidForSlot(slot, is) ? false : !(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(slot, is, side);
	}

	private static boolean canSideExtract(IInventory inv, ItemStack is, int slot, int side) {
		return !(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canExtractItem(slot, is, side);
	}

	private static ItemStack insertItemInSlot(IInventory inv, ItemStack is, int slot, int side) {
		ItemStack itemstack1 = inv.getStackInSlot(slot);

		if (canSideInsert(inv, is, slot, side)) {
			boolean flag = false;

			if (itemstack1 == null) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(is.getMaxStackSize(), inv.getInventoryStackLimit());
				if (max >= is.stackSize) {
					inv.setInventorySlotContents(slot, is);
					is = null;
				} else {
					inv.setInventorySlotContents(slot, is.splitStack(max));
				}
				flag = true;
			} else if (itemstackEqual(itemstack1, is)) {
				// Forge: BUGFIX: Again, make things respect max stack sizes.
				int max = Math.min(is.getMaxStackSize(), inv.getInventoryStackLimit());
				if (max > itemstack1.stackSize) {
					int l = Math.min(is.stackSize, max - itemstack1.stackSize);
					is.stackSize -= l;
					itemstack1.stackSize += l;
					flag = l > 0;
				}
			}

			if (flag) {
				if (inv instanceof TileEntityHopper) {
					((TileEntityHopper) inv).func_145896_c(8);
					inv.markDirty();
				}

				inv.markDirty();
			}
		}

		return is;
	}

	private IInventory getSidedInventry() {
		int i = this.sendSide;
		if (i != -1) {
			return getIInventry(this.getWorldObj(), (double) (this.xCoord + Facing.offsetsXForSide[i]), (double) (this.yCoord + Facing.offsetsYForSide[i]), (double) (this.zCoord + Facing.offsetsZForSide[i]));
		}
		return null;
	}

	public static IInventory getUnderInventry(IPipe pipe) {
		return getIInventry(pipe.getWorldObj(), pipe.getXPos(), pipe.getYPos() + 1.0D, pipe.getZPos());
	}

	public static EntityItem getDropItem(World world, double x, double y, double z) {
		List list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), IEntitySelector.selectAnything);
		return list.size() > 0 ? (EntityItem) list.get(0) : null;
	}

	public static IInventory getIInventry(World world, double x, double y, double z) {
		IInventory iinventory = null;
		int i = MathHelper.floor_double(x);
		int j = MathHelper.floor_double(y);
		int k = MathHelper.floor_double(z);
		TileEntity tileentity = world.getTileEntity(i, j, k);

		if (tileentity != null && tileentity instanceof IInventory) {
			iinventory = (IInventory) tileentity;

			if (iinventory instanceof TileEntityChest) {
				Block block = world.getBlock(i, j, k);

				if (block instanceof BlockChest) {
					iinventory = ((BlockChest) block).func_149951_m(world, i, j, k);
				}
			}
		}

		if (iinventory == null) {
			List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D),
					IEntitySelector.selectInventories);

			if (list != null && list.size() > 0) {
				iinventory = (IInventory) list.get(world.rand.nextInt(list.size()));
			}
		}

		return iinventory;
	}

	private static boolean itemstackEqual(ItemStack itemStack1, ItemStack itemStack2) {
		return itemStack1.getItem() != itemStack2.getItem() ? false
				: (itemStack1.getItemDamage() != itemStack2.getItemDamage() ? false : (itemStack1.stackSize > itemStack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(itemStack1, itemStack2)));
	}

	/**
	 * Gets the world X position for this hopper entity.
	 */
	public double getXPos() {
		return (double) this.xCoord;
	}

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	public double getYPos() {
		return (double) this.yCoord;
	}

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	public double getZPos() {
		return (double) this.zCoord;
	}

	public void setCooldown(int cooldown) {
		this.transferCooldown = cooldown;
	}

	public boolean isCooldownPlus() {
		return this.transferCooldown > 0;
	}

	public int getCooldownTime() {
		return this.transferCooldown;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.xCoord - 32, this.yCoord - 16, this.zCoord - 32, this.xCoord + 32, this.yCoord + 16, this.zCoord + 32);
		return bb;
	}

}
