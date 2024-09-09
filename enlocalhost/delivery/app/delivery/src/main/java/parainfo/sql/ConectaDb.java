package parainfo.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;

public class ConectaDb {

    public Connection getConnection() throws SQLException {
        Connection cn = null;

        try {
            Class.forName(driver);
            cn = DriverManager.getConnection(url,user,password);

        } catch (ClassNotFoundException ex) {

            throw new SQLException(ex.getMessage());
        }

        return cn;
    }

    private final String url = "jdbc:mysql://173.249.20.156:3306/delivery?serverTimezone=America/Lima";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String user = "root"; // root codificado cm9vdA==
    private final String password = "@1q2w3e4r5t6y."; // mysql codificado bXlzcWw=
    

}