
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/speech")
public interface RevSpeechService {

    @POST
    long upload();

    @GET
    int jobProgress();

    @GET
    String getTranscript();
}