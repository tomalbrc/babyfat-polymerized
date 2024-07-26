package codyhuh.babyfat.util;

import de.tomalbrc.bil.api.AnimatedHolder;
import de.tomalbrc.bil.api.Animator;
import net.minecraft.world.entity.LivingEntity;

public class AnimationHelper {
    public static void updateAquaticWalkAnimation(LivingEntity entity, AnimatedHolder holder) {
        Animator animator = holder.getAnimator();
        if (entity.isInWater()) {
            if ((entity.getDeltaMovement().length() > 0.05 || entity.walkAnimation.speed() > 0.02)) {
                animator.pauseAnimation("idle");
                animator.pauseAnimation("walk");
                animator.playAnimation("swim");
            } else {
                animator.pauseAnimation("swim");
                animator.pauseAnimation("walk");
                animator.playAnimation("idle");
            }
        } else {
            animator.pauseAnimation("idle");
            animator.pauseAnimation("swim");
            animator.playAnimation("walk");
        }
    }

    public static void updateHurtColor(LivingEntity entity, AnimatedHolder holder) {
        if (entity.hurtTime > 0 || entity.deathTime > 0)
            holder.setColor(0xff7e7e);
        else
            holder.clearColor();
    }
}