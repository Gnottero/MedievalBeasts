package com.gnottero.medieval_beasts.tile_entity.custom;

import com.gnottero.medieval_beasts.config.MedievalBeastsConfig;
import com.gnottero.medieval_beasts.item.ModItems;
import com.gnottero.medieval_beasts.item.custom.MobBoxItem;
import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import com.gnottero.medieval_beasts.util.EntityRanking;
import com.gnottero.medieval_beasts.util.EntityRanking.Ranking;
import com.gnottero.medieval_beasts.util.ParticleShapes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UpgradeAltarTile extends TileEntity implements ITickableTileEntity {

    private boolean isActive;
    private int ticks;
    Ranking rank = Ranking.F;

    private final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<IItemHandler> handlerLazyOptional = LazyOptional.of(() -> itemStackHandler);

    public UpgradeAltarTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public UpgradeAltarTile() {
        super(ModTileEntities.UPGRADE_ALTAR.get());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("Items"));
        this.isActive = nbt.getBoolean("IsActive");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("Items", itemStackHandler.serializeNBT());
        compound.putBoolean("IsActive", this.isActive);
        return super.write(compound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == ModItems.MOB_BOX.get();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handlerLazyOptional.cast();
        }

        return super.getCapability(cap, side);
    }

    public boolean hasPetInFirstSlot() {
        return this.itemStackHandler.getStackInSlot(0).getCount() > 0
                && this.itemStackHandler.getStackInSlot(0).getItem() == ModItems.MOB_BOX.get();
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemStackHandler;
    }

    @Override
    public void tick() {
        if (this.world != null && !this.world.isRemote() && hasPetInFirstSlot() && this.isActive) {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(0);
            if (stackInSlot.getItem() instanceof MobBoxItem) {
                rank = Ranking.valueOf(MobBoxItem.getRank(stackInSlot));
            }
            ticks++;
            if (ticks != 20 * MedievalBeastsConfig.RITUAL_TIME.get()) {

                ServerWorld serverWorld = ((ServerWorld) this.getWorld());
                if (this.world.getGameTime() % 2 == 1) {
                    ParticleShapes.particleMagicCircle(new RedstoneParticleData(rank.next().getRed(), rank.next().getGreen(), rank.next().getBlue(), 1.0f), serverWorld, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 0.5d, 0.2d, 0.5d, 3, 2.5f);
                }
            } else {
                ticks = 0;
                this.isActive = false;
                upgradeRank();
                ParticleShapes.fibonacciSphere(new RedstoneParticleData(rank.next().getRed(), rank.next().getGreen(), rank.next().getBlue(), 1.0f), (ServerWorld) world, pos, 0.5f, 2.0f, 0.5f, 1.5f, 1000, 1);
            }
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isUpgradeable() {
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(0);
        if (stackInSlot.getItem() instanceof MobBoxItem) {
            Ranking rank = Ranking.valueOf(MobBoxItem.getRank(stackInSlot));
            return rank != Ranking.SS;
        }
        return false;
    }

    public void upgradeRank() {
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(0);
        if (stackInSlot.getItem() instanceof MobBoxItem) {
            Ranking currentRank = Ranking.valueOf(MobBoxItem.getRank(stackInSlot));
            MobBoxItem.setRank(stackInSlot, currentRank.next().getName());
        }
    }
}
