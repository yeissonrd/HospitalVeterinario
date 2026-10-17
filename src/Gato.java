/**
 * HERENCIA: Gato es un Paciente con nivel de estres.
 */
public class Gato extends Paciente {

    private static final double FACTOR_DOSIS = 6;
    private static final double PESO_MAXIMO = 15;
    private static final int ESTRES_MAXIMO = 10;
    private static final int ESTRES_ALTO = 8;

    private int nivelEstres;   // 0 a 10

    public Gato(String nombre, double peso, int edad, int nivelEstres) {
        super(nombre, peso, edad, FACTOR_DOSIS);
        setNivelEstres(nivelEstres);
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getZona() {
        return "A";   // R5
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

    /** R9: con estres de 8 o mas no se puede medicar. */
    @Override
    public boolean tieneEstresAlto() {
        return nivelEstres >= ESTRES_ALTO;
    }

    /** R9: ademas de lo normal, el estres sube 2 (tope 10). */
    @Override
    public void registrarDosis(double mg) {
        super.registrarDosis(mg);
        setNivelEstres(Math.min(nivelEstres + 2, ESTRES_MAXIMO));
    }

    public int getNivelEstres() {
        return nivelEstres;
    }

    public boolean setNivelEstres(int nivelEstres) {
        if (nivelEstres < 0 || nivelEstres > ESTRES_MAXIMO) {
            return false;
        }
        this.nivelEstres = nivelEstres;
        return true;
    }
}
