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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jose
 */
public class MatrimonioQuery {
    
    String dpi_esposo,dpi_esposa;
    protected dbConn c;
    
    public MatrimonioQuery(String dpi_esposo,String dpi_esposa){
        this.dpi_esposa=dpi_esposa;
        this.dpi_esposo=dpi_esposo;
        c=new dbConn();      
    }
    
    public int inscribir() throws SQLException{
        
        Connection conexion = c.getConexion();
        if(conexion!=null){
            List<String[]> datos= new ArrayList<>();
            datos.add(new String[]{"DPI Esposo",dpi_esposo+""});
            datos.add(new String[]{"DPI Esposa",dpi_esposa+""});
            datos.add(new String[]{"Relacion ","Matrimonio"});
            
            int dpia = this.obtener_id(dpi_esposa);
            int dpio = this.obtener_id(dpi_esposo);
            if(dpia==-1 || dpio == -1){
                return -1;
            }
            String certificado=c.generarCertificado(2, datos)[0];           
            
            PreparedStatement query = conexion.prepareStatement("INSERT INTO matrimonio "
                    + "(esposo,esposa,certificado) "
                    + "VALUES (?,?,?);");            
            query.setInt(1, dpia);
            query.setInt(2, dpio);                        
            query.setInt(3, Integer.parseInt(certificado));   
            return query.executeUpdate();
        }else{
            return -1;
        }           
    }
    
    protected int obtener_id(String dpi){
        String q ="SELECT idciudadano "
                + "FROM ciudadano "
                + "WHERE DPI = '" + dpi + "';";
        try {
            ResultSet rs = c.ejecutarQuery(q);
            rs.next();
            return rs.getInt("idciudadano");
        } catch (SQLException ex) {
            Logger.getLogger(MatrimonioQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public String getCertificado() throws SQLException {
        dbConn c=new dbConn();      
        Connection conexion = c.getConexion();
        if(conexion!=null){            
            int certificado=c.ejecutarQueryForInt("Select certificado "
                    + "FROM matrimonio where esposa="+obtener_id(dpi_esposa)
                    +" and esposo="+obtener_id(dpi_esposo)+";","certificado");
            String contenido = c.ejecutarQueryForString("SELECT contenido FROM certificado where idcertificado="+certificado, "contenido");            
            return contenido;
        }else{
            return null;
        }   
    }
    
   

    public int divorciar() throws SQLException {
        dbConn c=new dbConn();      
        Connection conexion = c.getConexion();
        if(conexion!=null){            
            int certificado=c.ejecutarQueryForInt("Select certificado "
                    + "FROM matrimonio where esposa="+obtener_id(dpi_esposa)
                    +" and esposo="+obtener_id(dpi_esposo)+";","certificado");
            PreparedStatement query = conexion.prepareStatement("UPDATE certificado set valido=false where idcertificado=?");
            query.setInt(1, certificado);                       
            return query.executeUpdate();
        }else{
            return -1;
        }   
    }
        
    
}
