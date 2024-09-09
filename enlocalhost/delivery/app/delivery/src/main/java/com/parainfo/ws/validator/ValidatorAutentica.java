package com.parainfo.ws.validator;

import dao.DaoAccesos;
import dto.Usuarios;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ValidatorAutentica {

    @Inject
    private DaoAccesos daoAccesos;

    public Map<String, Object> autentica(String user) {
        Map<String, Object> map = new HashMap<>();

        if (user == null || user.trim().equals("")) {
            map.put("message", "No puede acceder sin sus credenciales");
            return map;
        }
        //

        try {
            user = new String(Base64.getDecoder().decode(user.getBytes()));
            
        } catch (Exception e) {
            map.put("message", "Sus credenciales no están cifradas");
            return map;
        }
        //

        String[] data = user.split(" - ");
        Usuarios usuario = daoAccesos.autentica(data[0], data[1]);
        if (usuario != null) { // si todo bien
            
            String token = generaToken(usuario); // viene token codificado

            if (token.isEmpty()) {
                map.put("message", "NO se pudo generar token");
                
            } else {
                map.put("nombres", usuario.getNombres());
                map.put("apellidos", usuario.getApellidos());
                map.put("idusuario", usuario.getIdusuario());
                map.put("navega", usuario.getAutorizacion().toLowerCase());
                map.put("token", token);
            }

            return map;
        }
        //

        map.put("message", daoAccesos.getMessage());
        return map;
    }
    
    public Map<String, Object> validaToken(String token, String autorizacion) {
        
        String data = new String(Base64.getDecoder().decode(token.getBytes()));
        String nameFile = data.split(" - ")[0] + ".tmp";
        autorizacion = autorizacion.toUpperCase();
        
        boolean ok;
        try (InputStream is = new FileInputStream(nameFile)) {

            byte[] t = is.readAllBytes();
            String token_server = new String(t);
            String data2 = new String(Base64.getDecoder().decode(token_server.getBytes()));
            
            ok = token.compareTo(token_server) == 0 // todo el token
                    && data2.split(" - ")[1].toUpperCase().compareTo(autorizacion) == 0; // solo autorización

        } catch (IOException e) {
            ok = false;
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("ok", ok);
        
        return map;
    }
    
    public Map<String, Object> exit(Integer idusuario) {
        
        String nameFile = idusuario + ".tmp";
        
        boolean ok = (new File(nameFile)).delete();
        
        Map<String, Object> map = new HashMap<>();
        map.put("ok", ok);
        
        return map;
    }

    ///////////////////////////////////////////////////////
    private String generaToken(Usuarios usuario) {
        String token = "";

        String nameFile = usuario.getIdusuario() + ".tmp";
        String contentFile = usuario.getIdusuario() + " - "
                + usuario.getAutorizacion() + " - "
                + System.currentTimeMillis();

        try (OutputStream os = new FileOutputStream(nameFile)) {

            token = Base64.getEncoder().encodeToString(contentFile.getBytes());

            os.write(token.getBytes());

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return token;
    }
}
