/**
 * [1968] - [2021] Centros Culturales de Mexico A.C / Universidad Panamericana
 * All Rights Reserved.
 */
package edu.up.isgc.raytracer;

import edu.up.isgc.raytracer.lights.DirectionalLight;
import edu.up.isgc.raytracer.lights.Light;
import edu.up.isgc.raytracer.lights.PointLight;
import edu.up.isgc.raytracer.lights.SpotLight;
import edu.up.isgc.raytracer.objects.*;
import edu.up.isgc.raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @authors Arturo Jafet & Pablo Uscanga
 */
public class Raytracer {

    public static final int LIMIT_DEPTH = 6;

    public static void main(String[] args) {
        System.out.println(new Date());
        Scene scene = new Scene();
//        scene.setCamera(new Camera(new Vector3D(0, 0, -9), 160, 160, 800, 800, 8.2f, 50f));
        //scene01.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(0.0, 0.0, 1.0), Color.WHITE, 0.8));
        scene.setCamera(new Camera(new Vector3D(0, 0.2f, -9), 160, 160, 800, 800, 4.2f, 50f));
        // LIGHTS
        scene.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(0.0, -0.1, 0.1), Color.WHITE, 2));
        scene.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(-0.2, -0.1, 0.0), Color.WHITE, 2));
        scene.addLight(new PointLight(new Vector3D(0.0,1.0,0.0),Color.WHITE, 0.9));
        //OBJECTS
        Material table = new Material(0.1f,0,0);
        Material teapot = new Material(0.1f,0.1f,0);
        scene.addObject(OBJReader.GetPolygon("Table.obj", new Vector3D(0f, -4f, 0f), new Color(150,75,0),table,0.1f));
        scene.addObject(OBJReader.GetPolygon("SmallTeapot.obj", new Vector3D(2f, -1.0f, 1.5f), new Color(225,193,110),teapot,1));

        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 1.1, -8), 160, 160, 1280, 720, 4.2f, 50f));
        //OBJECTS
        Material reflectDeco = new Material(0.5f,0.8f,0);
        Material bird = new Material(.5f, 0, 0);
        Material plane = new Material(0,0,0);
        scene02.addObject(OBJReader.GetPolygon("ReflectDeco.obj",new Vector3D(-2,0,1.5),Color.RED,reflectDeco,1));
        scene02.addObject(OBJReader.GetPolygon("ReflectDeco.obj",new Vector3D(2,0,1.5),Color.blue,reflectDeco,1));
        scene02.addObject(OBJReader.GetPolygon("Bird.obj",new Vector3D(0.25,0,0.5),new Color(255,215,0),bird,1));
        scene02.addObject(OBJReader.GetPolygon("BigPlane.obj",new Vector3D(0,-0.2,0),Color.WHITE,plane,1));
        scene02.addObject(OBJReader.GetPolygon("RoomSize6.obj",new Vector3D(0,2,5),Color.LIGHT_GRAY,plane,1.3f));
        //LIGHTS
         scene02.addLight(new PointLight(new Vector3D(0,3,-4),Color.WHITE,0.75f));

        Scene scene03 = new Scene();
        scene03.setCamera(new Camera(new Vector3D(0,1.1,-9),160,160,1200,1200,-4f,100f));
        //OBJECTS
        Material plane3 = new Material(0.1f,0,0);
        Material sphereLamb = new Material(0.4f,0,0);
        Material sphereRefract = new Material(0.2f,0,0.5f);
        Material sphereReflect = new Material(0.2f,0.6f,0);
        scene03.addObject(OBJReader.GetPolygon("BigPlane.obj",new Vector3D(0,-0.25,2),Color.MAGENTA,plane3,2));
        scene03.addObject(OBJReader.GetPolygon("RoomSize6.obj",new Vector3D(9,0.5,8),Color.WHITE,plane3,2));
        scene03.addObject(new Sphere(new Vector3D(0.1f,1.6,3),1.6f,Color.GREEN,sphereLamb));
        scene03.addObject(new Sphere(new Vector3D(2f,1,1),1f,Color.RED,sphereLamb));
        scene03.addObject(new Sphere(new Vector3D(-1.6f,1,2.8),1f,Color.BLUE,sphereLamb));
        scene03.addObject(new Sphere(new Vector3D(-0.5f,.8,0.2),.6f,Color.WHITE,sphereRefract));
        scene03.addObject(new Sphere(new Vector3D(2.8f,.8,-0.5),.65f,Color.BLACK,sphereReflect));
        //LIGHTS
        scene03.addLight(new PointLight(new Vector3D(0,4,0),Color.WHITE,2f));

        BufferedImage image = raytrace(scene);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException ioe) {
            System.out.println("Something failed");
        }
        BufferedImage image2 = raytrace(scene02);
        File outputImage2 = new File("image2.png");
        try {
            ImageIO.write(image2, "png", outputImage2);
        } catch (IOException ioe) {
            System.out.println("Something failed");
        }
        BufferedImage image3 = raytrace(scene03);
        File outputImage3 = new File("image3.png");
        try {
            ImageIO.write(image3, "png", outputImage3);
        } catch (IOException ioe) {
            System.out.println("Something failed");
        }
        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        ArrayList<Light> lights = scene.getLights();
        float[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        ArrayList<Object3D> objects = scene.getObjects();

        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Color pixelColor = render(ray, scene, 0);
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    public static Color render(Ray ray, Scene scene, int depth) {
        if (depth >= LIMIT_DEPTH) return new Color(128,128,128);//Color.BLACK;

        float[] nearFarPlanes = scene.getCamera().getNearFarPlanes();
        float cameraZ = (float) scene.getCamera().getPosition().getZ();

        float[] currentClipping = (depth == 0) ? new float[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]} : null;

        Intersection closestIntersection = raycast(ray, scene.getObjects(), null, currentClipping);

        if (closestIntersection == null) return Color.BLACK;

        //Background color
        Color pixelColor = Color.BLACK;

        for (Light light : scene.getLights()) {
            float nDotL = light.getNDotL(closestIntersection);
            float intensity = (float) light.getIntensity() * nDotL;
            Color lightColor = light.getColor();
            Color objColor = closestIntersection.getObject().getColor();
            float[] lightColors = new float[]{lightColor.getRed() / 255.0f, lightColor.getGreen() / 255.0f, lightColor.getBlue() / 255.0f};
            float[] objColors = new float[]{objColor.getRed() / 255.0f, objColor.getGreen() / 255.0f, objColor.getBlue() / 255.0f};
            for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                objColors[colorIndex] *= intensity * lightColors[colorIndex];
            }
            Color diffuse = new Color(clamp(objColors[0], 0, 1),clamp(objColors[1], 0, 1),clamp(objColors[2], 0, 1));
            pixelColor = addColor(pixelColor, diffuse);

            Vector3D L = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
            Vector3D N = Vector3D.normalize(closestIntersection.getNormal());
            Vector3D V = Vector3D.normalize(Vector3D.substract(scene.getCamera().getPosition(), closestIntersection.getPosition()));

            double dotNL = Vector3D.dotProduct(N, L);
            Vector3D R = Vector3D.substract(Vector3D.scalarMultiplication(N, 2 * dotNL), L);
            double spec = Math.pow(Math.max(0, Vector3D.dotProduct(R, V)), 32); // 32 is the "shininess"

            float specularIntensity = (float) (light.getIntensity() * spec * closestIntersection.getObject().getMaterial().getShininess());
            Color specular = new Color(clamp(specularIntensity, 0, 1), clamp(specularIntensity, 0, 1), clamp(specularIntensity, 0, 1));

            pixelColor = addColor(pixelColor, specular);
        }

        if (closestIntersection.getObject().getMaterial().getReflection() > 0) {
            Color reflectColor = reflect(closestIntersection,ray,scene,depth);
            float objReflectivity = closestIntersection.getObject().getMaterial().getReflection();
            pixelColor = lerpColor(pixelColor, reflectColor, objReflectivity);
        }

        if (closestIntersection.getObject().getMaterial().getRefraction() > 0) {
            Color refractColor = refract(closestIntersection,ray,scene);
            float objTransparency = closestIntersection.getObject().getMaterial().getRefraction();
            pixelColor = lerpColor(pixelColor, refractColor, objTransparency);
        }

        return pixelColor;
    }

    public static Color reflect(Intersection closestIntersection, Ray ray, Scene scene, int depth) {
        if (depth >= LIMIT_DEPTH || closestIntersection.getObject().getMaterial().getReflection() <= 0) {
            return Color.BLACK;
        }

        Vector3D I = Vector3D.normalize(ray.getDirection());
        Vector3D N = Vector3D.normalize(closestIntersection.getNormal());

        // Formula: R = I - 2 * (I . N) * N
        double dot = Vector3D.dotProduct(I,N);
        Vector3D reflectDir = Vector3D.substract(I, Vector3D.scalarMultiplication(N, 2 * dot));

        Vector3D shadowOffsetText = Vector3D.scalarMultiplication(N,0.001);
        Ray reflectedRay = new Ray(Vector3D.add(closestIntersection.getPosition(),shadowOffsetText), reflectDir);

        return render(reflectedRay, scene, depth + 1);
    }

    public static Color refract(Intersection closestIntersection, Ray ray, Scene scene) {
        Vector3D I = Vector3D.normalize(ray.getDirection());
        Vector3D N = Vector3D.normalize(closestIntersection.getNormal());

        double iorAir = 1.0;
        double iorObj = 1.5;
        double etai = iorAir;
        double etat = iorObj;

        double cosTheta = Vector3D.dotProduct(I,N);
        cosTheta = Math.max(-1.0, Math.min(1.0,cosTheta));
        boolean entering = cosTheta < 0; //Check for entering object

        if (entering) {
            cosTheta = -cosTheta;
        } else {
            double temp = etai;
            etai = etat;
            etat = temp;
            N = Vector3D.scalarMultiplication(N,-1);
        }

        double eta = etai / etat;

        double k = 1.0 - eta * eta * (1.0 - cosTheta * cosTheta);

        if (k < 0) {
//            return Color.BLACK;
            return new Color(173, 216, 230);
        }

        Vector3D refractDir = Vector3D.normalize(Vector3D.add(Vector3D.scalarMultiplication(I,eta),Vector3D.scalarMultiplication(N,eta * cosTheta - Math.sqrt(k))));

        // Offset the origin slightly to avoid acne
        Vector3D offsetOrigin = Vector3D.scalarMultiplication(N,0.001);
        Vector3D origin = entering ? Vector3D.substract(closestIntersection.getPosition(),offsetOrigin) : Vector3D.add(closestIntersection.getPosition(),offsetOrigin);
        Ray refractedRay = new Ray(origin, refractDir);

        Intersection nextHit = raycast(refractedRay, scene.getObjects(),closestIntersection.getObject(), null);

        if (nextHit != null) {
            Color hitColour = Color.BLACK;

            for (Light light : scene.getLights()) {
                float nDotL = light.getNDotL(nextHit);
                Color diffuse = getColor(light, nDotL, nextHit);
                hitColour = addColor(hitColour, diffuse);
            }
            return hitColour;
        }
        return new Color(173, 216, 230);
    }

    private static Color getColor(Light light, float nDotL, Intersection nextHit) {
        float intensity = (float) light.getIntensity() * nDotL;

        float[] lC = new float[]{light.getColor().getRed() / 255.0f, light.getColor().getGreen() / 255.0f, light.getColor().getBlue() / 255.0f};
        float[] oC = new float[]{nextHit.getObject().getColor().getRed() / 255.0f, nextHit.getObject().getColor().getGreen() / 255.0f, nextHit.getObject().getColor().getBlue() / 255.0f};

        return new Color(
                clamp(oC[0] * intensity * lC[0], 0, 1),
                clamp(oC[1] * intensity * lC[1], 0, 1),
                clamp(oC[2] * intensity * lC[2], 0, 1)
        );
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static Color addColor(Color original, Color otherColor){
        float red = clamp((original.getRed() / 255.0f) + (otherColor.getRed() / 255.0f), 0, 1);
        float green = clamp((original.getGreen() / 255.0f) + (otherColor.getGreen() / 255.0f), 0, 1);
        float blue = clamp((original.getBlue() / 255.0f) + (otherColor.getBlue() / 255.0f), 0, 1);
        return new Color(red, green, blue);
    }

    public static Color lerpColor(Color c1, Color c2, float amount) {
        float r = (c1.getRed() / 255f) * (1 - amount) + (c2.getRed() / 255f) * amount;
        float g = (c1.getGreen() / 255f) * (1 - amount) + (c2.getGreen() / 255f) * amount;
        float b = (c1.getBlue() / 255f) * (1 - amount) + (c2.getBlue() / 255f) * amount;
        return new Color(clamp(r, 0, 1), clamp(g, 0, 1), clamp(b, 0, 1));
    }

    public static Intersection raycast(Ray ray, ArrayList<Object3D> objects, Object3D caster, float[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int k = 0; k < objects.size(); k++) {
            Object3D currentObj = objects.get(k);
            if (caster == null || !currentObj.equals(caster)) {
                Intersection intersection = currentObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersection.getPosition().getZ() >= clippingPlanes[0] &&
                                    intersection.getPosition().getZ() <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
}
