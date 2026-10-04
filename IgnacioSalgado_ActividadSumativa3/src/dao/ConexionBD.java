package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/speedfast_db?useSSL=false&serverTimezone=UTC";

    private static final String USUARIO = "root";
    private static final String PASSWORD = "Mono7988";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}