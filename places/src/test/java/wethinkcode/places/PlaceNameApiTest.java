package wethinkcode.places;

import java.io.IOException;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * *Functional* tests of the PlaceNameService.
 */
public class PlaceNameApiTest
{
    public static final int TEST_PORT = 7777;

    private static PlaceNameService server;

    @BeforeAll
    public static void startServer() throws IOException{
        // FIXME
    }

    @AfterAll
    public static void stopServer(){
        // FIXME
    }

    @Test
    public void getProvincesJson(){
        HttpResponse<JsonNode> response = Unirest.get( serverUrl() + "/provinces" ).asJson();
    }

    @Test
    public void getTownsInAProvince_provinceExistsInDb(){
        HttpResponse<JsonNode> response = Unirest.get( serverUrl() + "/place/KwaZulu-Natal" ).asJson();
        fail( "TODO" );
    }

    @Test
    public void getTownsInAProvince_noSuchProvinceInDb(){
        HttpResponse<JsonNode> response = Unirest.get( serverUrl() + "/place/Oregon" ).asJson();
        fail( "TODO" );
    }

    private String serverUrl(){
        return "http://localhost:" + TEST_PORT;
    }
}
