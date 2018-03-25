package party.lemons.mite2shulker;

import net.minecraft.entity.monster.EntityEndermite;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sam on 20/03/2018.
 */
@Mod(modid = Mite2Shulker.MODID, name = Mite2Shulker.NAME, version = Mite2Shulker.VERSION)
@Mod.EventBusSubscriber
public class Mite2Shulker
{
	public static final String MODID = "mite2shulker";
	public static final String NAME = "Mite2Shulker";
	public static final String VERSION = "1.0.0";

	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityEndermite)
		{
			EntityEndermite mite = (EntityEndermite) event.getEntity();
			mite.tasks.addTask(5, new AiEndermiteBury(mite));
		}
	}
}

