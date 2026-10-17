/**
 * HERENCIA: AveExotica es un Paciente con envergadura.
 */
public class AveExotica extends Paciente {

    private static final double FACTOR_DOSIS = 3;
    private static final double PESO_MAXIMO = 15;

    private double envergadura;   // cm

    public AveExotica(String nombre, double peso, int edad, double envergadura) {
        super(nombre, peso, edad, FACTOR_DOSIS);
        setEnvergadura(envergadura);
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getZona() {
        return "B";   // R5
    }

    @Override
    public String getTipo() {
        return "AveExotica";
    }

    /** R1: ademas de lo comun, la envergadura debe ser mayor a 0. */
    @Override
    public boolean esValido() {
        return super.esValido() && envergadura > 0;
    }

    /** R4: con envergadura mayor a 60 cm necesita minimo jaula M. */
    @Override
    public String getTamanoRequerido() {
        String tamano = super.getTamanoRequerido();
        if (envergadura > 60 && tamano.equals("S")) {
            return "M";
        }
        return tamano;
    }

    public double getEnvergadura() {
        return envergadura;
    }

    public boolean setEnvergadura(double envergadura) {
        if (envergadura <= 0) {
            return false;
        }
        this.envergadura = envergadura;
        return true;
    }
}
