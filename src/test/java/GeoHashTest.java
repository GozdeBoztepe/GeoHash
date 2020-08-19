import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.Test;


public class GeoHashTest {

    @Test
    @Owner("gboztepe")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("GEOHASH-1")
    @Story("Verilen lat lon ve precision degerlerinden geohash olusturulmalidir.")
    @Description("Test encode function")
    public void encode() {
        String encoded = GeoHash.encode(52.205, 0.119, 7);
        Assert.assertEquals(encoded,"u120fxw");

    }

    @Test
    @Owner("gboztepe")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("GEOHASH-2")
    @Story("Verilen geohash degerinden lat ve lon degerleri elde edilmelidir.")
    @Description("Test decode function")
    public void decode() {
        Coordinates coordinates = GeoHash.decode("u120fxw");
        Coordinates coordinatesActual = new Coordinates(52.205,0.119);
        Assert.assertEquals(coordinates.getLat(), coordinatesActual.getLat(), 0.001);
        Assert.assertEquals(coordinates.getLon(), coordinatesActual.getLon(),0.001);
    }

    @Test
    @Owner("gboztepe")
    @Severity(SeverityLevel.NORMAL)
    @Issue("GEOHASH-3")
    @Story("Verilen geohash degerinden bounding box lat ve lon degerleri elde edilmelidir.")
    @Description("Test bounds function")
    public void bounds() {
        Bounds bounds = GeoHash.bounds("u120fxw");
        Assert.assertEquals(52.205, bounds.getNe().getLat(), 0.001);
        Assert.assertEquals(0.119, bounds.getNe().getLon(), 0.001);

        Assert.assertEquals(52.204, bounds.getSw().getLat(), 0.001);
        Assert.assertEquals(0.118, bounds.getSw().getLon(), 0.001);

    }

    @Test
    @Owner("gboztepe")
    @Severity(SeverityLevel.NORMAL)
    @Issue("GEOHASH-4")
    @Story("Verilen geohash ve yon bilgisinden komsulugunda bulunan alanin geohash degeri elde edilmelidir.")
    @Description("Test adjacent function")
    public void adjacent() {
        String northN = GeoHash.adjacent("u120fxw", "N");
        String actualNorthN = GeoHash.encode(52.2063, 0.1188, 7);
        Assert.assertEquals(northN, actualNorthN);

    }

    @Test
    @Owner("gboztepe")
    @Severity(SeverityLevel.MINOR)
    @Issue("GEOHASH-5")
    @Story("Verilen geohash ve yon bilgisinden komsulugunda bulunan alanin geohash degeri elde edilmelidir.")
    @Description("Test neighbours function")
    public void neighbours() {
        String northN = GeoHash.neighbours("u120fxw", "ne");
        String actualNorthN = GeoHash.encode(52.2063, 0.1202, 7);
        Assert.assertEquals(northN, actualNorthN);
    }
}