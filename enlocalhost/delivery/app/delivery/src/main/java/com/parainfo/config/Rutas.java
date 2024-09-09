package com.parainfo.config;

import jakarta.servlet.http.HttpServletRequest;

public class Rutas {

    public static String getDocs(HttpServletRequest request) {
        
        String path = request.getServletContext().getRealPath("")
                + "/WEB-INF/";
        
        return path;
    }
}
