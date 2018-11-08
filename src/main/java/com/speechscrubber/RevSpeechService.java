package com.speechscrubber;

import javax.json.JsonObject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.speechscrubber.job.JobChecker;

@Path("/speech")
public class RevSpeechService {
    String searchPhrase;
    JobChecker jc = new JobChecker();

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "yay!!";
    }

    @GET
    @Path("/check/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String isTranscriptReady(@PathParam("id") String id) {
        return "id " + id + " ready to transcribe: " + jc.isTranscriptReady(id);
    }

    @GET
    @Path("/{id}/transcript")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getTranscript(@PathParam("id") String id) throws Exception {
        return jc.getTranscript(id); 
    }

    @POST
    @Path("/search")
    @Produces(MediaType.TEXT_PLAIN)
    public String makeSearchPhrase(@QueryParam("phrase") String phrase) {
        setSearchPhrase(phrase);
        return "passed phrase: " + getSearchPhrase();
    }

    private void setSearchPhrase(String phrase) {
        this.searchPhrase = phrase;
    }

    private String getSearchPhrase() {
        return this.searchPhrase;
    }
}
