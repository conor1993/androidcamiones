package com.example.manueli.siscam;

/**
 * Created by integra-des2 on 19/12/2017.
 */

public class RutasConfirmacion {

    private String CodRuta;
    private String Empresa;
    private String HoraIni;
    private String Ruta;
    private String Tipo;

    public RutasConfirmacion(String CodRuta,String Empresa,String HoraIni, String Ruta,String Tipo){
        this.CodRuta = CodRuta;
        this.Empresa = Empresa;
        this.HoraIni = HoraIni;
        this.Ruta    = Ruta;
        this.Tipo   = Tipo;
    }

    public String getCodRuta() {
        return this.CodRuta;
    }

    public void setCodRuta(String codRuta) {
        CodRuta = codRuta;
    }

    public String getEmpresa() {
        return this.Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getHoraIni() {
        return this.HoraIni;
    }

    public void setHoraIni(String horaIni) {
        HoraIni = horaIni;
    }

    public String getRuta() {
        return this.Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }

    public String getTipo() {
        return this.Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
    public String toString() {
        return this.HoraIni+' '+ this.Empresa + ' '+this.Ruta+' '+this.Tipo ;
    }

}
