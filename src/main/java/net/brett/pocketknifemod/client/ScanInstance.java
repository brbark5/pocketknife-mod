package net.brett.pocketknifemod.client;

import net.brett.pocketknifemod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleEffect;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class ScanInstance {
    private static final List<Block> DETECTABLE_ORES = List.of(
            //Iron
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            //Gold
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            //Diamond
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            //Copper
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            //Lapis
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            //Emerald
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            //Coal
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            //Steel
            ModBlocks.STEEL_ORE, ModBlocks.DEEPSLATE_STEEL_ORE,
            ModBlocks.NETHER_STEEL_ORE, ModBlocks.END_STONE_STEEL_ORE
    );

    private static final Map<Block, ParticleEffect> ORE_PARTICLES = Map.ofEntries(
            // Iron - silver/gray
            Map.entry(Blocks.IRON_ORE, new DustParticleEffect(new Vector3f(0.78f, 0.78f, 0.78f), 1.2f)),
            Map.entry(Blocks.DEEPSLATE_IRON_ORE, new DustParticleEffect(new Vector3f(0.78f, 0.78f, 0.78f), 1.2f)),

            // Gold - yellow
            Map.entry(Blocks.GOLD_ORE, new DustParticleEffect(new Vector3f(1.0f, 0.84f, 0.0f), 1.5f)),
            Map.entry(Blocks.DEEPSLATE_GOLD_ORE, new DustParticleEffect(new Vector3f(1.0f, 0.84f, 0.0f), 1.5f)),
            Map.entry(Blocks.NETHER_GOLD_ORE, new DustParticleEffect(new Vector3f(1.0f, 0.84f, 0.0f), 1.5f)),

            // Diamond -cyan/blue
            Map.entry(Blocks.DIAMOND_ORE, new DustParticleEffect(new Vector3f(0.3f, 0.85f, 0.95f), 1.5f)),
            Map.entry(Blocks.DEEPSLATE_DIAMOND_ORE, new DustParticleEffect(new Vector3f(0.3f, 0.85f, 0.95f), 1.5f)),

            // Copper - orange-brown
            Map.entry(Blocks.COPPER_ORE, new DustParticleEffect(new Vector3f(0.8f, 0.45f, 0.25f), 1.2f)),
            Map.entry(Blocks.DEEPSLATE_COPPER_ORE, new DustParticleEffect(new Vector3f(0.8f, 0.45f, 0.25f), 1.2f)),

            // Lapis - deep blue
            Map.entry(Blocks.LAPIS_ORE, new DustParticleEffect(new Vector3f(0.15f, 0.25f, 0.85f), 1.3f)),
            Map.entry(Blocks.DEEPSLATE_LAPIS_ORE, new DustParticleEffect(new Vector3f(0.15f, 0.25f, 0.85f), 1.3f)),

            // Emerald - green
            Map.entry(Blocks.EMERALD_ORE, new DustParticleEffect(new Vector3f(0.1f, 0.9f, 0.3f), 1.3f)),
            Map.entry(Blocks.DEEPSLATE_EMERALD_ORE, new DustParticleEffect(new Vector3f(0.1f, 0.9f, 0.3f), 1.3f)),

            // Coal - dark gray/black
            Map.entry(Blocks.COAL_ORE, new DustParticleEffect(new Vector3f(0.2f, 0.2f, 0.2f), 1.2f)),
            Map.entry(Blocks.DEEPSLATE_COAL_ORE, new DustParticleEffect(new Vector3f(0.2f, 0.2f, 0.2f), 1.2f)),

            // Steel - IDK
            Map.entry(ModBlocks.STEEL_ORE, new DustParticleEffect(new Vector3f(0.6f, 0.6f, 0.65f), 1.3f)),
            Map.entry(ModBlocks.DEEPSLATE_STEEL_ORE, new DustParticleEffect(new Vector3f(0.5f, 0.5f, 0.55f), 1.3f)),
            Map.entry(ModBlocks.NETHER_STEEL_ORE, new DustParticleEffect(new Vector3f(0.7f, 0.4f, 0.4f), 1.3f)),
            Map.entry(ModBlocks.END_STONE_STEEL_ORE, new DustParticleEffect(new Vector3f(0.75f, 0.75f, 0.55f), 1.3f))
    );


    private final BlockPos center;
    private final World world;
    private final PlayerEntity player;
    private final double maxRadius;
    private final double expandSpeed = 0.8;
    private double currentRadius = 0.5; //was 0

    private final List<BlockPos> orePositions;
    private final Set<BlockPos> alreadyPinged = new HashSet<>();

    public ScanInstance(World world, PlayerEntity player, BlockPos center, double maxRadius) {
        this.world = world;
        this.player = player;
        this.center = center;
        this.maxRadius = maxRadius;
        this.orePositions = findOresInRadius(world, center, (int) maxRadius);
    }

    private List<BlockPos> findOresInRadius(World world, BlockPos center, int radius) {
        List<BlockPos> results = new ArrayList<>();
        for (BlockPos pos : BlockPos.iterate(
                center.add(-radius, -radius, -radius),
                center.add(radius, radius, radius))) {
            if (pos.getSquaredDistance(center) <= (double) radius * radius) {
                if (DETECTABLE_ORES.contains(world.getBlockState(pos).getBlock())) {
                    results.add(pos.toImmutable());
                }
            }
        }
        return results;
    }

    /** Returns false when the scan is finished and should be removed. */
    public boolean tick() {
        if (currentRadius > maxRadius) return false;

        spawnRingParticles();
        checkForPings();

        currentRadius += expandSpeed;
        return true;
    }

    private void spawnRingParticles() {
        int points = Math.max(8, (int) (currentRadius * 2)); //was * 8
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double x = center.getX() + 0.5 + currentRadius * Math.cos(angle);
            double z = center.getZ() + 0.5 + currentRadius * Math.sin(angle);
            world.addParticle(ParticleTypes.END_ROD, x, center.getY() + 0.1, z, 0, 0, 0);
        }
    }

    private void checkForPings() {
        for (BlockPos orePos : orePositions) {
            if (alreadyPinged.contains(orePos)) continue;

            double oreDist = Math.sqrt(center.getSquaredDistance(orePos));
            if (currentRadius >= oreDist) {
                alreadyPinged.add(orePos);
                Block foundBlock = world.getBlockState(orePos).getBlock();
                playPing(oreDist, foundBlock);
            }
        }
    }

    private void playPing(double distance, Block foundBlock) {
        float pitch = (float) MathHelper.clamp(2.0 - (distance / maxRadius) * 1.5, 0.5, 2.0);

        world.playSound(null, player.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 1.0f, pitch);

        ParticleEffect particle = ORE_PARTICLES.getOrDefault(foundBlock, ParticleTypes.END_ROD);

        for (int i = 0; i < 6; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.8;
            world.addParticle(particle,
                    player.getX() + offsetX, player.getY() + 1.0, player.getZ() + offsetZ,
                    0, 0.05, 0);
        }
    }
}