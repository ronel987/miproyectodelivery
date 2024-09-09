package com.parainfo.ws.validator;

import dao.DaoUsuarios;
import dto.Usuarios;
import jakarta.inject.Inject;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import parainfo.convert.DeString;
import parainfo.valida.ValidaTexto;

public class ValidatorUsuarios {

    @Inject
    private DaoUsuarios daoUsuarios;

    private final Integer filsXpag;

    public ValidatorUsuarios() {
        filsXpag = 15; // cantidad de filas por página
    }

    public Map<String, Object> usuariosQry(
            Integer numPag, String findCol, String findData, String orderBy) {

        Map<String, Object> map = daoUsuarios.usuariosQry(
                numPag, filsXpag, findCol, findData, orderBy);

        if (map == null) {
            map = new HashMap<>();
            map.put("msg", daoUsuarios.getMessage());
        }

        return map;
    }

    public Map<String, String[]> dlgSearch() {
        Map<String, String[]> map = new HashMap<>();

        // columnas por las cual el usuario puede filtrar
        map.put("findCols", new String[]{
            "apellidos", "nombres", "usuario", "autorizacion"
        });

        // texto que se mostrará por columna
        map.put("viewCols", new String[]{
            "Apellidos", "Nombres", "Usuario", "Autorización"
        });

        return map;
    }

    public Map<String, Object> usuariosInsUpd(Usuarios usuarios, boolean upd) {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ok", true); // optimistamente

        if (!ValidaTexto.conSpace(usuarios.getApellidos())) {
            map.put("msg", "Apellidos debe tener solo letras y spacio");
        }

        if (!ValidaTexto.conSpace(usuarios.getNombres())) {
            map.put("msg", "Nombres debe tener solo letras y spacio");
        }

        if (!ValidaTexto.conDigitos(usuarios.getUsuario())) {
            map.put("msg", "Usuario (nick) debe tener solo letras y dígitos");
        }

        if (!upd && !ValidaTexto.conDigitos(usuarios.getPassword())) {
            map.put("msg", "Password debe tener solo letras y dígitos");
        }

        if (map.size() > 1) {
            map.put("ok", false);
            return map;
        }

        usuarios.setAutorizacion(usuarios.getAutorizacion().toUpperCase());
        String msg = upd
                ? daoUsuarios.usuariosUpd(usuarios)
                : daoUsuarios.usuariosIns(usuarios);

        if (msg != null) {
            map.put("ok", false);
            map.put("msg", msg);
        }

        return map;
    }

    public Map<String, Object> usuariosDel(List<Integer> list) {

        Map<String, Object> map = new HashMap<>();

        if (list == null) {
            map.put("ok", false);
            map.put("msg", "IDs incorrectos");
        }

        String msg = daoUsuarios.usuariosDel(list);

        if (msg != null) {
            map.put("ok", false);
            map.put("msg", msg);

        } else {
            map.put("ok", true);
        }

        return map;
    }

    public Map<String, Object> usuariosGet(Integer idusuario) {

        Map<String, Object> map = new HashMap<>();

        if (idusuario == null) {
            map.put("ok", false);
            map.put("msg", "ID de usuario incorrecto");
            return map;
        }

        Usuarios usuarios = daoUsuarios.usuariosGet(idusuario);

        if (usuarios == null) {
            map.put("ok", false);
            map.put("msg", daoUsuarios.getMessage());
            return map;
        }

        map.put("ok", true);
        map.put("usuarios", usuarios);
        return map;
    }

    public Map<String, Object> changePassword(String passwords) {
        Map<String, Object> map = new HashMap<>();

        try {
            passwords = new String(Base64.getDecoder().decode(passwords.getBytes()));

        } catch (Exception e) {
            map.put("message", "Sus credenciales no están cifradas");
            return map;
        }

        String[] pass = passwords.split(" - ");
        Integer idusuario = DeString.aInteger(pass[0]);
        String password = pass[1].trim();
        String password1 = pass[2].trim();
        String password2 = pass[3].trim();

        if (password.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            map.put("message", "Debe ingresar sus tres credenciales");
            return map;
        }

        String msg = daoUsuarios.changePassword(idusuario, password1);

        if (msg != null) {
            map.put("message", msg);
            return map;
        }

        map.put("message", "Su password ha sido cambiado.");
        return map;
    }
}
