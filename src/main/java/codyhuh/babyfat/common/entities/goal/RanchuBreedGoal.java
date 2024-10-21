package codyhuh.babyfat.common.entities.goal;

import codyhuh.babyfat.common.entities.Ranchu;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class RanchuBreedGoal extends Goal {
	private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
	protected final Ranchu animal;
	private final Class<? extends Ranchu> mateClass;
	protected final Level world;
	protected Ranchu targetMate;
	private int spawnBabyDelay;
	private final double moveSpeed;

	public RanchuBreedGoal(Ranchu animal, double speedIn) {
		this(animal, speedIn, animal.getClass());
	}

	public RanchuBreedGoal(Ranchu animal, double moveSpeed, Class<? extends Ranchu> mateClass) {
		this.animal = animal;
		this.world = animal.level();
		this.mateClass = mateClass;
		this.moveSpeed = moveSpeed;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean canUse() {
		if (!(this.animal.level() instanceof ServerLevel serverLevel))
			return false;

        List<? extends Ranchu> list = serverLevel.getNearbyEntities(this.mateClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));

		assert !this.animal.isBaby();

		long time = this.animal.level().getLevelData().getDayTime();

		if (!this.animal.isInLove() || this.animal.isBaby() || time % 24000 <= 23000 || !this.animal.fromBucket()) {
			return false;
		} else if (list.size() < 6) {
			this.targetMate = this.getNearbyMate();
			return this.targetMate != null;
		} else {
			return false;
		}
	}

	public boolean canContinueToUse() {
		return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
	}

	public void stop() {
		this.targetMate = null;
		this.spawnBabyDelay = 0;
	}

	public void tick() {
		this.animal.getLookControl().setLookAt(this.targetMate, 10.0F, (float) this.animal.getMaxHeadXRot());
		this.animal.getNavigation().moveTo(this.targetMate, this.moveSpeed);
		++this.spawnBabyDelay;
		if (this.spawnBabyDelay >= 60 && this.animal.distanceToSqr(this.targetMate) < 9.0D) {
			this.spawnBaby();
		}
		if (this.animal.tickCount % 20 == 0) {
			this.animal.level().broadcastEntityEvent(this.animal, (byte) 18);
		}
	}

	@Nullable
	private Ranchu getNearbyMate() {
		if (!(this.animal.level() instanceof ServerLevel serverLevel))
			return null;

		List<? extends Ranchu> list = serverLevel.getNearbyEntities(this.mateClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(20.0D));
		double d0 = Double.MAX_VALUE;
		Ranchu animalentity = null;

		for (Ranchu ranchu : list) {
			if (this.animal.canMate(ranchu) && this.animal.distanceToSqr(ranchu) < d0 && !ranchu.isBaby()) {
				animalentity = ranchu;
				d0 = this.animal.distanceToSqr(ranchu);
			}
		}

		return animalentity;
	}

	protected void spawnBaby() {
		this.animal.spawnChildFromBreeding((ServerLevel) this.world, this.targetMate);
	}
}