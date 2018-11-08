package com.speechscrubber;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("speech")
public class RevSpeechService {

    @GET
    @Path("check")
    @Produces(MediaType.TEXT_PLAIN)
    public String check() {
        return "it works!!";
    }

}
