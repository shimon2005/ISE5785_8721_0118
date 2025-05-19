package primitives;

/**
 * The Material class represents the material properties of a 3D object.
 * It defines the coefficients for ambient, specular, and diffuse reflection,
 * as well as the shininess factor.
 */
public class Material {

    /**
     * Define and initialize the material fields
     */
    public Double3 kA = Double3.ONE;
    public Double3 kS = Double3.ZERO;
    public Double3 kD = Double3.ZERO;
    public Double3 kT = Double3.ZERO;
    public Double3 kR = Double3.ZERO;
    public int nSh = 0;

    /**
     * Constructs a Material object with default values.
     * Ambient reflection coefficient (kA) is set to 1,
     * specular reflection coefficient (kS) is set to 0,
     * diffuse reflection coefficient (kD) is set to 0,
     * and shininess factor (nSh) is set to 0.
     */
    public Material setKa(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient for the material.
     *
     * @param kA the ambient reflection coefficient
     * @return the Material object itself
     */
    public Material setKa(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Sets the specular reflection coefficient for the material.
     *
     * @param kS the specular reflection coefficient
     * @return the Material object itself
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient for the material.
     *
     * @param kS the specular reflection coefficient
     * @return the Material object itself
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient for the material.
     *
     * @param kD the diffuse reflection coefficient
     * @return the Material object itself
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient for the material.
     *
     * @param kD the diffuse reflection coefficient
     * @return the Material object itself
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the transmission coefficient for the material.
     *
     * @param kT the transmission coefficient
     * @return the Material object itself
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transmission coefficient for the material.
     *
     * @param kT the transmission coefficient
     * @return the Material object itself
     */
    public Material setKT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection coefficient for the material.
     *
     * @param kR the reflection coefficient
     * @return the Material object itself
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection coefficient for the material.
     *
     * @param kR the reflection coefficient
     * @return the Material object itself
     */
    public Material setKR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }


    /**
     * Sets the shininess factor for the material.
     *
     * @param nSh the shininess factor
     * @return the Material object itself
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }

}
