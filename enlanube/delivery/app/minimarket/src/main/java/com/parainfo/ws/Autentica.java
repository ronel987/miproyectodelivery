package com.parainfo.ws;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import com.parainfo.ws.validator.ValidatorAutentica;

@Path("autentica")
public class Autentica {

    @Inject
    private ValidatorAutentica validator;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autentica(@HeaderParam("user") String user) {   //user llega cifrado

        Map<String, Object> map = validator.autentica(user);

        return Response
                .ok()
                .entity(map)
                .build();
    }
    
    @GET
    @Path("/valida")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validaToken(
            @QueryParam("token") String token,
            @QueryParam("autorizacion") String autorizacion) {
        
        Map<String, Object> map = validator.validaToken(token, autorizacion);

        return Response
                .ok()
                .entity(map)
                .build();
    }
    
    @GET
    @Path("/exit")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exit(@QueryParam("idusuario") Integer idusuario) {
        
        Map<String, Object> map = validator.exit(idusuario);
        
        return Response
                .ok()
                .entity(map)
                .build();
    }
}
