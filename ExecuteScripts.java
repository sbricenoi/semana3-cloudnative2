import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class ExecuteScripts {
    public static void main(String[] args) {
        System.out.println("═".repeat(60));
        System.out.println("  EJECUTAR SCRIPTS EN ORACLE CLOUD");
        System.out.println("═".repeat(60));
        System.out.println();

        String user = "biblioteca";
        String password = "#Ng3naUQa*THhTd";
        String url = "jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)" +
                     "(address=(protocol=tcps)(port=1522)(host=adb.sa-santiago-1.oraclecloud.com))" +
                     "(connect_data=(service_name=g64afca1579a0d2_s58onuxcx4c1qxe9_high.adb.oraclecloud.com))" +
                     "(security=(ssl_server_dn_match=yes)))";

        System.setProperty("oracle.net.tns_admin", new File("wallet").getAbsolutePath());
        System.setProperty("oracle.net.wallet_location", "(SOURCE=(METHOD=FILE)(METHOD_DATA=(DIRECTORY=" + 
                          new File("wallet").getAbsolutePath() + ")))");
        System.setProperty("javax.net.ssl.trustStore", new File("wallet/truststore.jks").getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", "");
        System.setProperty("javax.net.ssl.keyStore", new File("wallet/keystore.jks").getAbsolutePath());
        System.setProperty("javax.net.ssl.keyStorePassword", "");

        System.out.println("👤 Usuario: " + user);
        System.out.println("🔌 Service: s58onuxcx4c1qxe9_high");
        System.out.println("📍 Wallet: wallet/");
        System.out.println();

        try {
            System.out.println("🔄 Conectando a Oracle Cloud...");
            Class.forName("oracle.jdbc.OracleDriver");
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexión exitosa\n");

            Statement stmt = conn.createStatement();
            
            System.out.println("📊 Verificando conexión...");
            ResultSet rs = stmt.executeQuery("SELECT 'Conectado a Oracle Cloud' FROM DUAL");
            if (rs.next()) {
                System.out.println("   " + rs.getString(1));
            }
            System.out.println();

            ejecutarScript(conn, "database/schema.sql");
            ejecutarScript(conn, "database/seed.sql");

            System.out.println("\n" + "═".repeat(60));
            System.out.println("  VERIFICACIÓN DE DATOS");
            System.out.println("═".repeat(60));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            if (rs.next()) {
                System.out.println("  📊 Usuarios en BD: " + rs.getInt(1));
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM libros");
            if (rs.next()) {
                System.out.println("  📚 Libros en BD: " + rs.getInt(1));
            }

            rs = stmt.executeQuery("SELECT COUNT(*) FROM prestamos");
            if (rs.next()) {
                System.out.println("  📋 Préstamos en BD: " + rs.getInt(1));
            }

            System.out.println("\n  Ejemplo de datos:");
            rs = stmt.executeQuery("SELECT nombre, apellido, email FROM usuarios WHERE ROWNUM <= 3");
            while (rs.next()) {
                System.out.println("    • " + rs.getString(1) + " " + rs.getString(2) + " - " + rs.getString(3));
            }

            conn.close();

            System.out.println("\n" + "═".repeat(60));
            System.out.println("  ✅ BASE DE DATOS CONFIGURADA Y LISTA");
            System.out.println("═".repeat(60));
            System.out.println("\nAhora puedes:");
            System.out.println("  1. Iniciar las funciones serverless: docker-compose up");
            System.out.println("  2. Probar las APIs con Postman");
            System.out.println("  3. Verificar integración completa");

        } catch (ClassNotFoundException e) {
            System.err.println("\n❌ Error: Driver JDBC de Oracle no encontrado");
            System.err.println("   Descarga ojdbc8.jar y agrégalo al classpath");
        } catch (SQLException e) {
            System.err.println("\n❌ Error de Oracle: " + e.getMessage());
            System.err.println("   Código: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void ejecutarScript(Connection conn, String archivo) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Ejecutando: " + archivo);
        System.out.println("=".repeat(60));

        try {
            String contenido = new String(Files.readAllBytes(Paths.get(archivo)));
            
            String[] partes = contenido.split("/");
            List<String> statements = new ArrayList<>();
            
            for (String parte : partes) {
                if (parte.trim().toUpperCase().contains("CREATE") && 
                    (parte.trim().toUpperCase().contains("TRIGGER") || 
                     parte.trim().toUpperCase().contains("PROCEDURE"))) {
                    statements.add(parte.trim());
                } else {
                    String[] sentencias = parte.split(";");
                    for (String sent : sentencias) {
                        String s = sent.trim();
                        if (!s.isEmpty() && !s.startsWith("--")) {
                            statements.add(s);
                        }
                    }
                }
            }

            Statement stmt = conn.createStatement();
            int exitosos = 0;
            int omitidos = 0;

            for (String statement : statements) {
                if (statement.isEmpty()) continue;

                try {
                    stmt.execute(statement);
                    exitosos++;
                    
                    if (statement.toUpperCase().contains("CREATE")) {
                        String[] palabras = statement.split("\\s+");
                        System.out.println("  ✓ " + palabras[0] + " " + palabras[1] + 
                                         (palabras.length > 2 ? " " + palabras[2] : ""));
                    } else if (statement.toUpperCase().contains("INSERT")) {
                        System.out.println("  ✓ INSERT ejecutado");
                    }
                    
                } catch (SQLException e) {
                    if (e.getErrorCode() == 955 || e.getErrorCode() == 1430) {
                        System.out.println("  ⚠ Objeto ya existe (omitiendo)");
                        omitidos++;
                    } else if (e.getErrorCode() == 1) {
                        System.out.println("  ⚠ Registro duplicado (omitiendo)");
                        omitidos++;
                    } else {
                        System.out.println("  ❌ Error: " + e.getMessage());
                    }
                }
            }

            conn.commit();
            System.out.println("\n✅ Completado: " + exitosos + " operaciones exitosas");
            if (omitidos > 0) {
                System.out.println("⚠️  " + omitidos + " objetos ya existían");
            }

        } catch (IOException e) {
            System.err.println("❌ Error al leer " + archivo + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error al ejecutar " + archivo + ": " + e.getMessage());
        }
    }
}
