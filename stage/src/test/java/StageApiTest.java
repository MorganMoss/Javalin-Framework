import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wethinkcode.stage.StageService;
import wethinkcode.stage.model.BadStageException;
import wethinkcode.stage.model.Stage;

import java.io.IOException;
import java.net.URISyntaxException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static wethinkcode.stage.StageService.SERVICE;

/**
 * *Functional* tests of the PlaceNameService.
 */
public class StageApiTest
{
    public static final int TEST_PORT = 7778;


    @BeforeAll
    public static void startServer() throws IOException, URISyntaxException {

        StageService.activate(
            SERVICE.initialise("-p="+TEST_PORT),
            "Test-Stage-Service"
        );
    }

    @AfterAll
    public static void stopServer(){
        SERVICE.stop();
    }

    @Test
    public void getStageJson() throws BadStageException {
        HttpResponse<JsonNode> response = Unirest.get( serverUrl() + "/stage" ).asJson();
        assertEquals(HttpStatus.OK ,response.getStatus());
        Stage stage = Stage.stageFromNumber(response.getBody().getObject().getInt("stage"));
        assertEquals(SERVICE.stage, stage);
    }

    @Test
    public void postStageOK() throws BadStageException {
        HttpResponse<JsonNode> response = Unirest.post( serverUrl() + "/stage" ).body("{\"stage\" : 4}").asJson();
        assertEquals(HttpStatus.ACCEPTED ,response.getStatus());

        response = Unirest.get( serverUrl() + "/stage" ).asJson();
        assertEquals(HttpStatus.OK ,response.getStatus());
        Stage stage = Stage.stageFromNumber(response.getBody().getObject().getInt("stage"));
        assertEquals(Stage.STAGE4, stage);

    }

    @Test
    public void postStageBadNumber(){
        HttpResponse<JsonNode> response = Unirest.post( serverUrl() + "/stage" ).body("{\"stage\" : 10}").asJson();
        assertEquals(HttpStatus.BAD_REQUEST ,response.getStatus());
    }


    @Test
    public void postStageBadNumberType(){
        HttpResponse<JsonNode> response = Unirest.post( serverUrl() + "/stage" ).body("{\"stage\" : \"4\"}").asJson();
        assertEquals(HttpStatus.BAD_REQUEST ,response.getStatus());
    }

    @Test
    public void postStageNoBody(){
        HttpResponse<JsonNode> response = Unirest.post( serverUrl() + "/stage" ).body("{}").asJson();
        assertEquals(HttpStatus.BAD_REQUEST ,response.getStatus());
    }

    private String serverUrl(){
        return "http://localhost:" + TEST_PORT;
    }
}
