package codyhuh.babyfat.common.entities;

import codyhuh.babyfat.BabyFat;
import codyhuh.babyfat.common.entities.goal.RanchuBreedGoal;
import codyhuh.babyfat.registry.BFBlocks;
import codyhuh.babyfat.registry.BFEntities;
import codyhuh.babyfat.registry.BFItems;
import codyhuh.babyfat.util.AnimationHelper;
import codyhuh.babyfat.util.CustomModelHolder;
import de.tomalbrc.bil.api.AnimatedEntity;
import de.tomalbrc.bil.api.AnimatedEntityHolder;
import de.tomalbrc.bil.core.holder.entity.living.LivingEntityHolder;
import de.tomalbrc.bil.core.model.Model;
import de.tomalbrc.bil.file.loader.AjModelLoader;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public class Ranchu extends Animal implements AnimatedEntity, Bucketable {
	public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BabyFat.MOD_ID, "ranchu");
	public static Model model = AjModelLoader.load(ID);

	public static final Ingredient FOOD_ITEMS = Ingredient.of(BFItems.WATER_LETTUCE);
	public static final int MAX_VARIANTS = 303;
	private int variant;

	private boolean fromBucket;

	private final LivingEntityHolder holder;

	public Ranchu(EntityType<? extends Animal> type, Level worldIn) {
		super(type, worldIn);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.setPathfindingMalus(PathType.WATER, 0.0F);

		this.holder = new CustomModelHolder(this, model);
		EntityAttachment.ofTicking(this.holder, this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(1, new RanchuBreedGoal(this, 1.25D));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.5, 1));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 2.5D);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(BFBlocks.WATER_LETTUCE.asItem());
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
		if (reason == MobSpawnType.BUCKET) {
			return spawnDataIn;
		}
		int i;
		if(reason == MobSpawnType.SPAWN_EGG){
			i = worldIn.getRandom().nextInt(302);
		}else{
			i = worldIn.getRandom().nextInt(3);
		}
		this.setVariant(i);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
	}

	public static boolean checkFishSpawnRules(EntityType<? extends Ranchu> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
		return worldIn.getBlockState(pos).is(Blocks.WATER) && worldIn.getBlockState(pos.above()).is(Blocks.WATER);
	}

	@Override
	public void setInLove(@Nullable Player player) {
		if (this.level() instanceof ServerLevel level) {
			for (int i = 0; i < 7; ++i) {
				double xOffset = this.random.nextGaussian() * 0.02;
				double yOffset = this.random.nextGaussian() * 0.02;
				double zOffset = this.random.nextGaussian() * 0.02;
				level.sendParticles(ParticleTypes.HEART, this.getRandomX(1), this.getRandomY() + 0.5, this.getRandomZ(1), 0, xOffset, yOffset, zOffset, 0);
			}
		}

		super.setInLove(player);
	}

	private boolean isFromBucket() {
		return this.fromBucket;
	}

	@Override
	public boolean fromBucket() {
		return this.fromBucket;
	}

	public void setFromBucket(boolean fromBucket) {
		this.fromBucket = fromBucket;
	}

	@Override
	public void loadFromBucketTag(CompoundTag compound) {
		Bucketable.loadDefaultDataFromBucketTag(this, compound);
		this.setVariant(compound.getInt("Variant"));
		this.setAge(compound.getInt("Age"));
	}

	@Override
	public void saveToBucketTag(ItemStack bucket) {
		Bucketable.saveDefaultDataToBucketTag(this, bucket);
		CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, (compoundTag) -> {
			compoundTag.putInt("Variant", this.getVariant());
			compoundTag.putInt("Age", this.getAge());
		});
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(BFItems.RANCHU_BUCKET);
	}

	@Override
	public SoundEvent getPickupSound() {
		return SoundEvents.BUCKET_EMPTY_FISH;
	}

	public int getVariant() {
		return this.variant;
	}

	public void setVariant(int variant) {
		this.holder.getVariantController().setVariant(String.valueOf(variant));
		this.variant = variant;
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader worldIn) {
		return worldIn.isUnobstructed(this);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Variant", getVariant());
		compound.putBoolean("FromBucket", this.isFromBucket());
		compound.putBoolean("Bucketed", this.fromBucket());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setVariant(Mth.clamp(compound.getInt("Variant"), 0, MAX_VARIANTS - 1));
		this.setFromBucket(compound.getBoolean("FromBucket"));
		this.setFromBucket(compound.getBoolean("Bucketed"));
	}

	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.isFromBucket();
	}

	protected void updateAir(int p_209207_1_) {
		if (this.isAlive() && !this.isInWaterOrBubble()) {
			this.setAirSupply(p_209207_1_ - 1);
			if (this.getAirSupply() == -20) {
				this.setAirSupply(0);
				this.hurt(damageSources().drown(), 2.0F);
			}
		} else {
			this.setAirSupply(300);
		}

	}

	@Override
	public void baseTick() {
		int i = this.getAirSupply();
		super.baseTick();
		this.updateAir(i);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.tickCount % 2 == 0) {
			AnimationHelper.updateAquaticWalkAnimation(this, this.holder);
			AnimationHelper.updateHurtColor(this, this.holder);
		}
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public boolean canBeLeashed() {
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		return new WaterBoundPathNavigation(this, worldIn);
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return !this.fromBucket() && !this.hasCustomName();
	}

	@Override
	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
			this.setOnGround(false);
			this.hasImpulse = true;
			this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
		}

		long time = level().getLevelData().getDayTime();

		if (canFindLettuce() && time % 24000 > 23000 && !this.isBaby()) {
			setInLoveTime(40);
		}
		if (canFindLettuce() && time % 13000 > 12500) {
			setInLoveTime(40);
		}
		super.aiStep();
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();

		if (this.forcedAgeTimer > 0) {
			if (this.forcedAgeTimer % 4 == 0) {
				ServerLevel serverLevel = (ServerLevel) this.level();
				serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1), this.getRandomY() + 0.5, this.getRandomZ(1), 0, 0.0, 0.0, 0.0, 0.0);
			}

			--this.forcedAgeTimer;
		}
	}

	private boolean canFindLettuce() {
		BlockPos blockpos = blockPosition();
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

		for(int i = 0; i < 10; ++i) {
			for(int j = 0; j < 10; ++j) {
				for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
					for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
						blockpos$mutable.setWithOffset(blockpos, k, i, l);
						if (level().getBlockState(blockpos$mutable).is(BFBlocks.WATER_LETTUCE)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(0.01F, travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(travelVector);
		}
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	@Nullable
	@Override
	public Ranchu getBreedOffspring(ServerLevel serverLevel, AgeableMob ranchuB) {
		Ranchu child = BFEntities.RANCHU.create(serverLevel);
		RandomSource rand = this.getRandom();
		if (ranchuB instanceof Ranchu) {
			// Feral + Feral
			if (this.getVariant() <= 2 && ((Ranchu) ranchuB).getVariant() <= 2) {
				if (rand.nextFloat() < 0.15) {
					child.setVariant(rand.nextInt(Ranchu.MAX_VARIANTS - 3) + 3);
				} else {
					child.setVariant(rand.nextInt(3) + 1);
				}
			}

			// Fancy + Fancy
			else if (this.getVariant() > 2 && ((Ranchu) ranchuB).getVariant() > 2) {
				child.setVariant(rand.nextInt(Ranchu.MAX_VARIANTS - 3) + 3);
			}

			// Feral + Fancy
			else if (this.getVariant() <= 2 || ((Ranchu) ranchuB).getVariant() <= 2 && this.getVariant() > 2 || ((Ranchu) ranchuB).getVariant() > 2) {
				if (rand.nextBoolean()) {
					child.setVariant(rand.nextInt(Ranchu.MAX_VARIANTS - 3) + 3);
				} else {
					child.setVariant(rand.nextInt(3) + 1);
				}
			}
		}
		child.setPersistenceRequired();

		return child;
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.FISH_SWIM;
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.COD_FLOP;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.COD_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.COD_HURT;
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(BFItems.RANCHU_SPAWN_EGG);
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		Optional<InteractionResult> result = Bucketable.bucketMobPickup(player, hand, this);

		if(result.isPresent() && result.get().consumesAction()){
			return result.get();
		}else {
			ItemStack itemstack = player.getItemInHand(hand);
			if (this.isFood(itemstack)) {
				int i = this.getAge();
				if (!this.level().isClientSide && i == 0 && this.canFallInLove()) {
					this.usePlayerItem(player, hand, itemstack);
					this.setInLove(player);
					this.setInLoveTime(24000);
					return InteractionResult.SUCCESS;
				}

				if (this.isBaby()) {
					this.usePlayerItem(player, hand, itemstack);
					this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
					return InteractionResult.sidedSuccess(this.level().isClientSide);
				}

				if (this.level().isClientSide) {
					return InteractionResult.CONSUME;
				}
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public AnimatedEntityHolder getHolder() {
		return this.holder;
	}
}
