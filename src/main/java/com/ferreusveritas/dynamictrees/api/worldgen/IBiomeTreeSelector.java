package com.ferreusveritas.dynamictrees.api.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Provides the tree used for a given biome
 * 
 * Mods should implement this interface and register it via the {@link TreeRegistry} to control which trees spawn in a {@link Biome}.
 * 
 * @author ferreusveritas
 */
public interface IBiomeTreeSelector {

	public String getName();
	
	
	public void init();
	
	/**
	 * 
	 * @param world
	 * @param biome
	 * @param pos
	 * @param dirt
	 * @return A decision on which tree to use.  Set decision to null for no tree.
	 */
	public Decision getTree(World world, Biome biome, BlockPos pos, IBlockState dirt);
	
	/**
	 * Used to determine which selector should run first.  Higher values are executed first.  Negative values are allowed.
	 * 
	 * @return priority number
	 */
	public int getPriority();
	
	public class Decision {
		private boolean handled;
		private DynamicTree tree;
		
		public Decision() {
			handled = false;
		}
		
		public Decision(DynamicTree tree) {
			this.tree = tree;
			handled = true;
		}
		
		public boolean isHandled() {
			return handled;
		}
		
		public DynamicTree getTree() {
			return tree;
		}
	}

}
