/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlosrodf
 */
public class dbConn {

    private final String url;
    private final String username;
    private final String password;

    public dbConn() {
        this.url = "jdbc:mysql://ec2-54-186-237-148.us-west-2.compute.amazonaws.com:3306/mydb";
        this.username = "*****";
        this.password = "*****";
    }

    protected Connection getConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            return null;
        }

        try {
            System.out.println("1");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("2");
            return conn;
        } catch (Exception e) {
            System.out.println("No se pudo crear la conexion a la DB\n" + e);
            return null;
        }
    }

    public int inscribirCiudadano(String n1, String n2, String a1, String a2, String fecha, String gen, String pais, String dpto, String mun) throws SQLException {
        Connection conn = getConexion();
        if (conn != null) {
            generarInscripcion();
            PreparedStatement query = conn.prepareStatement("INSERT INTO ciudadano"
                    + "(primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,fecha_nac,genero,pais,departamento,municipio,estado) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?);");

            query.setString(1, n1);
            query.setString(2, n2);
            query.setString(3, a1);
            query.setString(4, a2);
            query.setString(5, fecha);
            query.setString(6, gen);
            query.setString(7, pais);
            query.setString(8, dpto);
            query.setString(9, mun);
            query.setInt(10, 2);
            int r = query.executeUpdate();
            conn.close();
            return r;
        } else {
            return -1;
        }
    }

    //CASI TERMINADO
    private int generarInscripcion() {
        String correlativo;
        String verificador;

        try {
            ejecutar("INSERT INTO correlativos VALUES(null);");
            ResultSet rs = ejecutarQuery("SELECT md5(inc) FROM correlativos ORDER BY inc DESC LIMIT 1;");
            correlativo = rs.getString("inc");

            ejecutar("INSERT INTO verificadores VALUES(null);");
            rs = ejecutarQuery("SELECT md5(inc) FROM verificadores ORDER BY inc DESC LIMIT 1;");
            verificador = rs.getString("inc");

            Connection conn = getConexion();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO certificados(correlativo,verificador,tipo,valido,contenido) VALUES('?','?',1,1,'?')");
            ps.setString(1, correlativo);
            ps.setString(2, verificador);
            ps.setString(3, "Proximamente en cines...");
            ps.executeUpdate();

            rs = ejecutarQuery("SELECT idcertificado "
                    + "FROM certificado "
                    + "WHERE correlativo = ? "
                    + "AND verificador = ?");
            
            return rs.getInt("idcertificado");

        } catch (SQLException ex) {
            Logger.getLogger(dbConn.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    //SIN RESULTADO
    public void ejecutar(String sentencia) throws SQLException {
        Connection conn = getConexion();
        conn.prepareStatement(sentencia).execute();
        conn.close();
    }

    //CON RESULTADO
    public ResultSet ejecutarQuery(String query) throws SQLException {
        Connection conn = getConexion();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        conn.close();
        return rs;
    }
}
