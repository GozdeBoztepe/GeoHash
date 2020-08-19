public class GeohashCoords {
    private String lat;
    private String lon;

    public GeohashCoords(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCoordWithIndex(int type) {
        if (type == 0) {
            return getLat();
        } else {
            return getLon();
        }
    }
}
