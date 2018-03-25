package party.lemons.mite2shulker;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Sam on 20/03/2018.
 */
public class AiEndermiteBury extends EntityAIWander
{
	private EnumFacing facing;
	private boolean doMerge;

	public AiEndermiteBury(EntityEndermite endermite)
	{
		super(endermite, 1.0D, 10);
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if (this.entity.getAttackTarget() != null)
		{
			return false;
		}
		else if (!this.entity.getNavigator().noPath())
		{
			return false;
		}
		else
		{
			Random random = this.entity.getRNG();

			if (this.entity.world.getGameRules().getBoolean("mobGriefing") && random.nextInt(10) == 0)
			{
				this.facing = EnumFacing.random(random);
				BlockPos blockpos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
				IBlockState iblockstate = this.entity.world.getBlockState(blockpos);

				if (isValidBLock(iblockstate))
				{
					this.doMerge = true;
					return true;
				}
			}

			this.doMerge = false;
			return super.shouldExecute();
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return this.doMerge ? false : super.shouldContinueExecuting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		if (!this.doMerge)
		{
			super.startExecuting();
		}
		else
		{
			World world = this.entity.world;
			BlockPos blockpos = (new BlockPos(this.entity.posX, this.entity.posY + 0.5D, this.entity.posZ)).offset(this.facing);
			IBlockState iblockstate = world.getBlockState(blockpos);

			if (isValidBLock(iblockstate))
			{
				world.setBlockToAir(blockpos);
				EntityShulker shulker = new EntityShulker(world);
				shulker.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
				world.spawnEntity(shulker);

				this.entity.spawnExplosionParticle();
				this.entity.setDead();
			}
		}
	}

	public static boolean isValidBLock(IBlockState state)
	{
		return state.getBlock() == Blocks.PURPUR_BLOCK || state.getBlock() == Blocks.PURPUR_PILLAR;
	}
}
