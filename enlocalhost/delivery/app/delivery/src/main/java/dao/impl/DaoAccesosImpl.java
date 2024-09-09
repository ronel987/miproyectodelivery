package dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import dao.DaoAccesos;
import dto.Usuarios;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DaoAccesosImpl extends DaoImpl implements DaoAccesos {

    public DaoAccesosImpl() {
        super();
    }

    @Override
    public Usuarios autentica(String usuario, String password) {
        String sql = new StringBuilder()
                .append("SELECT idusuario, apellidos, nombres, autorizacion ")
                .append("FROM usuarios ")
                .append("WHERE usuario = ? ")
                .append("AND AES_DECRYPT(password, 'parainfo') = ?")
                .toString();

        Usuarios usuarios = null;

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    usuarios = new Usuarios();

                    usuarios.setIdusuario(rs.getInt(1));
                    usuarios.setApellidos(rs.getString(2));
                    usuarios.setNombres(rs.getString(3));
                    usuarios.setAutorizacion(rs.getString(4));

                } else {
                    message = "Usuario NO registrado";
                }

            } catch (SQLException e) {
                message = e.getMessage();
            }

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return usuarios;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
