package com.gabriel.scannerbandtec;

public class Maquina {

    private int idMaquina;
    private String modelo;
    private String hsl;
    private String serial;
    private String patrimonio;

    public int getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(int idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getHsl() {
        return hsl;
    }

    public void setHsl(String hsl) {
        this.hsl = hsl;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    @Override
    public String toString(){
        String todasMaquinas = String.format("HSL: %s\nSerial Number: %s\nModelo: %s\nPatrimônio: %s",hsl,serial,modelo,patrimonio);
        return todasMaquinas;
    }

}
