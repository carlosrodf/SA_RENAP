/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import db.MatrimonioQuery;
import db.ServiciosCarlos;
import db.ServiciosJulio;
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
     * Inscribe un nuevo ciudadano al registro como un menor de edad.
     * Si la operacion es exitosa retorna un arreglo de tres posiciones,
     * en la primera posicion el resultado de la operacion, en la segunda posicion
     * el correlativo del ciudadano y en la tercera posicion el codigo verificador del
     * ciudadano. Estos codigos sirven para solicitar la emision de un DPI.
     * Si la operacion falla retorna un arreglo de una posicion.
     *
     * @param nombre1
     * @param nombre2
     * @param apellido1
     * @param apellido2
     * @param fechanac
     * @param genero
     * @param pais
     * @param dpto
     * @param municipio
     * @return Arreglo de tipo String[] con el resultado de la operacion
     */
    @WebMethod(operationName = "Inscribirciudadano")
    public String[] inscribirCiudadano(@WebParam(name = "primer_nombre") String nombre1,
            @WebParam(name = "segundo_nombre") String nombre2,
            @WebParam(name = "primer_apellido") String apellido1,
            @WebParam(name = "segundo_apellido") String apellido2,
            @WebParam(name = "fecha_nacimiento") String fechanac,
            @WebParam(name = "genero") String genero,
            @WebParam(name = "pais") String pais,
            @WebParam(name = "departamento") String dpto,
            @WebParam(name = "municipio") String municipio) {

        ServiciosCarlos inf = new ServiciosCarlos();
        try {
            return inf.inscribirCiudadano(nombre1, nombre2, apellido1, apellido2, fechanac, genero, pais, dpto, municipio);
        } catch (SQLException ex) {
            System.out.println(ex);
            return new String[]{"Resultado: Fallido"};
        }
    }

    /**
     * Retorna el certificado de nacimiento del ciudadano que tenga el correlativo
     * y verificador ingresados.
     * 
     * @param correlativo
     * @param verificador
     * @return Certificado de nacimiento del ciudadano
     */
    @WebMethod(operationName = "ObtenerCertificadoNacimiento")
    public String obtenerCertificadoNacimiento(@WebParam(name = "correlativo") String correlativo,
            @WebParam(name = "verificador") String verificador) {

        ServiciosCarlos inf = new ServiciosCarlos();
        return inf.obtenerCertificadoNacimiento(correlativo, verificador);
    }
    /**
     * Genera un numero de DPI para el ciudadano con el correlativo y
     * verificador ingresados y lo retorna en el siguiente formato
     * "XXXX XXXXX XXXX"
     * 
     * @param correlativo
     * @param verificador
     * @return El DPI del ciudadano
     */
    @WebMethod(operationName = "EmisionDPI")
    public String EmisionDPI(@WebParam(name = "correlativo") String correlativo,
            @WebParam(name = "verificador") String verificador) {

        ServiciosCarlos inf = new ServiciosCarlos();
        return inf.emisionDPI(correlativo, verificador);
    }
    
    /**
     * Retorna el nombre y el apellido del ciudadano que tenga
     * el numero de DPI ingresado.
     * 
     * @param dpi
     * @return Nombre y apellido del ciudadano
     */
    @WebMethod(operationName = "ConsultaCiudadana")
    public String ConsultaCiudadana(@WebParam(name = "DPI") String dpi) {

        ServiciosCarlos inf = new ServiciosCarlos();
        return inf.consultaDatosCiudadano(dpi);
    }

     /**    
     * @param dpi_esposo
     * @param dpi_esposa
     * @return true si se logró inscribir el matrimonio, de lo contrario false.
     */
    @WebMethod(operationName = "InscribirMatrimonio")
    public boolean inscribirMatrimonio(@WebParam(name = "dpi_esposo") String dpi_esposo,
            @WebParam(name = "dpi_esposa") String dpi_esposa) {

        MatrimonioQuery m = new MatrimonioQuery(dpi_esposo, dpi_esposa);
        try {
            int res = m.inscribir();
            return res > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
     /**    
     * @param dpi_esposo
     * @param dpi_esposa
     * @return el certificado de matrimonio en html
     */
    @WebMethod(operationName = "ObtenerCertificadoMatrimonio")
    public String ObtenerCertificadoMatrimonio(@WebParam(name = "dpi_esposo") String dpi_esposo,
            @WebParam(name = "dpi_esposa") String dpi_esposa) {        
            MatrimonioQuery m = new MatrimonioQuery(dpi_esposo,dpi_esposa);            
            try {                
                return m.getCertificado();                
            } catch (Exception ex) {
                System.out.println(ex);                
            }
            return "";
    }
     /**    
     * @param dpi_esposo
     * @param dpi_esposa
     * @return true si se logró ejecutar el divorcio, de lo contrario false.
     */
    @WebMethod(operationName = "Divorciar")
    public boolean Divorciar(@WebParam(name = "dpi_esposo") String dpi_esposo,
            @WebParam(name = "dpi_esposa") String dpi_esposa) {        
            MatrimonioQuery m = new MatrimonioQuery(dpi_esposo,dpi_esposa);            
            try {                
                return m.divorciar() >0;                            
            } catch (Exception ex) {
                System.out.println(ex);                
                return false;
            }            
    }
    
    @WebMethod(operationName = "ReconocerHijo1")
    public boolean reconocerHijo1(@WebParam(name = "dpi_padre") String dpi_padre, @WebParam(name="correlativo")String correlativo, @WebParam(name="verificador")String verificador){
        
        ServiciosJulio j = new ServiciosJulio();
        try {
            int res =  j.reconocerHijo(dpi_padre,correlativo,verificador);
            if(res > 0) 
                return true;            
            else 
                return false;            
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    @WebMethod(operationName = "ReconocerHijo2")
    public boolean reconocerHijo2(@WebParam(name = "dpi_padre") String dpi_padre, @WebParam(name="dpi_madre") String dpi_madre, @WebParam(name="correlativo")String correlativo, @WebParam(name="verificador")String verificador){
        
        ServiciosJulio j = new ServiciosJulio();
        try {
            int res =  j.reconocerHijo(dpi_padre,dpi_madre,correlativo,verificador);
            if(res > 0) 
                return true;            
            else 
                return false;            
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    @WebMethod(operationName = "DeclararBancarrota")
    public boolean declararBancarrota(@WebParam(name="dpi") String dpi){
        ServiciosJulio j = new ServiciosJulio();
        try {
            int res =  j.declararBancarrota(dpi);
            if(res > 0) 
                return true;            
            else 
                return false;            
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    @WebMethod(operationName = "RehabilitarBancarrota")
    public boolean rehabilitarBancarrota(@WebParam(name="dpi") String dpi){
        ServiciosJulio j = new ServiciosJulio();
        try {
            int res =  j.rehabilitarBancarrota(dpi);
            if(res > 0) 
                return true;            
            else 
                return false;            
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    @WebMethod(operationName = "NotificarDefuncion")
    public boolean notificarDefuncion(@WebParam(name="dpi") String dpi,@WebParam(name="fecha") String fecha,@WebParam(name="causademuerte") String causamuerte){
        ServiciosJulio j = new ServiciosJulio();
        try {
            int res =  j.notificarDefuncion(dpi,fecha,causamuerte);
            if(res > 0) 
                return true;            
            else 
                return false;            
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    @WebMethod(operationName = "ConsultarTarifario")
    public String tarifario(){
        ServiciosJulio j = new ServiciosJulio();
        
            return j.tarifario();
                        
        
        
    }
}
