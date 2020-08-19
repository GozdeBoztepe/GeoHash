import java.math.BigDecimal;

public class GeoHash {
    static final String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";


    public static String encode(double lat, double lon, int precision) {

        // infer precision?

        if (precision == 0) {

            // refine geohash until it matches precision of supplied lat/lon

            for (int p = 1; p <= 12; p++) {

                String hash = GeoHash.encode(lat, lon, p);

                Coordinates posn = GeoHash.decode(hash);

                if (posn.getLat() == lat && posn.getLon() == lon) return hash;

            }

            precision = 12; // set to maximum


        }


        int idx = 0; // index into base32 map

        int bit = 0; // each char holds 5 bits

        boolean evenBit = true;

        String geohash = "";


        double latMin = -90, latMax = 90;

        double lonMin = -180, lonMax = 180;


        while (geohash.length() < precision) {

            if (evenBit) {

                // bisect E-W longitude

                double lonMid = (lonMin + lonMax) / 2;

                if (lon >= lonMid) {

                    idx = idx * 2 + 1;

                    lonMin = lonMid;

                } else {

                    idx = idx * 2;

                    lonMax = lonMid;

                }

            } else {

                // bisect N-S latitude

                double latMid = (latMin + latMax) / 2;

                if (lat >= latMid) {

                    idx = idx * 2 + 1;

                    latMin = latMid;

                } else {

                    idx = idx * 2;

                    latMax = latMid;

                }

            }

            evenBit = !evenBit;


            if (++bit == 5) {

                // 5 bits gives us a character: append it and start over

                geohash += base32.charAt(idx);

                bit = 0;

                idx = 0;

            }

        }


        return geohash;

    }

    public static Coordinates decode(String geohash) {

        Bounds bounds = GeoHash.bounds(geohash); // <-- the hard work

        // now just determine the centre of the cell...


        double latMin = bounds.getSw().getLat();
        double lonMin = bounds.getSw().getLon();

        double latMax = bounds.getNe().getLat();
        double lonMax = bounds.getNe().getLon();


        // cell centre

        double lat = (latMin + latMax) / 2;

        double lon = (lonMin + lonMax) / 2;


        // round to close to centre without excessive precision: ⌊2-log10(Δ°)⌋ decimal places

        BigDecimal latDec = new BigDecimal(lat).setScale((int) Math.floor(2 - Math.log10(latMax - latMin)), BigDecimal.ROUND_HALF_UP);
        lat = latDec.doubleValue();

        BigDecimal lonDec = new BigDecimal(lon).setScale((int) Math.floor(2 - Math.log10(lonMax - lonMin)), BigDecimal.ROUND_HALF_UP);
        lon = lonDec.doubleValue();

        return new Coordinates(lat, lon);

    }

    public static Bounds bounds(String geohash) {


        if (geohash.length() == 0) throw new Error("Invalid geohash");

        geohash = geohash.toLowerCase();

        boolean evenBit = true;

        double latMin = -90, latMax = 90;

        double lonMin = -180, lonMax = 180;


        for (int i = 0; i < geohash.length(); i++) {

            char chr = geohash.charAt(i);

            int idx = base32.indexOf(chr);

            if (idx == -1) throw new Error("Invalid geohash");


            for (int n = 4; n >= 0; n--) {

                int bitN = idx >> n & 1;

                if (evenBit) {

                    // longitude

                    double lonMid = (lonMin + lonMax) / 2;

                    if (bitN == 1) {

                        lonMin = lonMid;

                    } else {

                        lonMax = lonMid;

                    }

                } else {

                    // latitude

                    double latMid = (latMin + latMax) / 2;

                    if (bitN == 1) {

                        latMin = latMid;

                    } else {

                        latMax = latMid;

                    }

                }

                evenBit = !evenBit;

            }

        }

        return new Bounds(new Coordinates(latMin, lonMin), new Coordinates(latMax, lonMax));

    }

    public static String adjacent(String geohash, String direction) {

        // based on github.com/davetroy/geohash-js


        geohash = geohash.toLowerCase();

        direction = direction.toLowerCase();


        if (geohash.length() == 0) throw new Error("Invalid geohash");

        if ("nsew".indexOf(direction) == -1) throw new Error("Invalid direction");


        GeohashCoords nNorth = new GeohashCoords("p0r21436x8zb9dcf5h7kjnmqesgutwvy", "bc01fg45238967deuvhjyznpkmstqrwx");
        GeohashCoords nSouth = new GeohashCoords("14365h7k9dcfesgujnmqp0r2twvyx8zb", "238967debc01fg45kmstqrwxuvhjyznp");
        GeohashCoords nEast = new GeohashCoords("bc01fg45238967deuvhjyznpkmstqrwx", "p0r21436x8zb9dcf5h7kjnmqesgutwvy");
        GeohashCoords nWest = new GeohashCoords("238967debc01fg45kmstqrwxuvhjyznp", "14365h7k9dcfesgujnmqp0r2twvyx8zb");
        Neighbour neighbour = new Neighbour(nNorth, nSouth, nEast, nWest);


        GeohashCoords bNorth = new GeohashCoords("prxz", "bcfguvyz");
        GeohashCoords bSouth = new GeohashCoords("028b", "0145hjnp");
        GeohashCoords bEast = new GeohashCoords("bcfguvyz", "prxz");
        GeohashCoords bWest = new GeohashCoords("0145hjnp", "028b");
        Neighbour border = new Neighbour(bNorth, bSouth, bEast, bWest);


        String lastCh = geohash.substring(geohash.length() - 1);    // last character of hash

        String parent = geohash.substring(0, geohash.length() - 1); // hash without last character


        int type = geohash.length() % 2;


        // check for edge-cases which don't share common prefix

        if (border.getCoordFromDirection(direction).getCoordWithIndex(type).indexOf(lastCh) != -1 && parent != "") {

            parent = GeoHash.adjacent(parent, direction);

        }

        // append letter for direction to parent


        return parent + base32.charAt(neighbour.getCoordFromDirection(direction).getCoordWithIndex(type).indexOf(lastCh));

    }

    public static String neighbours(String geohash, String direction) {
        direction = direction.toLowerCase();
        switch (direction) {
            case "n":
                return GeoHash.adjacent(geohash, "n");
            case "ne":
                return GeoHash.adjacent(GeoHash.adjacent(geohash, "n"), "e");
            case "e":
                return GeoHash.adjacent(geohash, "e");
            case "se":
                return GeoHash.adjacent(GeoHash.adjacent(geohash, "s"), "e");
            case "s":
                return GeoHash.adjacent(geohash, "s");
            case "sw":
                return GeoHash.adjacent(GeoHash.adjacent(geohash, "s"), "w");
            case "w":
                return GeoHash.adjacent(geohash, "w");
            case "nw":
                return GeoHash.adjacent(GeoHash.adjacent(geohash, "n"), "w");
            default:
                return GeoHash.adjacent(geohash, "n");
        }
    }

}
