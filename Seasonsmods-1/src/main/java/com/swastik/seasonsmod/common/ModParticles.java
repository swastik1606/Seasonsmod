package com.swastik.seasonsmod.common;

import com.swastik.seasonsmod.SeasonsMod;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SeasonsMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> AUTUMN_LEAF = 
        PARTICLES.register("autumn_leaf", ()-> new SimpleParticleType(false));
}