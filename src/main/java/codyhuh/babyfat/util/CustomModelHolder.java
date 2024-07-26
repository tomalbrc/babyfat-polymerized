package codyhuh.babyfat.util;

import codyhuh.babyfat.common.entities.Ranchu;
import com.mojang.math.Transformation;
import de.tomalbrc.bil.core.holder.entity.living.LivingEntityHolder;
import de.tomalbrc.bil.core.holder.wrapper.DisplayWrapper;
import de.tomalbrc.bil.core.model.Model;
import de.tomalbrc.bil.core.model.Pose;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CustomModelHolder extends LivingEntityHolder<Ranchu> {

    public CustomModelHolder(Ranchu parent, Model model) {
        super(parent, model);
    }

    @Override
    protected void applyPose(Pose pose, DisplayWrapper display) {
        Matrix4f matrix4f;
        var translation = pose.readOnlyTranslation().sub(0.f, this.parent.getBbHeight()-(this.parent.isBaby() ? -0.2f : 0.075f), 0, new Vector3f());
        var tr = new Transformation(translation.mul(this.entityScale), pose.leftRotation(), pose.readOnlyScale().mul(this.entityScale, new Vector3f()), pose.rightRotation());
        matrix4f = tr.getMatrix();

        matrix4f
                .rotateLocalZ(0)
                .rotateLocalX(parent.xRotO * Mth.DEG_TO_RAD)
                .rotateLocalY(-(parent.yRotO) * Mth.DEG_TO_RAD)
        ;

        display.element().setTransformation(matrix4f);
        display.element().startInterpolationIfDirty();
    }

    @Override
    public void updateElement(DisplayWrapper display, @Nullable Pose pose) {
        if (pose == null) {
            this.applyPose(display.getLastPose(), display);
        } else {
            this.applyPose(pose, display);
        }
    }
}
