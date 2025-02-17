package datos;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Conexion {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/scaicfe?SSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConection() {
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al establecer la conexión: " + e.getMessage());
            throw new RuntimeException("Error al establecer la conexión.", e);
        }
    }

    public static void close(ResultSet rs) throws SQLException {
        rs.close();
    }

    public static void close(Statement smtm) throws SQLException {
        smtm.close();
    }

    public static void close(PreparedStatement smtm) throws SQLException {
        smtm.close();
    }

    public static void close(Connection conn) throws SQLException {
        conn.close();
    }

    public static boolean validarVisitante(String numeroIdentificacion) {
        try (Connection connection = getConection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM visitantes WHERE numeroDocumento=?")) {
            statement.setString(1, numeroIdentificacion);
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Consulta ejecutada con éxito");
                if (resultSet.next()) {
                    System.out.println("Visitante encontrado");
                    return true;

                } else {
                    System.out.println("Visitante no encontrado");
                    return true;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return false;
        }

    }

    public static boolean validarUsuario(String numeroIdentificacion, String contrasena) {
        try (Connection connection = getConection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM usuarios WHERE numeroIdentificacion=? AND contrasena = ?")) {
            statement.setString(1, numeroIdentificacion);
            statement.setString(2, contrasena);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Devuelve true si hay al menos un resultado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean esVisi(String numeroDocumento) {
        try (Connection connectionn = getConection();
             PreparedStatement statement = connectionn.prepareStatement("SELECT numeroDocumento FROM visitantes WHERE numeroDocumento =?")) {
            statement.setString(1, numeroDocumento);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    System.out.println("Visitante encontrado");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean esAdmin(String usuario) {
        try (Connection connection = getConection();
             PreparedStatement statement = connection.prepareStatement("SELECT rol FROM usuarios WHERE numeroIdentificacion = ? ")) {

            statement.setString(1, usuario);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String rol = resultSet.getString("rol");
                    return "administrador".equals(rol); // Ajusta el valor según tu diseño de base de datos
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Si no se encuentra el usuario o hay un error, devuelve false
    }

    public static boolean registrarVisitante(String primerNombre, String segundoNombre, String primerApellido,
                                             String segundoApellido, String numeroDocumento, String genero, String grado, String unidad) throws SQLException {
        try (Connection conection = getConection()) {
            String query = "INSERT INTO visitantes (primerNombre, segundoNombre, primerApellido, segundoApellido, " +
                    "numeroDocumento, genero, grado, unidad) VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = conection.prepareStatement(query)) {
                statement.setString(1, primerNombre);
                statement.setString(2, segundoNombre);
                statement.setString(3, primerApellido);
                statement.setString(4, segundoApellido);
                statement.setString(5, numeroDocumento);
                statement.setString(6, genero);
                statement.setString(7, grado);
                statement.setString(8, unidad);

                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error algopasa en este punto");
                return false;
            }


        }

    }
    public static boolean registrarIngreso(String primmerNombre, String primerApellido, String segundoApellido
    , String unidad, String tipoVehiculo, String placaVehiculo, String colorVehiculo, LocalDate fechaIngreso,LocalTime horaIngreso , LocalTime horaSalida,
            String segundoNombre, boolean estado, LocalDate fechaSalida)throws SQLException{
        try (Connection conection = getConection()){
            String query = "INSERT INTO ingreso(primerNombre,  primerApellido, segundoApellido, " +
                    "unidad, tipoVehiculo, placaVehiculo, colorVehiculo,  fechaIngreso,  horaIngreso, horaSalida,segundoNombre, estado, fechaSalida )" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try(PreparedStatement statement = conection.prepareStatement(query)){
                System.out.println("PreparedStatement creado correctamente.");
                statement.setString(1, primmerNombre);
                statement.setString(2,primerApellido);
                statement.setString(3,segundoApellido);
                statement.setString(4, unidad);
                statement.setString(5, tipoVehiculo);
                statement.setString(6, placaVehiculo);
                statement.setString(7, colorVehiculo);
                statement.setDate(8,Date.valueOf(fechaIngreso));
                statement.setTime(9, Time.valueOf(horaIngreso));
                statement.setTime(10, Time.valueOf(horaSalida));
                statement.setString(11, segundoNombre);
                statement.setBoolean(12,estado);
                statement.setDate(13, Date.valueOf(fechaSalida));
                System.out.println("Parámetros establecidos correctamente.");
                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;


            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("algo esta pasando");
                return false;

            }
        }


    }
    public static ResultSet obtenerInformacionVisitante (String numeroDocumento){
        try(Connection connection = getConection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM visitantes WHERE numeroDocumento=?")){
            statement.setString(1, numeroDocumento);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        }catch (SQLException e){
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }


    }
}
