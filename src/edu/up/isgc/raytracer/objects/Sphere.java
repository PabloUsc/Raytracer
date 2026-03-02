/**
 * [1968] - [2021] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package edu.up.isgc.raytracer.objects;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class Sphere extends Object3D {

    private float radius;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Sphere(Vector3D position, float radius, Color color, Material material) {
        super(position, color, material);
        setRadius(radius);
    }

//    @Override
//    public Intersection getIntersection(Ray ray) {
//        double distance = -1;
//        Vector3D normal = Vector3D.ZERO();
//        Vector3D position = Vector3D.ZERO();
//
//        Vector3D directionSphereRay = Vector3D.substract(ray.getOrigin(), getPosition());
//        double firstP = Vector3D.dotProduct(ray.getDirection(), directionSphereRay);
//        double secondP = Math.pow(Vector3D.magnitude(directionSphereRay), 2);
//        double intersection = Math.pow(firstP, 2) - secondP + Math.pow(getRadius(), 2);
//
//        if(intersection >= 0){
//            double sqrtIntersection = Math.sqrt(intersection);
//            double part1 = -firstP + sqrtIntersection;
//            double part2 = -firstP - sqrtIntersection;
//
//            distance = Math.min(part1, part2);
//            position = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
//            normal = Vector3D.normalize(Vector3D.substract(position, getPosition()));
//        } else {
//            return null;
//        }
//
//        return new Intersection(position, distance, normal, this);
//    }


    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D L = Vector3D.substract(ray.getOrigin(), getPosition());

        double a = Vector3D.dotProduct(ray.getDirection(), ray.getDirection());
        double b = 2.0 * Vector3D.dotProduct(ray.getDirection(), L);
        double c = Vector3D.dotProduct(L, L) - getRadius() * getRadius();

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null;
        }

        double sqrtD = Math.sqrt(discriminant);

        double t1 = (-b - sqrtD) / (2.0 * a);
        double t2 = (-b + sqrtD) / (2.0 * a);

        double t = Double.MAX_VALUE;

        if (t1 > 1e-6) t = t1;
        if (t2 > 1e-6 && t2 < t) t = t2;

        if (t == Double.MAX_VALUE) return null;

        Vector3D position = Vector3D.add(
                ray.getOrigin(),
                Vector3D.scalarMultiplication(ray.getDirection(), t)
        );

        Vector3D normal = Vector3D.normalize(
                Vector3D.substract(position, getPosition())
        );

        return new Intersection(position, t, normal, this);
    }
}
