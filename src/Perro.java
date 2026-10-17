/**
 * HERENCIA: Perro es un Paciente. Solo agrega lo que lo hace diferente.
 */
public class Perro extends Paciente {

    private static final double FACTOR_DOSIS = 10;
    private static final double PESO_MAXIMO = 80;

    private String raza;
    private boolean vacunado;

    public Perro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        super(nombre, peso, edad, FACTOR_DOSIS);   // la superclase guarda lo comun
        setRaza(raza);
        this.vacunado = vacunado;
        // R3: un perro no vacunado ingresa en cuarentena
        if (!vacunado) {
            setEstado(CUARENTENA);
        }
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
        return "Perro";
    }

    /** Vacuna al perro; si estaba en cuarentena pasa a INGRESADO. */
    public void vacunar() {
        vacunado = true;
        if (CUARENTENA.equals(getEstado())) {
            setEstado(INGRESADO);
        }
    }

    public String getRaza() {
        return raza;
    }

    public boolean setRaza(String raza) {
        if (raza == null || raza.trim().isEmpty()) {
            return false;
        }
        this.raza = raza.trim();
        return true;
    }

    public boolean isVacunado() {
        return vacunado;
    }
}
