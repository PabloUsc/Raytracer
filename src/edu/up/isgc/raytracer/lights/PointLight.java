package edu.up.isgc.raytracer.lights;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class PointLight extends Light{

    public PointLight(Vector3D position, Color color, double intensity){
        super(position, color, intensity);
    }

    @Override
    public float getNDotL(Intersection intersection) {
        Vector3D LminI = Vector3D.normalize(Vector3D.substract(this.getPosition(), intersection.getPosition()));
        return (float)Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(LminI, 1.0)), 0.0);
    }
}
