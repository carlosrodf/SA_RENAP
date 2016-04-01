/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user1
 */
public class ServiciosJulio {

    private Connection conn;
    private dbConn db;

    public ServiciosJulio() {
        this.db = new dbConn();
        this.conn = db.getConexion();
    }

    public int reconocerHijo(String dpi_padre, String correlativo, String verificador) throws SQLException {
        if (conn != null) {
            int predpi = db.getIdCertificado(correlativo, verificador);
            PreparedStatement query = conn.prepareStatement("SELECT genero FROM ciudadano WHERE dpi = ?;");

            query.setString(1, dpi_padre);
            ResultSet resultado = query.executeQuery();
            if ((char) resultado.getInt(1) == 'M') {
                query = conn.prepareStatement("UPDATE ciudadano SET padre = ? WHERE predpi = ?");
                query.setString(1, dpi_padre);
                query.setInt(2, predpi);
                int r = query.executeUpdate();
                return r;
            } else {
                query = conn.prepareStatement("UPDATE ciudadano SET madre = ? WHERE predpi = ?");
                query.setString(1, dpi_padre);
                query.setInt(2, predpi);
                int r = query.executeUpdate();
                return r;
            }
        } else {
            return -1;
        }
    }

    public int reconocerHijo(String dpi_padre, String dpi_madre, String correlativo, String verificador) throws SQLException {
        if (conn != null) {
             int predpi = db.getIdCertificado(correlativo, verificador);
                PreparedStatement query = conn.prepareStatement("UPDATE ciudadano SET padre = ?, madre = ? WHERE predpi = ?");
                query.setString(1, dpi_padre);
                query.setString(2, dpi_madre);
                query.setInt(3, predpi);
                int r = query.executeUpdate();
                return r;
            
        } else {
            return -1;
        }
    }

    public int declararBancarrota(String dpi) throws SQLException {
        if (conn != null) {
            PreparedStatement query = conn.prepareStatement("UPDATE ciudadano SET estado = 2 WHERE dpi = ?;");

            query.setString(1, dpi);
            int r = query.executeUpdate();
            return r;
        } else {
            return -1;
        }
    }
    public int rehabilitarBancarrota(String dpi) throws SQLException {
        if (conn != null) {
            PreparedStatement query = conn.prepareStatement("UPDATE ciudadano SET estado = 1 WHERE dpi = ?;");

            query.setString(1, dpi);
            int r = query.executeUpdate();
            return r;
        } else {
            return -1;
        }
    }
    
    public int notificarDefuncion(String dpi, String fecha, String causamuerte) throws SQLException{
        if (conn != null) {
            List<String[]> html = new ArrayList<>();
        html.add(new String[]{"Fecha", fecha});
        html.add(new String[]{"Causa de Muerte", causamuerte});
        db.generarCertificado(3,html);
            PreparedStatement query = conn.prepareStatement("UPDATE ciudadano SET estado = 3 WHERE dpi = ?;");

            query.setString(1, dpi);
            int r = query.executeUpdate();
            return r;
        } else {
            return -1;
        }
    }
    public int notificarDefuncion(String correlativo, String verificador, String fecha, String causamuerte) throws SQLException{
        if (conn != null) {
            List<String[]> html = new ArrayList<>();
        html.add(new String[]{"Fecha", fecha});
        html.add(new String[]{"Causa de Muerte", causamuerte});
        int predpi = db.getIdCertificado(correlativo, verificador);
        db.generarCertificado(3,html);
            PreparedStatement query = conn.prepareStatement("UPDATE ciudadano SET estado = 3 WHERE predpi = ?;");

            query.setInt(1, predpi);
            int r = query.executeUpdate();
            return r;
        } else {
            return -1;
        }
    }
    public String tarifario(){
        return "<table>"
                + "<tr><th>POR EMISIÓN DE CERTIFICACIÓN DE INSCRIPCIÓN</th><th>TARIFA</th></tr>"
                + "<tr><td>De Nacimiento</td><td>Q15.00</td></tr>"
                + "<tr><td>DeExtranjero Domiciliado</td><td>Q500.00</td></tr>"
                + "</table>"
                + "<table>"
                + "<tr><th>POR EMISIÓN DE</th><th>TARIFA</th></tr>"
                + "<tr><td>Documento Personal de Identificación</td><td>Q85.00</td></tr>"
                + "</table>"
                + "<table>"
                + "<tr><th colspan = 7>POR SERVICIOS ELECTRÓNICOS DE CONSULTA DE INFORMACIÓN DE IDENTIDAD</th></tr>"
                + "<tr><th rowspan = 2>Cantidad de Consultas</th><th colspan = 6>Tarifa de las modalidades del servicio</th></tr>"
                + "<tr><th>CUI</th><th>Nombre</th><th>Huella 1a1</th><th>Huella 1aN</th><th>FRS 1a1</th><th>FRS 1aN</th></tr>"
                + "<tr><th>De 1 a 250,000</th><td>Q5.00</td><td>Q5.00</td><td>Q10.00</td><td>Q15.00</td><td>Q10.00</td><td>Q15.00</td></tr>"
                + "<tr><th>De 250,001 a 500,000</th><td>Q4.00</td><td>Q4.00</td><td>Q8.00</td><td>Q13.00</td><td>Q8.00</td><td>Q13.00</td></tr>"
                + "</table>";
    }
}
