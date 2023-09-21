package com.ldtteam.cleanswing;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod("cleanswing")
public class CleanSwing
{
    public CleanSwing()
    {
        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(this.getClass());
    }

    @SubscribeEvent
    public static void onBlock(final PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getLevel().getBlockState(event.getPos()).getCollisionShape(event.getLevel(), event.getPos()).isEmpty())
        {
            final List<Entity> entities = event.getLevel().getEntities(null, new AABB(event.getPos()).expandTowards(event.getEntity().getLookAngle()));
            if (!entities.isEmpty())
            {
                if (event.getItemStack().canPerformAction(ToolActions.SWORD_SWEEP))
                {
                    for (final Entity entity : entities)
                    {
                        event.getEntity().attack(entity);
                    }
                }
                else
                {
                    event.getEntity().attack(entities.get(0));
                }

                event.setCanceled(true);
                event.getEntity().swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
