package com.mito.mitomod.common;

import com.mito.mitomod.common.tile.TileOscillatorChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerOscillatorChest extends Container {

	private final IInventory tileentity;
	private int numRows;
    private static final String __OBFID = "CL_00001750";
    private static final int ySize = 222;

    public ContainerOscillatorChest(EntityPlayer player, TileOscillatorChest inv)
    {
        this.tileentity = inv;
        inv.openInventory();
        this.numRows = 6;
        int i = (this.numRows - 4) * 18;
        
        int j;
		int k;
		
		for (j = 0; j < this.numRows; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inv, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, ySize - 24));
        }
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.tileentity.isUseableByPlayer(p_75145_1_);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < this.tileentity.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, this.tileentity.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.tileentity.getSizeInventory(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        this.tileentity.closeInventory();
    }

	public IInventory getLowerChestInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return this.tileentity;
	}

}
