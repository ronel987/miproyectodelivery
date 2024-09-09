package dao;

import dto.Usuarios;

public interface DaoAccesos extends Dao {

    public Usuarios autentica(String usuario, String password);
}
