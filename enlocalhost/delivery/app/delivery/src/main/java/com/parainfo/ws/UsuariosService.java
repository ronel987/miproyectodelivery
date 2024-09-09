package com.parainfo.ws;

import dto.Usuarios;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import com.parainfo.ws.validator.ValidatorUsuarios;
import java.util.List;
import parainfo.convert.DeString;

@Path("usuarios")
public class UsuariosService {

    @Inject
    private ValidatorUsuarios validator;

    @GET
    @Path("/qry")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response usuariosQry(
            @QueryParam("numPag") Integer numPag,
            @QueryParam("findCol") String findCol,
            @QueryParam("findData") String findData,
            @QueryParam("orderBy") String orderBy) {

        Map<String, Object> map = validator.usuariosQry(
                numPag, findCol, findData, orderBy);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dlgSearch() {

        Map<String, String[]> map = validator.dlgSearch();

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @POST
    @Path("/ins")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response usuariosIns(Usuarios usuarios) {

        Map<String, Object> map = validator.usuariosInsUpd(usuarios, false);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @DELETE
    @Path("/del/{ids}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response usuariosDel(@PathParam("ids") String ids) {

        List<Integer> list = DeString.ids(ids);
        
        Map<String, Object> map = validator.usuariosDel(list);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/get/{idusuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response usuariosGet(@PathParam("idusuario") Integer idusuario) {

        Map<String, Object> map = validator.usuariosGet(idusuario);

        Response.ResponseBuilder rb = Response.ok();

        if ((boolean) map.get("ok")) {

            rb.entity((Usuarios) map.get("usuarios"));
            rb.status(Response.Status.OK);

        } else {
            rb.entity(map);
            rb.status(Response.Status.ACCEPTED);
        }

        return rb.build();
    }

    @PUT
    @Path("/upd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response usuariosUpd(Usuarios usuarios) {

        Map<String, Object> map = validator.usuariosInsUpd(usuarios, true);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @PUT
    @Path("/password")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@HeaderParam("passwords") String passwords) {

        Map<String, Object> map = validator.changePassword(passwords);

        return Response
                .ok()
                .entity(map)
                .build();
    }
}
