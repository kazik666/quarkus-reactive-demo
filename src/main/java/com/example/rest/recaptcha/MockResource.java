package com.example.rest.recaptcha;

import com.example.rest.recaptcha.model.SiteVerifyResponse;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/recaptcha/api/siteverify")
public class MockResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<SiteVerifyResponse> siteVerify(@FormParam("response") String response
            , @FormParam("secret") String secret, @FormParam("remoteip") String remoteip) {
        return Uni.createFrom().item(SiteVerifyResponse.builder()
                .success(true)
                .score((float) 1.0)
                .action("ACTION")
                .hostname("localhost")
                .challengeTs(new Date())
                .build());
    }
}
