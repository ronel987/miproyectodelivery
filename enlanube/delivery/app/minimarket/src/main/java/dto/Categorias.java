package dto;

public class Categorias {

    private Integer idcategoria;
    private String categoria;
    private String descripcion;
    private String fotopath;

    public Categorias() {
    }

    public Integer getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(Integer idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotopath() {
        return fotopath;
    }

    public void setFotopath(String fotopath) {
        this.fotopath = fotopath;
    }

}
