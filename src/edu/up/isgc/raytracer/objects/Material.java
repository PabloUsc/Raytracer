package edu.up.isgc.raytracer.objects;

public class Material {
    private float shininess;
    private float reflection;
    private float refraction;

    public Material(float shininess, float reflection, float refraction) {
        setReflection(reflection);
        setRefraction(refraction);
        setShininess(shininess);
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = Math.max(0, Math.min(1f,shininess));
    }

    public float getReflection() {
        return reflection;
    }

    public void setReflection(float reflection) {
        this.reflection = Math.max(0, Math.min(1f,reflection));
    }

    public float getRefraction() {
        return refraction;
    }

    public void setRefraction(float refraction) {
        this.refraction = Math.max(0, Math.min(1f,refraction));
    }
}
