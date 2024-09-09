package com.parainfo.ws;

import com.parainfo.config.Rutas;
import dto.Productos;
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
import com.parainfo.ws.validator.ValidatorProductos;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import parainfo.convert.DeString;

@Path("productos")
public class ProductosService {

    @Inject
    private ValidatorProductos validator;

    @GET
    @Path("/qry/{idcategoria}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response productosQry(
            @PathParam("idcategoria") Integer idcategoria,
            @QueryParam("numPag") Integer numPag,
            @QueryParam("findCol") String findCol,
            @QueryParam("findData") String findData,
            @QueryParam("orderBy") String orderBy) {

        Map<String, Object> map = validator.productosQry(idcategoria,
                numPag, findCol, findData, orderBy);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/qry2/{idcategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response productosQry(@PathParam("idcategoria") Integer idcategoria) {

        Map<String, Object> map = validator.productosQry(idcategoria);

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
    public Response productosInsUpd(@Context HttpServletRequest request) {

        Map<String, Object> map = validator.productosInsUpd(request);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @DELETE
    @Path("/del/{ids}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response productosDel(
            @Context HttpServletRequest request,
            @PathParam("ids") String ids) {

        List<Integer> list = DeString.ids(ids);

        Map<String, Object> map = validator.productosDel(request, list);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/foto/{idproducto}")
    public Response productosFoto(
            @Context HttpServletRequest request,
            @PathParam("idproducto") Integer idproducto) {

        Map<String, Object> map = validator.productosGet(idproducto);
        boolean ok = (boolean) map.get("ok");

        String fotopath;
        if (ok) {
            Productos productos = (Productos) map.get("productos");
            fotopath = productos.getFotopath();
            String ruta = Rutas.getDocs(request) + "productos/" +fotopath;

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
    @Path("/get/{idproducto}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response productosGet(@PathParam("idproducto") Integer idproducto) {

        Map<String, Object> map = validator.productosGet(idproducto);

        return Response
                .ok()
                .entity(map)
                .build();
    }

    @GET
    @Path("/cbo/{idcategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response productosCbo(@PathParam("idcategoria") Integer idcategoria) {

        Map<String, Object> map = validator.productosCbo(idcategoria);

        return Response
                .ok()
                .entity(map)
                .build();
    }
}
