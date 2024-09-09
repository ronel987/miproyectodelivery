package dao;

import dto.Productos;
import java.util.List;
import java.util.Map;

public interface DaoProductos extends Dao {

    public Map<String, Object> productosQry(Integer idcategoria,
            Integer numPag, Integer filsXpag, String findCol, String findData, String orderBy);
    
    public List<Object[]> productosQry(Integer idcategoria);

    public String productosIns(Productos productos);
    
    public Productos productosGet(Integer idproducto);

    public String productosUpd(Productos productos);

    public String productosDel(List<Integer> list);
    
    public List<Object[]> productosCbo(Integer idcategoria);
    
}
