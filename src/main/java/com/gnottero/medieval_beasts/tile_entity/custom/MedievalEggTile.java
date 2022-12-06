package com.gnottero.medieval_beasts.tile_entity.custom;

import com.gnottero.medieval_beasts.entity.custom.MedievalBeastEntity;
import com.gnottero.medieval_beasts.tile_entity.ModTileEntities;
import com.gnottero.medieval_beasts.util.EntityRanking.Ranking;
import com.gnottero.medieval_beasts.util.ParticleShapes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public class MedievalEggTile extends TileEntity implements ITickableTileEntity, IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);

    private int growthStage;
    private int walkedSteps;
    private Ranking rank;
    private String catalyst;
    private boolean isActive;
    private int ticks;
    private UUID owner;
    private EntityType<?> entityType;

    public MedievalEggTile(TileEntityType<?> tileEntityTypeIn, EntityType<?> entityType) {
        super(tileEntityTypeIn);
        this.entityType = entityType;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        compound.putBoolean("IsActive", this.isActive);
        compound.putInt("GrowthStage", this.growthStage);
        compound.putInt("WalkedSteps", this.walkedSteps);
        if (this.rank != null) {
            compound.putString("Rank", this.rank.getName());
        }
        if (this.catalyst != null) {
            compound.putString("Catalyst", this.catalyst);
        }
        compound.putBoolean("IsActive", this.isActive);
        if (this.owner != null) {
            compound.putUniqueId("Owner", this.owner);
        }
//        if (this.entityType != null) {
//            compound.putString("EntityType", this.entityType.toString());
//        }
        return compound;
    }

    public void handleUpdateTag(CompoundNBT nbt) {
        this.growthStage = nbt.getInt("GrowthStage");
        this.walkedSteps = nbt.getInt("WalkedSteps");
        if (nbt.contains("Rank")) {
            this.rank = Ranking.valueOf(nbt.getString("Rank"));
        }
        if (nbt.contains("Catalyst")) {
            this.catalyst = nbt.getString("Catalyst");
        }
        this.isActive = nbt.getBoolean("IsActive");
        if (nbt.hasUniqueId("Owner")) {
            this.owner = nbt.getUniqueId("Owner");
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote()) {
            ticks++;

            if (ticks == 40) {
                ticks = 0;
                if (this.isActive) {

                    if (this.growthStage < 4) {
                        ParticleShapes.particleCircle(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, pos.getX(), pos.getY(), pos.getZ(), 1.5f, 0.5f, 0.5f, 2, 90, 1);
                        ParticleShapes.particleCircle(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, pos.getX(), pos.getY(), pos.getZ(), -0.5f, 0.5f, 0.5f, 2, 90, 1);
                        ParticleShapes.particleCircle(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, pos.getX(), pos.getY(), pos.getZ(), 0.5f, 0.5f, 1.5f, 2, 90, 1);
                        ParticleShapes.particleCircle(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, pos.getX(), pos.getY(), pos.getZ(), 0.5f, 0.5f, -0.5f, 2, 90, 1);
                        ParticleShapes.particleCircle(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, pos.getX(), pos.getY(), pos.getZ(), 0.5f, 0.5f, 0.5f, 3, 90, 1);
                        growthStage++;
                        markDirty();
                    } else {
                        ParticleShapes.fibonacciSphere(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, pos, 0.5f, 0.5f, 0.5f, 3, 1000, 1);
                        world.destroyBlock(pos, false);
                        EntityType<?> baseEntity = this.getEntityType();
                        if (baseEntity != null) {
                            Entity spawnedEntity = baseEntity.spawn((ServerWorld) this.world, null, null, new BlockPos(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f), SpawnReason.TRIGGERED, true, true);
                            if (spawnedEntity instanceof MedievalBeastEntity) {
                                MedievalBeastEntity medievalBeastEntity = (MedievalBeastEntity) spawnedEntity;
                                medievalBeastEntity.setRank(this.getRank());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("GrowthStage", this.growthStage);
        compound.putInt("WalkedSteps", this.walkedSteps);
        if (this.rank != null) {
            compound.putString("Rank", this.rank.getName());
        }
        if (this.catalyst != null) {
            compound.putString("Catalyst", this.catalyst);
        }
        compound.putBoolean("IsActive", this.isActive);
        if (this.owner != null) {
            compound.putUniqueId("Owner", this.owner);
        }
//        if (this.entityType != null) {
//            compound.putString("EntityType", this.entityType);
//        }
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.growthStage = nbt.getInt("GrowthStage");
        this.walkedSteps = nbt.getInt("WalkedSteps");
        if (nbt.contains("Rank")) {
            this.rank = Ranking.valueOf(nbt.getString("Rank"));
        }
        if (nbt.contains("Catalyst")) {
            this.catalyst = nbt.getString("Catalyst");
        }
        this.isActive = nbt.getBoolean("IsActive");
        if (nbt.hasUniqueId("Owner")) {
            this.owner = nbt.getUniqueId("Owner");
        }
//        if (nbt.contains("EntityType")) {
//            this.entityType = nbt.getString("EntityType");
//        }
        super.read(state, nbt);
    }


    // Create all the getters and setters
    public int getGrowthStage() {
        return this.growthStage;
    }

    public void setGrowthStage(int growthStageIn) {
        this.growthStage = growthStageIn;
    }

    public int getWalkedSteps() {
        return this.walkedSteps;
    }

    public void setWalkedSteps(int walkedStepsIn) {
        this.walkedSteps = walkedStepsIn;
    }

    public Ranking getRank() {
        return this.rank;
    }

    public void setRank(Ranking rankIn) {
        this.rank = rankIn;
    }

    public String getCatalyst() {
        return this.catalyst;
    }

    public void setCatalyst(String catalystIn) {
        this.catalyst = catalystIn;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActiveIn) {
        this.isActive = isActiveIn;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID ownerIn) {
        this.owner = ownerIn;
    }

    public EntityType<?> getEntityType() {
        return this.entityType;
    }

    public void setEntityType(EntityType<?> entityTypeIn) {
        this.entityType = entityTypeIn;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>
                (this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.serializeNBT().getBoolean("IsActive")) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.medieval_egg.hatch", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.medieval_egg.idle", false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
