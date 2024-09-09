package dao;

import dto.Categorias;
import java.util.List;
import java.util.Map;

public interface DaoCategorias extends Dao {

    public Map<String, Object> categoriasQry(
            Integer numPag, Integer filsXpag, String findCol, String findData, String orderBy);
    
    public List<Object[]> categoriasQry();

    public String categoriasIns(Categorias categorias);

    public Categorias categoriasGet(Integer idcategoria);

    public String categoriasUpd(Categorias categorias);

    public String categoriasDel(List<Integer> list);
    
    public List<Object[]> categoriasCbo();
    
}
