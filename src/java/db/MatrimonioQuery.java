/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author jose
 */
public class MatrimonioQuery {
    
    String dpi_esposo,dpi_esposa;
    
    public MatrimonioQuery(String dpi_esposo,String dpi_esposa){
        this.dpi_esposa=dpi_esposa;
        this.dpi_esposo=dpi_esposo;
    }
    
    public int inscribir() throws SQLException{
        dbConn c=new dbConn();      
        Connection conexion = c.getConexion();
        if(conexion!=null){
            List<String[]> datos= new ArrayList<>();
            datos.add(new String[]{"DPI Esposo",dpi_esposo+""});
            datos.add(new String[]{"DPI Esposa",dpi_esposa+""});
            String certificado=c.generarCertificado(2, datos)[0];
            PreparedStatement query = conexion.prepareStatement("INSERT INTO matrimonio"
                    + "(esposo,esposa,certificado) "
                    + "VALUES (?,?,?);");
            query.setString(1, dpi_esposa);
            query.setString(2, dpi_esposo);                        
            query.setString(3, certificado);                        
            return query.executeUpdate();
        }else{
            return -1;
        }        
    }

    public String getCertificado() throws SQLException {
        dbConn c=new dbConn();      
        Connection conexion = c.getConexion();
        if(conexion!=null){            
            int certificado=c.ejecutarQueryForInt("Select certificado FROM matrimonio where esposa='"+dpi_esposa+"' and esposo='"+dpi_esposo+"'","certificado");
            ResultSet r =c.ejecutarQuery("SELECT contenido FROM certificado where idcertificado="+certificado);
            return r.getString(0);
        }else{
            return null;
        }   
    }
        
    
}
