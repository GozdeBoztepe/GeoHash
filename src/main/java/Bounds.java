public class Bounds {
    private Coordinates sw;
    private Coordinates ne;

    public Bounds(Coordinates sw, Coordinates ne) {
        this.sw = new Coordinates(sw.getLat(), sw.getLon());
        this.ne = new Coordinates(ne.getLat(), ne.getLon());
    }

    public Coordinates getSw() {
        return sw;
    }

    public void setSw(Coordinates sw) {
        this.sw = sw;
    }

    public Coordinates getNe() {
        return ne;
    }

    public void setNe(Coordinates ne) {
        this.ne = ne;
    }
}
