package com.parainfo.ws.validator;

import com.parainfo.config.Rutas;
import dao.DaoCategorias;
import dto.Categorias;
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

public class ValidatorCategorias {

    @Inject
    private DaoCategorias daoCategorias;

    @Inject
    private FileUploadMas uploadMas;

    private final Integer filsXpag;

    public ValidatorCategorias() {
        filsXpag = 15; // cantidad de filas por página
    }

    public Map<String, Object> categoriasQry(
            Integer numPag, String findCol, String findData, String orderBy) {

        Map<String, Object> map = daoCategorias.categoriasQry(
                numPag, filsXpag, findCol, findData, orderBy);

        if (map == null) {
            map = new HashMap<>();
            map.put("msg", daoCategorias.getMessage());
        }

        return map;
    }

    public Map<String, Object> categoriasQry() {

        List<Object[]> list = daoCategorias.categoriasQry();

        Map<String, Object> map = new HashMap<>();

        if (list != null) {
            map.put("ok", true);
            map.put("categorias", list);

        } else {
            map.put("ok", false);
        }

        return map;
    }

    public Map<String, String[]> dlgSearch() {
        Map<String, String[]> map = new HashMap<>();

        // columnas por las cual el categoria puede filtrar
        map.put("findCols", new String[]{
            "categoria"
        });

        // texto que se mostrará por columna
        map.put("viewCols", new String[]{
            "Categoría"
        });

        return map;
    }

    public Map<String, Object> categoriasGet(Integer idcategoria) {

        Map<String, Object> map = new HashMap<>();

        if (idcategoria == null) {
            map.put("ok", false);
            map.put("msg", "ID de categoría incorrecto");
        }

        Categorias categorias = daoCategorias.categoriasGet(idcategoria);

        if (categorias != null) {
            map.put("ok", true);
            map.put("categorias", categorias);

        } else {
            map.put("ok", false);
        }

        return map;
    }

    public Map<String, Object> categoriasInsUpd(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Categorias categorias = new Categorias();

        map.put("ok", true); // optimista
        List<String> list_msg = new ArrayList<>(); // para mensajería
        String accion = null; // INS, UPD
        byte[] imagen = null; // imagen de categoría
        String categoriaOld = null; // flag para eliminar imagen, caso UPD

        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (isMultipart) { // recoge de parámetros

            List<DiskFileItem> list = uploadMas.recogeParam(request);

            for (DiskFileItem item : list) {

                String name = item.getFieldName().trim();

                switch (name) {
                    case "accion" ->
                        accion = item.getString();

                    case "idcategoria" ->
                        categorias.setIdcategoria(DeString.aInteger(item.getString()));

                    case "categoria" ->
                        categorias.setCategoria(new String(item.get(), StandardCharsets.UTF_8));

                    case "descripcion" ->
                        categorias.setDescripcion(new String(item.get(), StandardCharsets.UTF_8));

                    case "imagen" -> {
                        imagen = item.get(); // siempre viene algo (cabecera)
                        String imagenName = item.getName(); // viene null cuando NO envía imagen

                        if (imagenName == null) {
                            imagen = null;
                        }

                        categorias.setFotopath(imagenName);
                    }
                }
            }

        } else {
            list_msg.add("Envío debe ser multipart");
        }

        // validaciones de los datos envíados por cliente
        if (accion == null) {
            list_msg.add("Acción no definida");
        }

        if (categorias.getIdcategoria() == null) {
            list_msg.add("Debe enviar ID");
        }

        if (categorias.getCategoria() == null || categorias.getCategoria().trim().isEmpty()) {
            list_msg.add("Debe enviar Categoría");
        }

        if ((accion != null && accion.equals("INS"))
                && (imagen == null || categorias.getFotopath() == null)) {
            list_msg.add("Debe enviar imagen de categoría");
        }

        if (list_msg.isEmpty()) {

            // caso del UPD
            if (accion != null && accion.contentEquals("UPD")) {

                Categorias cate = daoCategorias.categoriasGet(categorias.getIdcategoria());

                // para UPD cuando NO se desea cambiar imagen
                if (categorias.getFotopath() == null) {

                    categorias.setFotopath(cate.getFotopath()); // se mantiene anterior imagen

                } else { // SI cambia imagen, entonces debemos borrar la anterior

                    categoriaOld = cate.getFotopath();
                }
            }
        }

        if (list_msg.isEmpty()) { // todo OK para DB
            String msg = switch (accion) {
                case "INS" ->
                    daoCategorias.categoriasIns(categorias);
                case "UPD" ->
                    daoCategorias.categoriasUpd(categorias);
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
                    + "/categorias/", categorias.getFotopath(), imagen);

            if (!msg.contentEquals("ok")) {
                list_msg.add(msg);
            }

            if (categoriaOld != null) { // borrar vieja imagen para UPD

                msg = uploadMas.deleteFile(Rutas.getDocs(request)
                        + "/categorias/", categoriaOld);

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

    public Map<String, Object> categoriasDel(HttpServletRequest request,
            List<Integer> list) {

        Map<String, Object> map = new HashMap<>();
        map.put("ok", true); // optimista

        if (list == null) {
            map.put("ok", false);
            map.put("msg", "IDs incorrectos");

        } else { // eliminando imágenes
            for (Integer id : list) {
                Categorias c = daoCategorias.categoriasGet(id);

                uploadMas.deleteFile(Rutas.getDocs(request)
                        + "/categorias/", c.getFotopath());
            }

            // eliminanado categorias en DB
            String msg = daoCategorias.categoriasDel(list);

            if (msg != null) {
                map.put("ok", false);
                map.put("msg", msg);
            }
        }

        return map;
    }

    public Map<String, Object> categoriasCbo() {

        List<Object[]> list = daoCategorias.categoriasCbo();

        Map<String, Object> map = new HashMap<>();

        if (list != null) {
            map.put("ok", true);
            map.put("categorias", list);

        } else {
            map.put("ok", false);
        }

        return map;
    }

}
