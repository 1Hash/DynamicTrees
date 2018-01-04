package com.ferreusveritas.dynamictrees.systems.featuregen;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class FeatureGenFruit implements IGenFeature {

	protected Species species;
	protected IBlockState fruitState;
	protected float verSpread = 30;
	protected int qty = 4;
	protected float rayDistance = 5;
	protected boolean enableHash = true;
	
	public FeatureGenFruit(Species species, IBlockState fruitState) {
		this.species = species;
		setFruit(fruitState);
	}
	
	public FeatureGenFruit setFruit(IBlockState fruitState) {
		this.fruitState = fruitState;
		return this;
	}
	
	public FeatureGenFruit setQuantity(int qty) {
		this.qty = qty;
		return this;
	}
	
	public FeatureGenFruit setRayDistance(float rayDistance) {
		this.rayDistance = rayDistance;
		return this;
	}
	
	public FeatureGenFruit setEnableHash(boolean doHash) {
		this.enableHash = doHash;
		return this;
	}
	
	@Override
	public void gen(World world, BlockPos treePos, List<BlockPos> endPoints) {
		if(!endPoints.isEmpty()) {
			for(int i = 0; i < qty; i++) {
				BlockPos endPoint = endPoints.get(world.rand.nextInt(endPoints.size()));
				addFruit(world, species, treePos, endPoint);
			}
		}
	}
	
	protected void addFruit(World world, Species species, BlockPos treePos, BlockPos branchPos) {
		RayTraceResult result = species.branchRayTrace(world, treePos, branchPos, 90, verSpread, rayDistance);
		
		if(result != null) {
			BlockPos leafPos = result.getBlockPos();
			if(world.getBlockState(leafPos).getBlock() instanceof BlockLeaves) {
				while(world.getBlockState(leafPos).getBlock() instanceof BlockLeaves) {
					leafPos = leafPos.down();
				}
				if(world.isAirBlock(leafPos) && ( !enableHash || ( (coordHashCode(leafPos) & 1) != 0) ) ) {
					world.setBlockState(leafPos, fruitState);
				}
			}
		}
	}
	
	public static int coordHashCode(BlockPos pos) {
		int hash = (pos.getX() * 4111 ^ pos.getY() * 271 ^ pos.getZ() * 3067) >> 1;
		return hash & 0xFFFF;
	}
		
}