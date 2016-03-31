/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author jose
 */
public class MatrimonioQuery {
    
    int dpi_esposo,dpi_esposa;
    
    public MatrimonioQuery(int dpi_esposo,int dpi_esposa){
        this.dpi_esposa=dpi_esposa;
        this.dpi_esposo=dpi_esposo;
    }
    
    public int inscribir() throws SQLException{
        Connection conexion = new dbConn().getConexion();
        if(conexion!=null){
            PreparedStatement query = conexion.prepareStatement("INSERT INTO matrimonio"
                    + "(esposo,esposa) "
                    + "VALUES (?,?);");
            query.setInt(1, dpi_esposa);
            query.setInt(2, dpi_esposo);                        
            return query.executeUpdate();
        }else{
            return -1;
        }        
    }
    
}
