package dao.impl;

import dao.DaoProductos;
import dto.Productos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoProductosImpl extends DaoImpl implements DaoProductos {

    public DaoProductosImpl() {
        super();
    }

    @Override
    public List<Object[]> productosCbo(Integer idcategoria) {
        message = null;
        List<Object[]> list = null;

        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idproducto,")
                .append("producto ")
                .append("FROM productos ")
                .append("WHERE idcategoria = ? ")
                .append("AND estado = '1' ")
                .append("ORDER BY producto")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idcategoria);

            try (ResultSet rs = ps.executeQuery()) {

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

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return list;
    }

    @Override
    public Productos productosGet(Integer idproducto) {
        message = null;
        Productos productos = null;
        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idproducto,")
                .append("idcategoria,")
                .append("producto,")
                .append("precio,")
                .append("fotopath,")
                .append("estado ")
                .append("FROM productos ")
                .append("WHERE idproducto = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idproducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    productos = new Productos();

                    productos.setIdproducto(rs.getInt(1));
                    productos.setIdcategoria(rs.getInt(2));
                    productos.setProducto(rs.getString(3));
                    productos.setPrecio(rs.getDouble(4));
                    productos.setFotopath(rs.getString(5));
                    productos.setEstado(rs.getString(6));

                } else {
                    message = "ID: " + idproducto + " no existe";
                }
            }

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return productos;
    }

    @Override
    public Map<String, Object> productosQry(Integer idcategoria,
            Integer numPag, Integer filsXpag, String findCol, String findData, String orderBy) {
        message = null;
        Map<String, Object> map = null;

        String sql01 = new StringBuilder()
                .append("SELECT ")
                .append("idproducto,")
                .append("producto,")
                .append("FORMAT(precio, 2),")
                .append("IF(estado = '1', 'Si', 'No') ")
                .append("FROM productos ")
                .append("WHERE idcategoria = ? ")
                .append("AND ").append(findCol).append(" LIKE ? ")
                .append("ORDER BY ").append(orderBy).append(" ")
                .append("LIMIT ?, ?")
                .toString();

        String sql02 = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM productos ")
                .append("WHERE idcategoria = ? ")
                .append("AND ").append(findCol).append(" LIKE ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps01 = cn.prepareStatement(sql01); PreparedStatement ps02 = cn.prepareStatement(sql02)) {

            ps01.setInt(1, idcategoria);
            ps01.setString(2, "%" + findData + "%");
            ps01.setInt(3, numPag * filsXpag);
            ps01.setInt(4, filsXpag);

            ps02.setInt(1, idcategoria);
            ps02.setString(2, "%" + findData + "%");

            try (ResultSet rs01 = ps01.executeQuery(); ResultSet rs02 = ps02.executeQuery()) {

                List<Object[]> list = new ArrayList<>();

                while (rs01.next()) {
                    Object[] fil = new Object[5];

                    fil[0] = rs01.getInt(1);
                    fil[1] = rs01.getString(2);
                    fil[2] = rs01.getString(3);
                    fil[3] = rs01.getString(4);

                    list.add(fil);
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
    public String productosIns(Productos productos) {
        message = null;
        String sql = new StringBuilder()
                .append("INSERT INTO productos(")
                .append("idcategoria,")
                .append("producto,")
                .append("precio,")
                .append("fotopath,")
                .append("estado ")
                .append(") VALUES(?, ?, ?, ?, ?)")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, productos.getIdcategoria());
            ps.setString(2, productos.getProducto());
            ps.setDouble(3, productos.getPrecio());
            ps.setString(4, productos.getFotopath());
            ps.setString(5, productos.getEstado());

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException e) {
            message = e.getMessage();

            if (message.toUpperCase().contains("DUPLICATE")) {
                message = "Producto ya está registrado";
            }
        }

        return message;
    }

    @Override
    public String productosUpd(Productos productos) {
        message = null;
        String sql = new StringBuilder()
                .append("UPDATE productos SET ")
                .append("idcategoria = ?,")
                .append("producto = ?,")
                .append("precio = ?,")
                .append("fotopath = ?,")
                .append("estado = ? ")
                .append("WHERE idproducto = ?")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, productos.getIdcategoria());
            ps.setString(2, productos.getProducto());
            ps.setDouble(3, productos.getPrecio());
            ps.setString(4, productos.getFotopath());
            ps.setString(5, productos.getEstado());
            ps.setInt(6, productos.getIdproducto());

            int ctos = ps.executeUpdate();
            if (ctos == 0) {
                message = "0 filas afectadas";
            }

        } catch (SQLException ex) {
            message = ex.getMessage();

            if (message.toUpperCase().contains("DUPLICATE")) {
                message = "Producto ya está registrado";
            }
        }

        return message;
    }

    @Override
    public String productosDel(List<Integer> list) {
        message = null;
        String sql = "DELETE FROM productos WHERE idproducto = ?";

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            cn.setAutoCommit(false);
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

            cn.setAutoCommit(true);

        } catch (SQLException e) {
            message = e.getMessage();
        }

        return message;
    }

    @Override
    public List<Object[]> productosQry(Integer idcategoria) {
        message = null;
        List<Object[]> list = null;

        String sql = new StringBuilder()
                .append("SELECT ")
                .append("idproducto,")
                .append("idcategoria,")
                .append("producto,")
                .append("FORMAT(precio, 2),")
                .append("fotopath,")
                .append("estado ")
                .append("FROM productos ")
                .append("WHERE idcategoria = ? ")
                .append("AND estado = '1' ")
                .append("ORDER BY producto")
                .toString();

        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idcategoria);

            try (ResultSet rs = ps.executeQuery()) {

                list = new ArrayList<>();

                while (rs.next()) {
                    Object[] fil = new Object[6];

                    fil[0] = rs.getInt(1);
                    fil[1] = rs.getInt(2);
                    fil[2] = rs.getString(3);
                    fil[3] = rs.getString(4);
                    fil[4] = rs.getString(5);
                    fil[5] = rs.getString(6);

                    list.add(fil);
                }

            } catch (SQLException e) {
                message = e.getMessage();
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
