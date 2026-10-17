/**
 * Jaula del hospital. Esta libre cuando no tiene ocupante.
 */
public class Jaula {

    private int numero;
    private String tamano;      // "S", "M" o "L"
    private String zona;        // "A" o "B"
    private Paciente ocupante;  // null si esta libre

    public Jaula(int numero, String tamano, String zona) {
        setNumero(numero);
        setTamano(tamano);
        setZona(zona);
        this.ocupante = null;
    }

    public boolean estaLibre() {
        return ocupante == null;
    }

    public int getNumero() {
        return numero;
    }

    public boolean setNumero(int numero) {
        if (numero <= 0) {
            return false;
        }
        this.numero = numero;
        return true;
    }

    public String getTamano() {
        return tamano;
    }

    public boolean setTamano(String tamano) {
        if (!"S".equals(tamano) && !"M".equals(tamano) && !"L".equals(tamano)) {
            return false;
        }
        this.tamano = tamano;
        return true;
    }

    public String getZona() {
        return zona;
    }

    public boolean setZona(String zona) {
        if (!"A".equals(zona) && !"B".equals(zona)) {
            return false;
        }
        this.zona = zona;
        return true;
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    public void setOcupante(Paciente ocupante) {
        this.ocupante = ocupante;
    }
}
