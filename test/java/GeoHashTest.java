import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.Test;

import tr.com.havelsan.geohash.GeoHash;

public class GeoHashTest {

    @Test
    @Owner("gboztepe")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("GEOHASH-1")
    @Story("Verilen lat lon ve precision degerlerinden geohash olusturulmalidir.")
    @Description("Test encode function")
    public void encode() {
        String encoded = GeoHash.encode(0.119,52.205,  7);
        Assert.assertEquals(encoded,"u120fxw");

    }


}