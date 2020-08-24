package tr.com.havelsan.geohash;

public class Neighbour {
    private GeohashCoords north;
    private GeohashCoords south;
    private GeohashCoords east;
    private GeohashCoords west;

    public Neighbour(GeohashCoords north, GeohashCoords south, GeohashCoords east, GeohashCoords west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    public GeohashCoords getNorth() {
        return north;
    }

    public void setNorth(GeohashCoords north) {
        this.north = north;
    }

    public GeohashCoords getSouth() {
        return south;
    }

    public void setSouth(GeohashCoords south) {
        this.south = south;
    }

    public GeohashCoords getEast() {
        return east;
    }

    public void setEast(GeohashCoords east) {
        this.east = east;
    }

    public GeohashCoords getWest() {
        return west;
    }

    public void setWest(GeohashCoords west) {
        this.west = west;
    }

    public GeohashCoords getCoordFromDirection(String direction) {
        switch (direction) {
            case "n":
                return getNorth();

            case "s":
                return getSouth();

            case "e":
                return getEast();

            case "w":
                return getWest();

            default:
                return getNorth();
        }
    }
}
