package com.gnottero.medieval_beasts.entity.custom;

import com.gnottero.medieval_beasts.item.ModItems;
import com.gnottero.medieval_beasts.util.EntityRanking;
import com.gnottero.medieval_beasts.util.ParticleShapes;
import com.google.common.cache.RemovalCause;
import io.netty.util.internal.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import vazkii.patchouli.common.item.PatchouliItems;

import java.util.Objects;
import java.util.Random;

public class PixieEntity extends MedievalBeastEntity implements IAnimatable, IFlyingAnimal {

    private AnimationFactory factory = new AnimationFactory(this);
    public PixieEntity(EntityType<? extends MedievalBeastEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 10, true);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
            public boolean canEntityStandOnPos(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(false);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    public static AttributeModifierMap.MutableAttribute setAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0D)
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.4D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        if (itemstack.getItem() == ModItems.MOB_BOX.get()) {

            if (!this.isTamed()) {
                return ActionResultType.CONSUME;
            } else {
                if (this.getOwner() == playerIn) {
                    return ActionResultType.PASS;
                } else {
                    return ActionResultType.CONSUME;
                }
            }

        }

        if (!this.isTamed()) {

            if (itemstack.getItem().equals(Items.GOLDEN_APPLE)) {
                if (!playerIn.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                if (!this.isSilent()) {
                    this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                }
                if (!this.world.isRemote()) {
                    if (this.rand.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, playerIn)) {
                        this.setTamedBy(playerIn);

                        // Spawn the Lexicon
                        ItemStack stack = new ItemStack(PatchouliItems.book, 1);
                        stack.getOrCreateTag().putString("patchouli:book", "medieval_beasts:medieval_beasts_lexicon");
                        ItemEntity itementity = new ItemEntity(this.world, this.getPosX(), this.getPosY() + 0.5d, this.getPosZ(), stack);
                        itementity.setDefaultPickupDelay();
                        this.world.addEntity(itementity);

                        // Spawn the fist berry
                        ItemStack stack1 = new ItemStack(ModItems.CELESTIAL_BERRY.get(), 1);
                        stack1.setDisplayName(new TranslationTextComponent("item.medieval_beasts.original_berry").mergeStyle(TextFormatting.GOLD));
                        ItemEntity itementity1 = new ItemEntity(this.world, this.getPosX(), this.getPosY() + 0.5d, this.getPosZ(), stack1);
                        itementity1.setDefaultPickupDelay();
                        this.world.addEntity(itementity1);

                        this.world.setEntityState(this, (byte)7);
                    } else {
                        this.world.setEntityState(this, (byte)6);
                    }
                }
            }

            return ActionResultType.func_233537_a_(this.world.isRemote());

        } else {

            if (!this.world.isRemote()) {
                final int foodDuration = 12000;
                if (itemstack.getItem().equals(ModItems.CELESTIAL_BERRY.get()) && this.getFoodTime() + foodDuration < 20000) {
                    if (!playerIn.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    this.setFoodTime(this.getFoodTime() + foodDuration);
                    this.setSkillCooldown(0);
                    ((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX(), this.getPosY() + this.getEyeHeight(), this.getPosZ(), 20, Math.random(), Math.random(), Math.random(), 0.1f);
                    this.applyEffectToEntities((ServerWorld) world);
                    return ActionResultType.SUCCESS;
                }

                if (this.isOwner(playerIn) && hand.equals(Hand.MAIN_HAND)) {
                    this.fixedSitting();
                    return ActionResultType.SUCCESS;
                }
            }

            return super.getEntityInteractionResult(playerIn, hand);
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.6F;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.serializeNBT().getBoolean("NoAI")) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pixie.sitting", true));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pixie.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pixie.idle", true));
        return PlayState.CONTINUE;
    }


    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (this.isQueuedToSit()) {
                this.fixedSitting();
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public void livingTick() {
        World world = this.getEntityWorld();

        if (!world.isRemote() && world instanceof ServerWorld) {

            this.glowing = this.isQueuedToSit();
            this.updateFoodTime();
            this.updateSkillCooldown();
            this.updateAttributes();

            ServerWorld serverWorld = (ServerWorld) world;

            // If the entity is sitting (Disabled AI), make it look at the player (if player in range)
            if (this.serializeNBT().getBoolean("NoAI") && serverWorld.getEntities().findAny().isPresent()) {
                serverWorld.getEntities().filter(entity -> entity instanceof PlayerEntity).forEach(
                    entity -> {
                        PlayerEntity player = (PlayerEntity) entity;
                        if (this.getDistance(player) <= 8 && this.isOwner(player)) {
                            this.lookAt(EntityAnchorArgument.Type.EYES, player.getPositionVec());
                        }
                    }
                );
            }

            // Handle the dynamic skills and cooldown system
            if (this.getFoodTime() > 0) {

                // Every 20 ticks, generate all the graphic effects
                if (this.getSkillCooldown() % 20 == 1) {

                    // Check if the entity has an owner and the owner is a player
                    if (this.getOwner() != null && this.getOwner() instanceof PlayerEntity) {

                        PlayerEntity player = (PlayerEntity) this.getOwner();

                        serverWorld.getEntities().filter(entity -> entity instanceof MonsterEntity).forEach(
                            entity -> {
                                MonsterEntity monster = (MonsterEntity) entity;
                                if (monster.getActivePotionEffect(Effects.LEVITATION) != null && monster.getDistance(this.getEntity()) <= this.getRank().getRange()) {

                                    // Draw a particle line from the pixie to the monster entity
                                    ParticleShapes.drawParticleLine(new RedstoneParticleData(1.0f, 1.0f, 1.0f, 1.2f), serverWorld, this.getPositionVec().add(0.0d, this.getEyeHeight() - 0.5, 0.0d), monster.getPositionVec().add(0.0d, monster.getEyeHeight(), 0.0d), 0.2d);

                                    // Handle the effects for Rank D Pixies
                                    if (Objects.requireNonNull(this.getRank()).getAttrMultiplier() >= EntityRanking.Ranking.D.getAttrMultiplier() && monster.isAlive()) {
                                        monster.attackEntityFrom(DamageSource.GENERIC, (float) (monster.getHealth() * 0.25));
                                    }

                                    // Handle the effects for Rank C Pixies
                                    if (Objects.requireNonNull(this.getRank()).getAttrMultiplier() >= EntityRanking.Ranking.C.getAttrMultiplier() && monster.isAlive() && Math.random() <= 0.25) {
                                        monster.onKillCommand();
                                    }

                                }

                            }
                        );

                    }

                    // Generate the particle circle on skill activation
                    ParticleShapes.particleCircle(ParticleTypes.SOUL_FIRE_FLAME, (ServerWorld) this.world, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0f, 0.5f, 0.0f, 1.5f, 30, 1);
                }

                // Apply the levitation effect to all the entity inside the skill range
                if (this.getSkillCooldown() == 0 && serverWorld.getEntities().findAny().isPresent()) {
                    this.applyEffectToEntities(serverWorld);
                    this.setSkillCooldown(this.getRank().getCooldown());
                }
            }
        }

        super.livingTick();
    }

    public void applyEffectToEntities(ServerWorld serverWorld) {
        serverWorld.getEntities().filter(entity -> entity instanceof MonsterEntity).forEach(
            entity -> {
                if (entity.getDistance(this.getEntity()) <= this.getRank().getRange()) {
                    if (((MonsterEntity) entity).getActivePotionEffect(Effects.LEVITATION) == null && entity.isOnGround()) {
                        ((MonsterEntity) entity).addPotionEffect(new EffectInstance(Effects.LEVITATION, this.getRank().getDuration(), 0));
                    }
                }
            }
        );
    }

    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    protected float playFlySound(float volume) {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        return 0.5F;
    }

    protected boolean makeFlySound() {
        return true;
    }

    protected float getSoundPitch() {
        return getPitch(this.rand);
    }

    public static float getPitch(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public double getDefaultHealth() {
        return 6.0D;
    }
}
