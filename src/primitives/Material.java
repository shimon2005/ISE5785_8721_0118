package primitives;

public class Material {
    public Double3 ka = Double3.ONE;

    public Material setKa(Double3 ka) {
        this.ka = ka;
        return this;
    }

    public Material setKa(double ka) {
        this.ka = new Double3(ka);
        return this;
    }


}
