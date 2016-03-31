/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import db.MatrimonioQuery;
import db.dbConn;
import java.sql.SQLException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author carlosrodf
 */
@WebService(serviceName = "RENAP")
public class RENAP {

    /**
     * Inscripcion de un ciudadano al registro
     *
     * @param dpi
     * @param nombre1
     * @param nombre2
     * @param apellido1
     * @param apellido2
     * @param fechanac
     * @param genero
     * @param pais
     * @param dpto
     * @param municipio
     * @return
     */
    @WebMethod(operationName = "Inscribirciudadano")
    public boolean inscribir(@WebParam(name = "primer_nombre") String nombre1,
            @WebParam(name = "segundo_nombre") String nombre2,
            @WebParam(name = "primer_apellido") String apellido1,
            @WebParam(name = "segundo_apellido") String apellido2,
            @WebParam(name = "fecha_nacimiento") String fechanac,
            @WebParam(name = "genero") String genero,
            @WebParam(name = "pais") String pais,
            @WebParam(name = "departamento") String dpto,
            @WebParam(name = "municipio") String municipio) {

        dbConn inf = new dbConn();
        try {
            int res =  inf.inscribirCiudadano(nombre1, nombre2, apellido1, apellido2, fechanac, genero, pais, dpto, municipio);
            if(res > 0){
                return true;
            }else{
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    @WebMethod(operationName = "InscribirMatrimonio")
    public boolean inscribirMatrimonio(@WebParam(name = "dpi_esposo") int dpi_esposo,
            @WebParam(name = "dpi_esposa") int dpi_esposa) {

        MatrimonioQuery m = new MatrimonioQuery(dpi_esposo,dpi_esposa);
        try {
            int res =  m.inscribir();
            if(res > 0) 
                return true;            
            else 
                return false;            
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
}
