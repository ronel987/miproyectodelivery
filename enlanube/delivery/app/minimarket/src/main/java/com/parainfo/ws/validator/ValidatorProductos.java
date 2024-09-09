package com.parainfo.ws.validator;

import com.parainfo.config.Rutas;
import dao.DaoProductos;
import dto.Productos;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import parainfo.convert.DeString;
import parainfo.fileupload.FileUploadMas;

public class ValidatorProductos {

    @Inject
    private DaoProductos daoProductos;

    @Inject
    private FileUploadMas uploadMas;

    private final Integer filsXpag;

    public ValidatorProductos() {
        filsXpag = 15; // cantidad de filas por página
    }

    public Map<String, Object> productosQry(Integer idcategoria,
            Integer numPag, String findCol, String findData, String orderBy) {

        Map<String, Object> map = daoProductos.productosQry(idcategoria,
                numPag, filsXpag, findCol, findData, orderBy);

        if (map == null) {
            map = new HashMap<>();
            map.put("msg", daoProductos.getMessage());
        }

        return map;
    }

    public Map<String, Object> productosQry(Integer idcategoria) {

        List<Object[]> list = daoProductos.productosQry(idcategoria);

        Map<String, Object> map = new HashMap<>();

        if (list != null) {
            map.put("ok", true);
            map.put("productos", list);

        } else {
            map.put("ok", false);
        }

        return map;
    }

    public Map<String, String[]> dlgSearch() {
        Map<String, String[]> map = new HashMap<>();

        // columnas por las cual el producto puede filtrar
        map.put("findCols", new String[]{
            "producto",
            "precio",
            "estado"
        });

        // texto que se mostrará por columna
        map.put("viewCols", new String[]{
            "Producto",
            "Precio",
            "Estado"
        });

        return map;
    }

    public Map<String, Object> productosGet(Integer idproducto) {

        Map<String, Object> map = new HashMap<>();

        if (idproducto == null) {
            map.put("ok", false);
            map.put("msg", "ID de producto incorrecto");
        }

        Productos productos = daoProductos.productosGet(idproducto);

        if (productos != null) {
            map.put("ok", true);
            map.put("productos", productos);

        } else {
            map.put("ok", false);
        }

        return map;
    }

    public Map<String, Object> productosInsUpd(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Productos productos = new Productos();

        map.put("ok", true); // optimista
        List<String> list_msg = new ArrayList<>(); // para mensajería
        String accion = null; // INS, UPD
        byte[] imagen = null; // imagen de categoría
        String productosOld = null; // flag para eliminar imagen, caso UPD

        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (isMultipart) { // recoge de parámetros

            List<DiskFileItem> list = uploadMas.recogeParam(request);

            for (DiskFileItem item : list) {

                String name = item.getFieldName().trim();

                switch (name) {
                    case "accion" ->
                        accion = item.getString();

                    case "idproducto" ->
                        productos.setIdproducto(DeString.aInteger(item.getString()));

                    case "idcategoria" ->
                        productos.setIdcategoria(DeString.aInteger(item.getString()));

                    case "producto" ->
                        productos.setProducto(new String(item.get(), StandardCharsets.UTF_8));

                    case "precio" ->
                        productos.setPrecio(DeString.aDouble(item.getString()));

                    case "imagen" -> {
                        imagen = item.get(); // siempre viene algo (cabecera)
                        String imagenName = item.getName(); // viene null cuando NO envía imagen

                        if (imagenName == null) {
                            imagen = null;
                        }

                        productos.setFotopath(imagenName);
                    }

                    case "estado" ->
                        productos.setEstado(item.getString());
                }
            }

        } else {
            list_msg.add("Envío debe ser multipart");
        }

        // validaciones de los datos envíados por cliente
        if (accion == null) {
            list_msg.add("Acción no definida");
        }

        if (productos.getIdproducto() == null) {
            list_msg.add("Debe enviar ID");
        }

        if (productos.getProducto() == null || productos.getProducto().trim().isEmpty()) {
            list_msg.add("Debe enviar Producto");
        }

        if (productos.getPrecio() == null) {
            list_msg.add("Debe enviar precio Producto");
        }

        if ((accion != null && accion.equals("INS"))
                && (imagen == null || productos.getFotopath() == null)) {
            list_msg.add("Debe enviar imagen de producto");
        }

        if (list_msg.isEmpty()) {

            // caso del UPD
            if (accion != null && accion.contentEquals("UPD")) {

                Productos prod = daoProductos.productosGet(productos.getIdproducto());

                // para UPD cuando NO se desea cambiar imagen
                if (productos.getFotopath() == null) {

                    productos.setFotopath(prod.getFotopath()); // se mantiene anterior imagen

                } else { // SI cambia imagen, entonces debemos borrar la anterior

                    productosOld = prod.getFotopath();
                }
            }
        }

        if (list_msg.isEmpty()) { // todo OK para DB
            String msg = switch (accion) {
                case "INS" ->
                    daoProductos.productosIns(productos);
                case "UPD" ->
                    daoProductos.productosUpd(productos);
                default ->
                    "Acción no definida";
            };

            if (msg != null) {
                list_msg.add(msg);
            }
        }

        // guardar imagen (INS/UP), y eliminar (UPD)
        if (list_msg.isEmpty() && imagen != null) {

            String msg = uploadMas.saveFile(Rutas.getDocs(request)
                    + "/productos/", productos.getFotopath(), imagen);

            if (!msg.contentEquals("ok")) {
                list_msg.add(msg);
            }

            if (productosOld != null) { // borrar vieja imagen para UPD

                msg = uploadMas.deleteFile(Rutas.getDocs(request)
                        + "/productos/", productosOld);

                if (!msg.contentEquals("ok")) {
                    list_msg.add(msg);
                }
            }
        }

        if (!list_msg.isEmpty()) {
            map.put("ok", false);
            map.put("msg", list_msg);
        }

        return map;
    }

    public Map<String, Object> productosDel(HttpServletRequest request,
            List<Integer> list) {

        Map<String, Object> map = new HashMap<>();
        map.put("ok", true); // optimista

        if (list == null) {
            map.put("ok", false);
            map.put("msg", "IDs incorrectos");

        } else { // eliminando imágenes
            for (Integer id : list) {
                Productos p = daoProductos.productosGet(id);

                uploadMas.deleteFile(Rutas.getDocs(request)
                        + "/categorias/", p.getFotopath());
            }

            // eliminanado categorias en DB
            String msg = daoProductos.productosDel(list);

            if (msg != null) {
                map.put("ok", false);
                map.put("msg", msg);
            }
        }

        return map;
    }

    public Map<String, Object> productosCbo(Integer idcategoria) {

        List<Object[]> list = daoProductos.productosCbo(idcategoria);

        Map<String, Object> map = new HashMap<>();

        if (list != null) {
            map.put("ok", true);
            map.put("productos", list);

        } else {
            map.put("ok", false);
        }

        return map;
    }
}
