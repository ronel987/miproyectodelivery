package dao.impl;

import dao.DaoUsuarios;
import dto.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoUsuariosImpl extends DaoImpl implements DaoUsuarios {

    public DaoUsuariosImpl() {
        super();
    }

    @Override
    public Map<String, Object> usuariosQry(Integer numPag, Integer filsXpag,
            String findCol, String findData, String orderBy) {
        message = null;
        Map<String, Object> map = null;

        String sql01 = new StringBuilder()
                .append("SELECT ")
                .append("idusuario,")
                .append("apellidos,")
                .append("nombres,")
                .append("usuario,")
                .append("autorizacion ")
                .append("FROM usuarios ")
                .append("WHERE ").append(findCol).append(" LIKE ? ")
                .append("ORDER BY ").append(orderBy).append(" ")
                .append("LIMIT ?, ?")
                .toString();

        String sql02 = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM usuarios ")
                .append("WHERE ").append(findCol).append(" LIKE ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps01 = cn.prepareStatement(sql01); PreparedStatement ps02 = cn.prepareStatement(sql02)) {

            ps01.setString(1, "%" + findData + "%");
            ps01.setInt(2, numPag * filsXpag);
            ps01.setInt(3, filsXpag);

            ps02.setString(1, "%" + findData + "%");

            try (ResultSet rs01 = ps01.executeQuery(); ResultSet rs02 = ps02.executeQuery()) {

                List<Usuarios> list = new ArrayList<>();

                while (rs01.next()) {
                    Usuarios usuarios = new Usuarios();

                    usuarios.setIdusuario(rs01.getInt(1));
                    usuarios.setApellidos(rs01.getString(2));
                    usuarios.setNombres(rs01.getString(3));
                    usuarios.setUsuario(rs01.getString(4));
                    usuarios.setAutorizacion(rs01.getString(5));

                    list.add(usuarios);
                }

                // tomando el total de filas de la consulta
                rs02.next();
                Integer ctasFils = rs02.getInt(1);

                /////////////////////////////////////////
                map = new HashMap<>();
                map.put("rows", list);
                map.put("ctasFils", ctasFils);
                map.put("ctasPags", (ctasFils % filsXpag == 0
                        ? ctasFils / filsXpag
                        : ctasFils / filsXpag + 1));

            } catch (SQLException e) {
                message = e.getMessage();
            }

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return map;
    }

    @Override
    public String usuariosIns(Usuarios usuarios) {
        message = null;
        String sql = new StringBuilder()
                .append("INSERT usuarios(")
                .append("apellidos,")
                .append("nombres,")
                .append("usuario,")
                .append("password,")
                .append("autorizacion")
                .append(") VALUES(?, ?, ?, AES_ENCRYPT(?, 'parainfo'), ?)")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuarios.getApellidos());
            ps.setString(2, usuarios.getNombres());
            ps.setString(3, usuarios.getUsuario());
            ps.setString(4, usuarios.getPassword());
            ps.setString(5, usuarios.getAutorizacion());

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException | NullPointerException e) {
            message = e.getMessage();

            if (message.toUpperCase().contains("DUPLICATE")) {
                message = "Este Usuario ya está registrado (coincidencia en apellidos y nombre o nick)";
            }
        }

        return message;
    }

    @Override
    public String usuariosDel(List<Integer> list) {
        message = null;
        String sql = "DELETE FROM usuarios WHERE idusuario = ?";

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            cn.setAutoCommit(false); // desactiva autoCommit
            boolean ok = true;

            for (Integer x : list) {
                ps.setInt(1, x);

                int ctos = ps.executeUpdate();
                if (ctos == 0) {
                    ok = false;
                    message = "ID recibido no existe";
                    break;
                }
            }

            if (ok) {
                cn.commit();
            } else {
                cn.rollback();
            }

            cn.setAutoCommit(true); // activa autoCommit

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return message;
    }

    @Override
    public Usuarios usuariosGet(Integer idusuario) {
        message = null;
        Usuarios usuarios = null;

        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idusuario,")
                .append("apellidos,")
                .append("nombres,")
                .append("usuario,")
                .append("autorizacion ")
                .append("FROM usuarios ")
                .append("WHERE idusuario = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idusuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    usuarios = new Usuarios();

                    usuarios.setIdusuario(rs.getInt(1));
                    usuarios.setApellidos(rs.getString(2));
                    usuarios.setNombres(rs.getString(3));
                    usuarios.setUsuario(rs.getString(4));
                    usuarios.setAutorizacion(rs.getString(5));

                } else {
                    message = "ID no existe";
                }

            } catch (SQLException | NullPointerException e) {
                message = e.getMessage();
            }

        } catch (SQLException | NullPointerException e) {
            message = e.getMessage();
        }

        return usuarios;
    }

    @Override
    public String usuariosUpd(Usuarios usuarios) {
        message = null;
        String sql = new StringBuilder()
                .append("UPDATE usuarios SET ")
                .append("apellidos = ?,")
                .append("nombres = ?,")
                .append("usuario = ?,")
                .append("autorizacion = ? ")
                .append("WHERE idusuario = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuarios.getApellidos());
            ps.setString(2, usuarios.getNombres());
            ps.setString(3, usuarios.getUsuario());
            ps.setString(4, usuarios.getAutorizacion());
            ps.setInt(5, usuarios.getIdusuario());

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException | NullPointerException e) {
            message = e.getMessage();

            if (message.toUpperCase().contains("DUPLICATE")) {
                message = "Este Usuario ya está registrado (coincidencia en apellidos y nombre o nick)";
            }
        }

        return message;
    }

    @Override
    public String changePassword(Integer idusuario, String newPassword) {
        message = null;
        String sql = new StringBuilder()
                .append("UPDATE usuarios SET ")
                .append("password = AES_ENCRYPT(?, 'parainfo') ")
                .append("WHERE idusuario = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, idusuario);

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException | NullPointerException e) {
            message = e.getMessage();
        }

        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
