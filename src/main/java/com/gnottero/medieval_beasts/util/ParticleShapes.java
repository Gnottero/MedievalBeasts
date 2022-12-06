package com.gnottero.medieval_beasts.util;

import net.minecraft.particles.IParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ParticleShapes {

    public static void particleCircle(IParticleData particleType, ServerWorld world, double posX, double posY, double posZ, double offSetX, double offSetY, double offSetZ, float radius, int samples, int count) {
        for (int i = 0; i < samples; ++i) {
            world.spawnParticle(particleType, posX + offSetX + Math.sin(i) * radius, posY + offSetY, posZ + offSetZ + Math.cos(i) * radius, count, 0.0d, 0.0d, 0.0d, 0.0d);
        }
    }

    public static void fibonacciSphere(IParticleData particleType, ServerWorld world, BlockPos pos, float offSetX, float offSetY, float offSetZ, float enlargement, int samples, int count) {
        double phi = Math.PI * (3.0 - Math.sqrt(5.0));

        for (int i = 0; i < samples; ++i) {
            float y = 1 - (i / (float)(samples - 1)) * 2;
            double radius = Math.sqrt(1 - y * y);
            double theta = phi * i;
            double z = Math.sin(theta) * radius;
            double x = Math.cos(theta) * radius;

            world.spawnParticle(particleType, pos.getX() + offSetX + x * enlargement, pos.getY() + offSetY + y * enlargement, pos.getZ() + offSetZ + z * enlargement, count, 0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public static void particleMagicCircle(IParticleData particleType, ServerWorld world, double posX, double posY, double posZ, double offSetX, double offSetY, double offSetZ, float radius, float density) {

        // Triangles
        drawParticlePolygon(particleType, world, posX, posY, posZ, offSetX, offSetY, radius + 0.5, 1.5f, 3, density, (float) (Math.PI));
        drawParticlePolygon(particleType, world, posX, posY, posZ, offSetX, offSetY, - radius + 0.5, 1.5f, 3, density, 0.0f);
        drawParticlePolygon(particleType, world, posX, posY, posZ, radius + 0.5, offSetY, offSetZ, 1.5f, 3, density, (float) (3 * Math.PI/2));
        drawParticlePolygon(particleType, world, posX, posY, posZ, - radius + 0.5, offSetY, offSetZ, 1.5f, 3, density, (float) (Math.PI/2));

        // Circles
        particleCircle(particleType, world, posX, posY, posZ, offSetX, offSetY, radius + 0.5, 0.5f, 10, 1);
        particleCircle(particleType, world, posX, posY, posZ, offSetX, offSetY, - radius + 0.5, 0.5f, 10, 1);
        particleCircle(particleType, world, posX, posY, posZ, radius + 0.5, offSetY, offSetZ, 0.5f, 10, 1);
        particleCircle(particleType, world, posX, posY, posZ, - radius + 0.5, offSetY, offSetZ, 0.5f, 10, 1);

        particleCircle(particleType, world, posX, posY, posZ, 0.5f - 2.5f, offSetY, 0.5f - 2.5f, 1.0f, 20, 1);
        particleCircle(particleType, world, posX, posY, posZ, 0.5f - 2.5f, offSetY, 0.5f + 2.5f, 1.0f, 20, 1);
        particleCircle(particleType, world, posX, posY, posZ, 0.5f + 2.5f, offSetY, 0.5f + 2.5f, 1.0f, 20, 1);
        particleCircle(particleType, world, posX, posY, posZ, 0.5f + 2.5f, offSetY, 0.5f - 2.5f, 1.0f, 20, 1);

        // Big Octagon
        drawParticlePolygon(particleType, world, posX, posY, posZ, offSetX, offSetY, offSetZ, 5.0f, 8, 20.0f, (float) (Math.PI/40 * world.getGameTime() * 0.05f));
        drawParticlePolygon(particleType, world, posX, posY, posZ, offSetX, offSetY, offSetZ, 1.5f, 4, 20.0f, (float) (Math.PI/2));
    }

    public static void drawParticlePolygon(IParticleData particleType, ServerWorld world, double posX, double posY, double posZ, double offSetX, double offSetY, double offSetZ, float radius, int nVertices, float density, float rotation) {
        List<Vector3d> vertices = polygonVertices(posX, posY, posZ, offSetX, offSetY, offSetZ, radius, nVertices, rotation);
        List<Vector3d> rotatedVertices = vertices.stream()
                .map(v -> v.subtract(new Vector3d(posX + offSetX, posY + offSetY, posZ + offSetZ)))
                .map(v -> v.rotateYaw(rotation))
                .map(v -> v.add(new Vector3d(posX + offSetX, posY + offSetY, posZ + offSetZ)))
                .collect(Collectors.toList());

        for (int i = 0; i < vertices.size(); i++) {
            drawParticleLine(particleType, world, rotatedVertices.get(i), rotatedVertices.get((i+1) % rotatedVertices.size()), density);
        }
    }

    public static List<Vector3d> polygonVertices(double posX, double posY, double posZ, double offSetX, double offSetY, double offSetZ, float radius, int nVertices, float rotation) {
        List<Vector3d> vertices = new ArrayList<>();
        double step = ((Math.PI * 2) / nVertices);
        double x, z, current = 0;
        for (int i = 0; i < nVertices; i++) {
            x = Math.sin(current) * radius;
            z = Math.cos(current) * radius;
            current += step;
            vertices.add(new Vector3d(posX + offSetX + x, posY + offSetY, posZ + offSetZ + z));
        }
        return vertices;
    }

    public static void drawOptimizedParticleLine(IParticleData particleType, ServerWorld world, Vector3d fromPos, Vector3d toPos, double density) {
        double distance = fromPos.distanceTo(toPos);
        int particles = (int) (distance/density);
        Vector3d towards = toPos.subtract(fromPos);
        world.spawnParticle(
                particleType,
                (towards.x)/2 + fromPos.x, (towards.y)/2 + fromPos.y, (towards.z)/2 + fromPos.z,
                particles/3,
                towards.x/6, towards.y/6, towards.z/6, 0.0d
        );
        int divider = 6;
        while (particles/divider > 1)
        {
            int center = (divider*2)/3;
            int dev = 2*divider;
            world.spawnParticle(
                    particleType,
                    (towards.x)/center + fromPos.x, (towards.y)/center + fromPos.y, (towards.z)/center + fromPos.z,
                    particles/divider,
                    towards.x/dev, towards.y/dev, towards.z/dev, 0.0
            );
            world.spawnParticle(
                    particleType,
                    (towards.x) * (1.0-1.0/center) + fromPos.x, (towards.y) * (1.0-1.0/center) + fromPos.y, (towards.z) * (1.0-1.0/center) + fromPos.z,
                    particles/divider,
                    towards.x/dev, towards.y/dev, towards.z/dev, 0.0
            );
            divider = 2 * divider;
        }
    }

    private static boolean isStraight(Vector3d from, Vector3d to, double density) {
        if ((from.x == to.x && from.y == to.y) || (from.x == to.x && from.z == to.z) || (from.y == to.y && from.z == to.z))
            return from.distanceTo(to) / density > 20;
        return false;
    }

    public static void drawParticleLine(IParticleData particleType, ServerWorld world, Vector3d fromPos, Vector3d toPos, double density) {
        double distance = fromPos.distanceTo(toPos);
        if (distance == 0) return;
        if (distance < 100) {
            Random rand = world.getRandom();
            int particles = (int)(distance/density)+1;
            Vector3d towards = toPos.subtract(fromPos);
            for (int i = 0; i < particles; i++) {
                Vector3d at = fromPos.add(towards.scale(rand.nextDouble()));
                world.spawnParticle(particleType,
                        at.x, at.y, at.z, 1,
                        0.0, 0.0, 0.0, 0.0
                );
            }
        }

        if (isStraight(fromPos, toPos, density)) {
            drawOptimizedParticleLine(particleType, world, fromPos, toPos, density);
            return;
        }
        Vector3d incvec = toPos.subtract(fromPos).scale(2*density/Math.sqrt(distance));

        for (Vector3d delta = new Vector3d(0.0d,0.0d,0.0d);
             delta.lengthSquared() < distance;
             delta = delta.add(incvec.scale(world.getRandom().nextFloat()))) {
            world.spawnParticle(particleType,
                    delta.x + fromPos.x, delta.y + fromPos.y, delta.z + fromPos.z, 1,
                    0.0, 0.0, 0.0, 0.0);
        }
    }
}
