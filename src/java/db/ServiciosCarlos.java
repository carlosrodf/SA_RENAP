/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlosrodf
 */
public class ServiciosCarlos {

    private Connection conn;
    private dbConn db;

    public ServiciosCarlos() {
        this.db = new dbConn();
        this.conn = db.getConexion();
    }

    public int inscribirCiudadano(String n1, String n2, String a1, String a2, String fecha, String gen, String pais, String dpto, String mun) throws SQLException {

        List<String[]> html = new ArrayList<>();
        html.add(new String[]{"Nombres", n1 + " " + n2});
        html.add(new String[]{"Apellidos", a1 + " " + a2});
        html.add(new String[]{"Fecha de nacimiento", fecha});

        if (conn != null) {
            String[] datos = this.db.generarCertificado(1, html);
            PreparedStatement query = conn.prepareStatement("INSERT INTO ciudadano"
                    + "(predpi,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,fecha_nac,genero,pais,departamento,municipio,estado) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?);");

            query.setInt(1, Integer.parseInt(datos[0]));
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

    public String obtenerCertificadoNacimiento(String correlativo, String verificador) {
        String query = "SELECT COUNT(idcertificado) AS valido "
                + "FROM certificado "
                + "WHERE correlativo = '" + correlativo + "' "
                + "AND verificador = '" + verificador + "';";

        try {
            int valido = this.db.ejecutarQueryForInt(query, "valido");
            if (valido == 1) {
                query = "SELECT contenido "
                        + "FROM certificado "
                        + "WHERE correlativo = '" + correlativo + "' "
                        + "AND verificador = '" + verificador + "';";
                return this.db.ejecutarQueryForString(query, "contenido");
            } else {
                return "No valido";
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosCarlos.class.getName()).log(Level.SEVERE, null, ex);
            return "No valido";
        }
    }
    
    public boolean validarCertificadoNacimiento(String correlativo, String verificador){
        String query = "SELECT COUNT(idcertificado) AS valido "
                + "FROM certificado "
                + "WHERE correlativo = '" + correlativo + "' "
                + "AND verificador = '" + verificador + "';";

        try {
            int valido = this.db.ejecutarQueryForInt(query, "valido");
            return valido == 1;
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosCarlos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public String emisionDPI(String correlativo, String verificador){
        String dpi;
        try {
            dpi = this.db.getNextDPI();
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosCarlos.class.getName()).log(Level.SEVERE, null, ex);
            return "Datos no validos.";
        }
        int predpi = this.db.getIdCertificado(correlativo, verificador);
        
        String query = "UPDATE ciudadano "
                + "SET DPI = '"+dpi+"' "
                + "WHERE predpi = " + predpi + ";";
        
        try {
            this.db.ejecutar(query);
        } catch (SQLException ex) {
            return "Datos no validos.";
        }
        return dpi;
    }

}
