package dao;

import dto.Usuarios;
import java.util.List;
import java.util.Map;

public interface DaoUsuarios extends Dao {

    public Map<String, Object> usuariosQry(Integer numPag, Integer filsXpag, String findCol, String findData, String orderBy);

    public String usuariosIns(Usuarios usuarios);

    public String usuariosDel(List<Integer> list);

    public Usuarios usuariosGet(Integer idusuario);

    public String usuariosUpd(Usuarios usuarios);

    public String changePassword(Integer idusuario, String newPassword);
}
