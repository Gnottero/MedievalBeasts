package com.gnottero.medieval_beasts.entity.custom;

import com.gnottero.medieval_beasts.config.MedievalBeastsConfig;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.gnottero.medieval_beasts.util.EntityRanking.Ranking;

public class MedievalBeastEntity extends TameableEntity implements IRankableEntity {

    protected static final DataParameter<String> RANK = EntityDataManager.createKey(MedievalBeastEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> FOOD_TIME = EntityDataManager.createKey(MedievalBeastEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SKILL_COOLDOWN = EntityDataManager.createKey(MedievalBeastEntity.class, DataSerializers.VARINT);

    protected MedievalBeastEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 1.0F, 2.0F, true));
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
    }

    public void fixedSitting() {
        this.setSitting(!this.isQueuedToSit());
        boolean flag = this.isQueuedToSit();
        this.setNoAI(flag);
    }

    public void updateAttributes() {
        Ranking rank = this.getRank();
        ModifiableAttributeInstance healthAttribute = this.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttribute != null && rank != null) {
            double rankBasedHealth = this.getDefaultHealth() * MedievalBeastsConfig.ATTRIBUTE_MULTIPLIER.get() * rank.getAttrMultiplier();

            if (healthAttribute.getValue() != rankBasedHealth) {
                healthAttribute.setBaseValue(rankBasedHealth);
                this.setHealth((float) Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).getValue());
            }
        }
    }

    // [Start] Register Custom NBTs

    protected void registerData() {
        this.dataManager.register(RANK, "F");
        this.dataManager.register(FOOD_TIME, 0);
        this.dataManager.register(SKILL_COOLDOWN, 0);
        super.registerData();
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        this.writeFoodTimeNbt(compound);
        this.writeSkillCooldownNbt(compound);
        this.writeRankNbt(compound);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if(!world.isRemote()) {
            this.readFoodTimeNbt(compound);
            this.readSkillCooldown(compound);
            this.readRankNbt(compound);
        }
    }

    // [End] Register Custom NBTs



    // [Start] Create all the getters and setters for the custom tags

    public int getFoodTime() {
        return this.dataManager.get(FOOD_TIME);
    }

    public int getSkillCooldown() {
        return this.dataManager.get(SKILL_COOLDOWN);
    }

    public double getDefaultHealth() {
        return 0;
    }

    public void setFoodTime(int time) {
        this.dataManager.set(FOOD_TIME, time);
    }

    public void setSkillCooldown(int time) {
        this.dataManager.set(SKILL_COOLDOWN, time);
    }

    @Nullable
    public Ranking getRank() {
        return Ranking.valueOf(this.dataManager.get(RANK));
    }


    public void setRank(Ranking entityRankIn) {
        this.dataManager.set(RANK, entityRankIn.getName());
    }

    // [End] Create all the getters and setters for the custom tags

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }
}
