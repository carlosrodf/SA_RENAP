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
    
    private final Connection conn;

    public dbConn() {
        this.url = "jdbc:mysql://ec2-54-186-237-148.us-west-2.compute.amazonaws.com:3306/mydb";
        this.username = "*****";
        this.password = "*****";
        this.conn = getConexion();
    }

    protected Connection getConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            return null;
        }

        try {
            Connection aux = DriverManager.getConnection(url, username, password);
            return aux;
        } catch (Exception e) {
            System.out.println("No se pudo crear la conexion a la DB\n" + e);
            return null;
        }
    }

    public int inscribirCiudadano(String n1, String n2, String a1, String a2, String fecha, String gen, String pais, String dpto, String mun) throws SQLException {

        if (conn != null) {
            int predpi = generarInscripcion();
            PreparedStatement query = conn.prepareStatement("INSERT INTO ciudadano"
                    + "(predpi,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,fecha_nac,genero,pais,departamento,municipio,estado) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?);");

            query.setInt(1, predpi);
            query.setString(2, n1);
            query.setString(3, n2);
            query.setString(4, a1);
            query.setString(5, a2);
            query.setString(6, fecha);
            query.setString(7, gen);
            query.setString(8, pais);
            query.setString(9, dpto);
            query.setString(10, mun);
            query.setInt(11, 2);
            int r = query.executeUpdate();
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
            correlativo = ejecutarQueryForString("SELECT md5(inc) FROM correlativos ORDER BY inc DESC LIMIT 1;","md5(inc)");

            ejecutar("INSERT INTO verificadores VALUES(null);");
            verificador = ejecutarQueryForString("SELECT md5(inc) FROM verificadores ORDER BY inc DESC LIMIT 1;","md5(inc)");

            PreparedStatement ps = conn.prepareStatement("INSERT INTO certificado(correlativo,verificador,tipo,valido,contenido) VALUES(?,?,1,1,?)");
            ps.setString(1, correlativo);
            ps.setString(2, verificador);
            ps.setString(3, "Proximamente en cines...");
            ps.executeUpdate();

            return ejecutarQueryForInt("SELECT idcertificado "
                    + "FROM certificado " 
                    + "WHERE correlativo = '" + correlativo + "' "
                    + "AND verificador = '" + verificador + "';", "idcertificado");

        } catch (SQLException ex) {
            Logger.getLogger(dbConn.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    //SIN RESULTADO
    public void ejecutar(String sentencia) throws SQLException {
        conn.prepareStatement(sentencia).execute();
    }

    //CON RESULTADO STRING
    public String ejecutarQueryForString(String query, String paramName) throws SQLException {

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return rs.getString(paramName);
    }

    //CON RESULTADO STRING
    public int ejecutarQueryForInt(String query, String paramName) throws SQLException {

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        return rs.getInt(paramName);
    }
}
