package dao.impl;

import dao.DaoCategorias;
import dto.Categorias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoCategoriasImpl extends DaoImpl implements DaoCategorias {

    public DaoCategoriasImpl() {
        super();
    }

    @Override
    public Map<String, Object> categoriasQry(
            Integer numPag, Integer filsXpag, String findCol, String findData, String orderBy) {
        message = null;
        Map<String, Object> map = null;

        String sql01 = new StringBuilder()
                .append("SELECT ")
                .append("idcategoria,")
                .append("categoria,")
                .append("descripcion,")
                .append("fotopath ")
                .append("FROM categorias ")
                .append("WHERE ").append(findCol).append(" LIKE ? ")
                .append("ORDER BY ").append(orderBy).append(" ")
                .append("LIMIT ?, ?")
                .toString();

        String sql02 = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM categorias ")
                .append("WHERE ").append(findCol).append(" LIKE ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps01 = cn.prepareStatement(sql01); PreparedStatement ps02 = cn.prepareStatement(sql02)) {

            ps01.setString(1, "%" + findData + "%");
            ps01.setInt(2, numPag * filsXpag);
            ps01.setInt(3, filsXpag);

            ps02.setString(1, "%" + findData + "%");

            try (ResultSet rs01 = ps01.executeQuery(); ResultSet rs02 = ps02.executeQuery()) {

                List<Categorias> list = new ArrayList<>();

                while (rs01.next()) {
                    Categorias categorias = new Categorias();

                    categorias.setIdcategoria(rs01.getInt(1));
                    categorias.setCategoria(rs01.getString(2));
                    categorias.setDescripcion(rs01.getString(3));
                    categorias.setFotopath(rs01.getString(4));

                    list.add(categorias);
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
    public List<Object[]> categoriasQry() {
        message = null;
        List<Object[]> list = null;

        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idcategoria,")
                .append("categoria,")
                .append("descripcion ")
                .append("FROM categorias ")
                .append("ORDER BY categoria")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            list = new ArrayList<>();

            while (rs.next()) {
                Object[] fil = new Object[3];

                fil[0] = rs.getInt(1);
                fil[1] = rs.getString(2);
                fil[2] = rs.getString(3);

                list.add(fil);
            }

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return list;
    }

    @Override
    public String categoriasIns(Categorias categorias) {
        message = null;
        String sql = new StringBuilder()
                .append("INSERT INTO categorias(")
                .append("categoria,")
                .append("descripcion,")
                .append("fotopath ")
                .append(") VALUES(?, ?, ?)")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, categorias.getCategoria());
            ps.setString(2, categorias.getDescripcion());
            ps.setString(3, categorias.getFotopath());

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException e) {
            message = e.getMessage();

            if (message.toUpperCase().contains("DUPLICATE")) {
                message = "Categoría ya está registrada";
            }
        }

        return message;
    }

    @Override
    public Categorias categoriasGet(Integer idcategoria) {
        message = null;
        Categorias categorias = null;
        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idcategoria,")
                .append("categoria,")
                .append("descripcion,")
                .append("fotopath ")
                .append("FROM categorias ")
                .append("WHERE idcategoria = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idcategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categorias = new Categorias();

                    categorias.setIdcategoria(rs.getInt(1));
                    categorias.setCategoria(rs.getString(2));
                    categorias.setDescripcion(rs.getString(3));
                    categorias.setFotopath(rs.getString(4));

                } else {
                    message = "ID: " + idcategoria + " no existe";
                }
            }

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return categorias;
    }

    @Override
    public String categoriasUpd(Categorias categorias) {
        message = null;
        String sql = new StringBuilder()
                .append("UPDATE categorias SET ")
                .append("categoria = ?,")
                .append("descripcion = ?,")
                .append("fotopath = ? ")
                .append("WHERE idcategoria = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, categorias.getCategoria());
            ps.setString(2, categorias.getDescripcion());
            ps.setString(3, categorias.getFotopath());
            ps.setInt(4, categorias.getIdcategoria());

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException ex) {
            message = ex.getMessage();

            if (message.toUpperCase().contains("DUPLICATE")) {
                message = "Categoría ya está registrada";
            }
        }

        return message;
    }

    @Override
    public String categoriasDel(List<Integer> list) {
        message = null;
        String sql = "DELETE FROM categorias WHERE idcategoria = ?";

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
    public List<Object[]> categoriasCbo() {
        message = null;
        List<Object[]> list = null;

        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idcategoria,")
                .append("categoria ")
                .append("FROM categorias ")
                .append("ORDER BY categoria")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            list = new ArrayList<>();

            while (rs.next()) {
                Object[] fil = new Object[2];

                fil[0] = rs.getInt(1);
                fil[1] = rs.getString(2);

                list.add(fil);
            }

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return list;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
