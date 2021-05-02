package com.ferreusveritas.dynamictrees.blocks.leaves;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.treepacks.JsonApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.resources.JsonRegistryEntryReloadListener;
import com.ferreusveritas.dynamictrees.util.json.JsonHelper;
import com.ferreusveritas.dynamictrees.util.json.JsonPropertyApplierLists;
import com.google.gson.JsonObject;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

import java.util.function.Consumer;

/**
 * @author Harley O'Connor
 */
public final class LeavesPropertiesManager extends JsonRegistryEntryReloadListener<LeavesProperties> {

    public LeavesPropertiesManager() {
        super(LeavesProperties.REGISTRY, JsonApplierRegistryEvent.LEAVES_PROPERTIES);
    }

    @Override
    public void registerAppliers() {
        this.reloadAppliers.register("requires_shears", Boolean.class, LeavesProperties::setRequiresShears)
                .register("cell_kit", CellKit.class, LeavesProperties::setCellKit)
                .register("smother", Integer.class, LeavesProperties::setSmotherLeavesMax)
                .register("light_requirement", Integer.class, LeavesProperties::setLightRequirement)
                .register("fire_spread", Integer.class, LeavesProperties::setFireSpreadSpeed)
                .register("flammability", Integer.class, LeavesProperties::setFlammability)
                .register("connect_any_radius", Boolean.class, LeavesProperties::setConnectAnyRadius);

        // Primitive leaves are needed both client and server (so cannot be done on setup).
        this.setupAppliers.register("primitive_leaves", Block.class, LeavesProperties::setPrimitiveLeaves);

        super.registerAppliers();
    }

    @Override
    protected void preLoad(JsonObject jsonObject, LeavesProperties leavesProperties, Consumer<String> errorConsumer, Consumer<String> warningConsumer) {
        // Generate block by default, but allow it to be turned off.
        if (JsonHelper.getOrDefault(jsonObject, "generate_block", Boolean.class, true)) {
            leavesProperties.generateDynamicLeaves(JsonHelper.getBlockProperties(jsonObject,
                    leavesProperties.getDefaultMaterial(), leavesProperties.getDefaultMaterial().getColor(),
                    leavesProperties::getDefaultBlockProperties, errorConsumer, warningConsumer));
        }
    }

}