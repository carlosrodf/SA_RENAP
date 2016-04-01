/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
