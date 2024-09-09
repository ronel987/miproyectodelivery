package com.parainfo.ws;

import com.parainfo.config.Rutas;
import dto.Categorias;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import com.parainfo.ws.validator.ValidatorCategorias;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import parainfo.convert.DeString;

@Path("categorias")
public class CategoriasService {

    @Inject
    private ValidatorCategorias validator;
    
    @GET
    @Path("/qry")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response categoriasQry(
            @QueryParam("numPag") Integer numPag,
            @QueryParam("findCol") String findCol,
            @QueryParam("findData") String findData,
            @QueryParam("orderBy") String orderBy) {

        Map<String, Object> map = validator.categoriasQry(
                numPag, findCol, findData, orderBy);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/qry2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response categoriasQry() {

        Map<String, Object> map = validator.categoriasQry();

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
    @Path("/insUpd")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response categoriasInsUpd(@Context HttpServletRequest request) {

        Map<String, Object> map = validator.categoriasInsUpd(request);

        return Response.ok().entity(map).build();
    }

    @GET
    @Path("/get/{idcategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response categoriasGet(@PathParam("idcategoria") Integer idcategoria) {

        Map<String, Object> map = validator.categoriasGet(idcategoria);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @DELETE
    @Path("/del/{ids}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response categoriasDel(
            @Context HttpServletRequest request,
            @PathParam("ids") String ids) {

        List<Integer> list = DeString.ids(ids);

        Map<String, Object> map = validator.categoriasDel(request, list);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/foto/{idcategoria}")
    public Response categoriasFoto(
            @Context HttpServletRequest request,
            @PathParam("idcategoria") Integer idcategoria) {

        Map<String, Object> map = validator.categoriasGet(idcategoria);
        boolean ok = (boolean) map.get("ok");

        String fotopath;
        if (ok) {
            Categorias categorias = (Categorias) map.get("categorias");
            fotopath = categorias.getFotopath();
            String ruta = Rutas.getDocs(request) + "categorias/" + fotopath;

            StreamingOutput fileStream = (OutputStream os) -> {

                java.nio.file.Path path = Paths.get(ruta);
                byte[] data = Files.readAllBytes(path);
                os.write(data);
            };

            return Response
                    .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + fotopath)
                    .build();

        } else {
            return Response
                    .ok(map, MediaType.APPLICATION_JSON)
                    .entity(map)
                    .build();

        }
    }

    @GET
    @Path("/cbo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response categoriasCbo() {

        Map<String, Object> map = validator.categoriasCbo();

        return Response
                .ok()
                .entity(map)
                .build();
    }
}
