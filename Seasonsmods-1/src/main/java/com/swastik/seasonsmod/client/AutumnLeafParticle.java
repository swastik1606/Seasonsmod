package com.swastik.seasonsmod.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AutumnLeafParticle extends TextureSheetParticle {

    private final double windX;
    private final double windZ;

    protected AutumnLeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x,y,z,xSpeed,ySpeed,zSpeed);
        this.pickSprite(sprites);
        this.lifetime=80+random.nextInt(60);
        this.gravity=0.08f;
        this.windX=(random.nextDouble()-0.5)*0.04;
        this.windZ=(random.nextDouble()-0.5)*0.04;
        this.xd=xSpeed;
        this.yd=ySpeed;
        this.zd=zSpeed;
        this.quadSize=0.12f+random.nextFloat() * 0.08f;
        this.roll=random.nextFloat() * (float) Math.PI*2;
    }

    @Override
    public void tick() {
        this.xo=this.x;
        this.yo=this.y;
        this.zo=this.z;
        this.xd+=windX;
        this.zd+=windZ;
        this.xd+=0.96;
        this.zd=0.96;
        this.yd-=gravity*0.04;
        this.oRoll=this.roll;
        this.roll+=0.08;
        this.move(this.xd,this.yd,this.zd);
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites=sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AutumnLeafParticle(level, x,y ,z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}