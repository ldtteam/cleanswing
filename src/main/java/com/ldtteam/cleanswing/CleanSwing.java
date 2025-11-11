package com.ldtteam.cleanswing;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

@Mod("cleanswing")
public class CleanSwing
{
    public CleanSwing()
    {
        NeoForge.EVENT_BUS.register(this.getClass());
    }

    @SubscribeEvent
    public static void onBlock(final PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getLevel().getBlockState(event.getPos()).getCollisionShape(event.getLevel(), event.getPos()).isEmpty() && event.getEntity() != null && !(event.getEntity() instanceof FakePlayer))
        {
            final VoxelShape interactionShape = event.getLevel().getBlockState(event.getPos()).getShape(event.getLevel(), event.getPos());
            if (interactionShape.isEmpty())
            {
                return;
            }

            final List<Entity> entities = event.getLevel().getEntities(null, interactionShape.bounds().move(event.getPos()).expandTowards(0.2,0.2,0.2).expandTowards(-0.2,-0.2,-0.2));
            if (!entities.isEmpty())
            {
                boolean foundEntity = false;
                final boolean sweepin = event.getItemStack().canPerformAction(ItemAbilities.SWORD_SWEEP);
                for (final Entity entity : entities)
                {
                    if (entity instanceof LivingEntity && entity.isAttackable() && !entity.getUUID().equals(event.getEntity().getUUID()))
                    {
                        if (event.getLevel().isClientSide())
                        {
                            Minecraft.getInstance().gameMode.attack(event.getEntity(), entity);
                        }
                        foundEntity = true;
                        if (!sweepin)
                        {
                            break;
                        }
                    }
                }

                if (foundEntity)
                {
                    event.setCanceled(true);
                    event.getEntity().swing(InteractionHand.MAIN_HAND);
                }
            }
        }
    }
}
