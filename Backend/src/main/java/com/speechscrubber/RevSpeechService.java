package com.speechscrubber;

import com.speechscrubber.client.RevSpeechClientService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("speech")
public class RevSpeechService {

    @Inject
    @RestClient
    private RevSpeechClientService revClient;

    @GET
    @Path("check")
    @Produces(MediaType.TEXT_PLAIN)
    public String check() {
        return "it works!!";
    }

    @GET
    public Response getJob(@PathParam("id") String id) throws Exception {
        System.out.println("Logic goes here!");

        try {
            return revClient.getJob(id);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build());
            throw new Exception(e.toString());
        }

    }

//    @POST
//    public Response postJob() {
//
//    }
//
//    @GET
//    public Response getTranscript() {
//
//    }
}
