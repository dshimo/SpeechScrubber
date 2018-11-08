package com.speechscrubber;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.speechscrubber.job.JobChecker;

@Path("/speech")
public class RevSpeechService {

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
        JobChecker jobChecker = new JobChecker();
        return "id " + id + " ready to transcribe: " + jobChecker.isTranscriptReady(id);
    }

    @GET
    @Path("/{id}/transcript")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTranscript(@PathParam("id") String id) throws Exception {
        JobChecker jobChecker = new JobChecker();
        return "id" + id + " transcript = " + jobChecker.getTranscript(id); 
    }
}
