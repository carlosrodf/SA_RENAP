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
import java.util.List;
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
        this.username = "***********";
        this.password = "***********";
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

    // RETORNA UN RESULT SET
    public ResultSet ejecutarQuery(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    // TIPOS DE CERTIFICADO (NACIMIENTO = 1, MATRIMONIO = 2, DEFUNCION = 3)
    // RETORNA UN ARRAY DE STRINGS CON [id,correlativo,verificador] DEL CERTIFICADO CREADO
    public String[] generarCertificado(int tipo, List<String[]> datos) {
        String correlativo;
        String verificador;
        int id;

        try {
            ejecutar("INSERT INTO correlativos VALUES(null);");
            correlativo = ejecutarQueryForString("SELECT md5(inc) FROM correlativos ORDER BY inc DESC LIMIT 1;", "md5(inc)");

            ejecutar("INSERT INTO verificadores VALUES(null);");
            verificador = ejecutarQueryForString("SELECT md5(inc) FROM verificadores ORDER BY inc DESC LIMIT 1;", "md5(inc)");

            datos.add(new String[]{"correlativo",correlativo});
            datos.add(new String[]{"verificador",verificador});
            
            PreparedStatement ps = conn.prepareStatement("INSERT INTO certificado(correlativo,verificador,tipo,valido,contenido) VALUES(?,?,1,1,?)");
            ps.setString(1, correlativo);
            ps.setString(2, verificador);
            ps.setString(3, generarCertificadoHTML(datos));
            ps.executeUpdate();

            id = ejecutarQueryForInt("SELECT idcertificado "
                    + "FROM certificado "
                    + "WHERE correlativo = '" + correlativo + "' "
                    + "AND verificador = '" + verificador + "';", "idcertificado");

            String[] retorno = new String[3];
            retorno[0] = id + "";
            retorno[1] = correlativo;
            retorno[2] = verificador;

            return retorno;

        } catch (SQLException ex) {
            Logger.getLogger(dbConn.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    // RECIBE UNA LISTA DE STRING[2], POSICION 0 NOMBRE DEL CAMPO, POSICION 1 VALOR
    // SE GUARRDA EN EL CAMPO 'contenido' DE LA ENTIDAD CERTIFICADO
    public String generarCertificadoHTML(List<String[]> datos) {
        String cert = "<h1>RENAP</h1>\n";
        cert += "<table>\n";
        
        for(String[] item : datos){
            cert += "<tr><td>"+item[0]+"</td><td>"+item[1]+"</td></tr>\n";
        }
        
        cert += "</table>\n";
        
        return cert;
    }
}
