package com.ferreusveritas.dynamictrees.blocks.rootyblocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class RootyWaterBlock extends RootyBlock implements IWaterLoggable {

    protected static final AxisAlignedBB WATER_ROOTS_AABB = new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public RootyWaterBlock(Block primitiveDirt) {
        super(Properties.copy(primitiveDirt).randomTicks(), "rooty_"+ primitiveDirt.getRegistryName().getPath(), primitiveDirt);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    @Override
    public int getRadiusForConnection(BlockState blockState, IBlockReader blockAccess, BlockPos pos, BranchBlock from, Direction side, int fromRadius) {
        return 1;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        BlockState upState = world.getBlockState(pos.above());
        if (TreeHelper.isBranch(upState))
            return new ItemStack(TreeHelper.getBranch(upState).getFamily().getBranchItem());
        return ItemStack.EMPTY;
    }

    @Override
    public float getHardness(IBlockReader worldIn, BlockPos pos) {
        return (float) (0.5 * DTConfigs.ROOTY_BLOCK_HARDNESS_MULTIPLIER.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.create(WATER_ROOTS_AABB);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getDecayBlockState(BlockState state, IWorld access, BlockPos pos) {
        if (state.hasProperty(WATERLOGGED) && !state.getValue(WATERLOGGED))
            return Blocks.AIR.defaultBlockState();
        return super.getDecayBlockState(state,access, pos);
    }


    ///////////////////////////////////////////
    // RENDERING
    ///////////////////////////////////////////

    @Override
    public boolean getColorFromBark() {
        return true;
    }

    public boolean fallWithTree (BlockState state, World world, BlockPos pos){
        //The block is removed when this is checked because it means it got attached to a tree
        world.setBlockAndUpdate(pos, getDecayBlockState(state, world, pos));
        return true;
    }

}