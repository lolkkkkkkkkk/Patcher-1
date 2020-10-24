/*
 * Copyright © 2020 by Sk1er LLC
 *
 * All rights reserved.
 *
 * Sk1er LLC
 * 444 S Fulton Ave
 * Mount Vernon, NY
 * sk1er.club
 */

package club.sk1er.patcher.util.fov;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Allow for multiplying the FOV status for certain states that drastically change FOV.
 */
public class FovHandler {

    /**
     * Create a constant of the speed modifier's FOV status.
     */
    private static final float MODIFIER_SPEED = 0.1F;

    /**
     * Create a constant of the slowness modifier's FOV status.
     */
    private static final float MODIFIER_SLOWNESS = -0.075F;

    /**
     * Create a constant of how many states a bow creates when pulling it back.
     */
    private static final float MAX_BOW_TICKS = 20.0F;

    /**
     * Create a map containing the bow's current FOV modifier status.
     */
    private static final Map<Integer, Float> MODIFIER_BY_TICK = new HashMap<>();

    /**
     * Change the FOV multiplier accordingly to what the user states.
     *
     * @param event {@link FOVUpdateEvent}
     */
    @SubscribeEvent
    public void fovChange(FOVUpdateEvent event) {
        if (!PatcherConfig.allowFovModifying) return;

        float base = 1.0F;

        if (event.entity.isSprinting()) {
            base += (float) (0.15000000596046448 * PatcherConfig.sprintingFovModifier);
        }

        final ItemStack item = event.entity.getItemInUse();
        if (item != null && item.getItem() == Items.bow) {
            int duration = (int) Math.min(event.entity.getItemInUseDuration(), MAX_BOW_TICKS);
            float modifier = MODIFIER_BY_TICK.get(duration);
            base -= modifier * PatcherConfig.bowFovModifier;
        }

        final Collection<PotionEffect> effects = event.entity.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getPotionID() == 1) {
                base += (MODIFIER_SPEED * (effect.getAmplifier() + 1) * PatcherConfig.speedFovModifier);
            }

            if (effect.getPotionID() == 2) {
                base += (MODIFIER_SLOWNESS * (effect.getAmplifier() + 1) * PatcherConfig.slownessFovModifier);
            }
        }

        event.newfov = base;
    }

    // Input the current state and modifier.
    static {
        MODIFIER_BY_TICK.put(0, 0.0F);
        MODIFIER_BY_TICK.put(1, 3.7497282E-4f);
        MODIFIER_BY_TICK.put(2, 0.0015000105f);
        MODIFIER_BY_TICK.put(3, 0.0033749938f);
        MODIFIER_BY_TICK.put(4, 0.0059999824f);
        MODIFIER_BY_TICK.put(5, 0.009374976f);
        MODIFIER_BY_TICK.put(6, 0.013499975f);
        MODIFIER_BY_TICK.put(7, 0.01837498f);
        MODIFIER_BY_TICK.put(8, 0.023999989f);
        MODIFIER_BY_TICK.put(9, 0.030375004f);
        MODIFIER_BY_TICK.put(10, 0.037500024f);
        MODIFIER_BY_TICK.put(11, 0.04537499f);
        MODIFIER_BY_TICK.put(12, 0.05400002f);
        MODIFIER_BY_TICK.put(13, 0.063374996f);
        MODIFIER_BY_TICK.put(14, 0.07349998f);
        MODIFIER_BY_TICK.put(15, 0.084375024f);
        MODIFIER_BY_TICK.put(16, 0.096000016f);
        MODIFIER_BY_TICK.put(17, 0.10837501f);
        MODIFIER_BY_TICK.put(18, 0.121500015f);
        MODIFIER_BY_TICK.put(19, 0.13537502f);
        MODIFIER_BY_TICK.put(20, 0.14999998f);
    }
}
