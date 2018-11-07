package com.speechscrubber.client;

import javax.ws.rs.*;

import javax.enterprise.context.Dependent;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Dependent
@RegisterRestClient
@Produces("application/json")
@Path("/jobs")
public interface RevSpeechClientService {

    @GET
    @Produces("application/json")
    public Response getJob(@PathParam("id") String id);

//    @POST
}
