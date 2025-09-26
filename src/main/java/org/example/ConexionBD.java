package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class ConexionBD {
    private static HikariDataSource dataSource;
    private static boolean inicializado = false;

    // Configuración de la base de datos basada en tu setup
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "atletas_guatemala";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "roberto123";


    private static synchronized void configurarDataSource() {
        if (inicializado && dataSource != null && !dataSource.isClosed()) {
            return; // Ya está configurado y activo
        }

        try {
            HikariConfig config = new HikariConfig();

            // URL de conexión a MariaDB
            config.setJdbcUrl("jdbc:mariadb://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
            config.setUsername(DB_USER);
            config.setPassword(DB_PASSWORD);

            // Configuraciones del pool de conexiones
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300000); // 5 minutos
            config.setConnectionTimeout(20000); // 20 segundos
            config.setMaxLifetime(1200000); // 20 minutos

            // Configuraciones adicionales para evitar el cierre prematuro
            config.setLeakDetectionThreshold(60000); // 1 minuto
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("useUnicode", "true");

            // Crear el nuevo DataSource
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }

            dataSource = new HikariDataSource(config);
            inicializado = true;

            System.out.println(" Pool de conexiones MariaDB configurado correctamente");

        } catch (Exception e) {
            System.err.println(" Error al configurar pool de conexiones: " + e.getMessage());
            inicializado = false;
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        // Verificar si necesita reconfiguración
        if (!inicializado || dataSource == null || dataSource.isClosed()) {
            configurarDataSource();
        }

        if (dataSource == null) {
            throw new SQLException("DataSource no está configurado");
        }

        return dataSource.getConnection();
    }

    public static synchronized void cerrarDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println(" Pool de conexiones cerrado");
        }
        inicializado = false;
        dataSource = null;
    }

    public static synchronized void reiniciarDataSource() {
        cerrarDataSource();
        configurarDataSource();
    }


    public static boolean isPoolActivo() {
        return inicializado && dataSource != null && !dataSource.isClosed();
    }

    public static boolean probarConexion() {
        try (Connection connection = obtenerConexion()) {
            System.out.println("🇬🇹 Conexión exitosa a MariaDB: " +
                    connection.getMetaData().getDatabaseProductName() +
                    " " + connection.getMetaData().getDatabaseProductVersion());
            return true;
        } catch (SQLException e) {
            System.err.println(" Error al conectar con MariaDB: " + e.getMessage());
            return false;
        }
    }
}