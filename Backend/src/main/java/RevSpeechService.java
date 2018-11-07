
import javax.ws.rs.GET;

@Path("/speech")
@Consumes("application/json")
public interface RevSpeechService {

    @POST
    long upload();

    @GET
    int jobProgress();

    @GET
    transcription getTranscript();
}