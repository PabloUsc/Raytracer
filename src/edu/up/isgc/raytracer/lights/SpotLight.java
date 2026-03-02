package edu.up.isgc.raytracer.lights;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class SpotLight extends Light {
    private Vector3D direction;
    private float cutoffAngle; //Radians 0-2

    public SpotLight(Vector3D position, Vector3D direction, Color color, double intensity, float cutoffAngle) {
        super(position,color,intensity);
        setDirection(direction);
        setCutoffAngle(cutoffAngle);
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = Vector3D.normalize(direction);
    }

    public float getCutoffAngle() {
        return cutoffAngle;
    }

    public void setCutoffAngle(float cutoffAngle) {
        this.cutoffAngle = cutoffAngle;
    }

    @Override
    public float getNDotL(Intersection intersection) {
        Vector3D LminI = Vector3D.normalize(Vector3D.substract(this.getPosition(), intersection.getPosition()));
        float theta = (float) Vector3D.dotProduct(LminI,direction);

        if (theta > Math.cos(cutoffAngle)) {
            Vector3D normal = intersection.getNormal();
            Vector3D invLightDir = Vector3D.scalarMultiplication(direction,-1);
            return (float) Math.max(0.0f, Vector3D.dotProduct(normal,invLightDir));
//            return (float) Math.pow(theta, 2.0);
        }
        return 0.0f;

    }
}
