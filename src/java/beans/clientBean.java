/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Clientes.RENAP_Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author carlosrodf
 */
@ManagedBean
public class clientBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/ec2-54-186-237-148.us-west-2.compute.amazonaws.com_8080/Proyecto_SA/RENAP.wsdl")
    private RENAP_Service service;

    private Date fechanac;
    private Map<String, String> generos;

    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private String genero;
    private String pais;
    private String departamento;
    private String municipio;
    private String respuestaR;

    private String correlativo;
    private String verificador;
    private String respuestaE;

    @PostConstruct
    public void init() {
        generos = new HashMap<>();
        generos.put("Masculino", "M");
        generos.put("Femenino", "F");
    }

    public String emitirDPI() {

        this.respuestaE = emisionDPI(correlativo, verificador);
        
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Emision de DPI",
                        respuestaE));
        return "index";
    }

    public String registrar() {
        this.respuestaR = "";
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        String date = DATE_FORMAT.format(fechanac);
        String datos = "";

        List<String> result = this.inscribirciudadano(nombre1, nombre1, apellido1, apellido1, date, genero, pais, departamento, municipio);

        try {
            datos = result.get(1);
            datos += " - " + result.get(2);
            this.respuestaR = datos;
        } catch (Exception e) {
            datos = "Error";
            this.respuestaR = datos;
        }

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        result.get(0),
                        datos));

        return "index";
    }

    public Date getFechanac() {
        return fechanac;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
    }

    public Map<String, String> getGeneros() {
        return generos;
    }

    public void setGeneros(Map<String, String> generos) {
        this.generos = generos;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public String getVerificador() {
        return verificador;
    }

    public void setVerificador(String verificador) {
        this.verificador = verificador;
    }

    public String getRespuestaR() {
        return respuestaR;
    }

    public void setRespuestaR(String respuestaR) {
        this.respuestaR = respuestaR;
    }

    public String getRespuestaE() {
        return respuestaE;
    }

    public void setRespuestaE(String respuestaE) {
        this.respuestaE = respuestaE;
    }

    private java.util.List<java.lang.String> inscribirciudadano(java.lang.String primerNombre, java.lang.String segundoNombre, java.lang.String primerApellido, java.lang.String segundoApellido, java.lang.String fechaNacimiento, java.lang.String genero, java.lang.String pais, java.lang.String departamento, java.lang.String municipio) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        Clientes.RENAP port = service.getRENAPPort();
        return port.inscribirciudadano(primerNombre, segundoNombre, primerApellido, segundoApellido, fechaNacimiento, genero, pais, departamento, municipio);
    }

    private String emisionDPI(java.lang.String correlativo, java.lang.String verificador) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        Clientes.RENAP port = service.getRENAPPort();
        return port.emisionDPI(correlativo, verificador);
    }

}
