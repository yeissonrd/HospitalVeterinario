/**
 * Clase general (superclase) de todos los pacientes del hospital.
 * GENERALIZACION: aqui van los atributos y comportamientos que comparten
 * Perro, Gato y AveExotica, para no repetir codigo en cada subclase.
 * Es abstracta porque no existe un "paciente" que no sea de algun tipo.
 */
public abstract class Paciente {

    // Estados validos de un paciente
    public static final String INGRESADO = "INGRESADO";
    public static final String CUARENTENA = "CUARENTENA";
    public static final String EN_TRATAMIENTO = "EN_TRATAMIENTO";

    // ENCAPSULAMIENTO: todos los atributos son privados
    private String nombre;
    private double peso;            // kg
    private int edad = -1;          // anios (-1 = aun no tiene un valor valido)
    private String estado;
    private final double factorDosis;   // mg por kg, lo define cada subclase
    private double dosisAcumulada;      // mg
    private Jaula jaula;                // null si no tiene jaula asignada

    public Paciente(String nombre, double peso, int edad, double factorDosis) {
        this.factorDosis = factorDosis;
        this.estado = INGRESADO;
        this.dosisAcumulada = 0;
        this.jaula = null;
        // Se usan los setters para que las validaciones esten en un solo lugar
        setNombre(nombre);
        setPeso(peso);
        setEdad(edad);
    }

    // ---------- Metodos que cada subclase debe definir ----------

    /** Peso maximo permitido para el tipo de animal. */
    public abstract double getPesoMaximo();

    /** Zona del hospital donde puede estar ("A" o "B"). */
    public abstract String getZona();

    /** Nombre del tipo de paciente para el reporte. */
    public abstract String getTipo();

    // ---------- Comportamiento comun ----------

    /** true si todos los datos quedaron validos (regla R1). */
    public boolean esValido() {
        return nombre != null && peso > 0 && edad >= 0;
    }

    /** R4: tamano de jaula segun el peso. */
    public String getTamanoRequerido() {
        if (peso < 5) {
            return "S";
        } else if (peso <= 25) {
            return "M";
        } else {
            return "L";
        }
    }

    /** R7: dosis maxima por aplicacion = peso x factorDosis. */
    public double getDosisMaxima() {
        return peso * factorDosis;
    }

    /** R7: el acumulado no puede superar 3 x dosis maxima. */
    public double getTopeAcumulado() {
        return 3 * getDosisMaxima();
    }

    /** R8/R9: por defecto un paciente no tiene estres alto (Gato lo redefine). */
    public boolean tieneEstresAlto() {
        return false;
    }

    /** R9: registra una medicacion valida. */
    public void registrarDosis(double mg) {
        dosisAcumulada = dosisAcumulada + mg;
        estado = EN_TRATAMIENTO;
    }

    // ---------- Getters y setters con validacion ----------
    // Cada setter devuelve true si acepto el valor, o false si lo rechazo
    // (cuando lo rechaza, el atributo NO se modifica).

    public String getNombre() {
        return nombre;
    }

    public boolean setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        this.nombre = nombre.trim();
        return true;
    }

    public double getPeso() {
        return peso;
    }

    public boolean setPeso(double peso) {
        if (peso <= 0 || peso > getPesoMaximo()) {
            return false;
        }
        this.peso = peso;
        return true;
    }

    public int getEdad() {
        return edad;
    }

    public boolean setEdad(int edad) {
        if (edad < 0 || edad > 30) {
            return false;
        }
        this.edad = edad;
        return true;
    }

    public String getEstado() {
        return estado;
    }

    public boolean setEstado(String estado) {
        if (!INGRESADO.equals(estado) && !CUARENTENA.equals(estado)
                && !EN_TRATAMIENTO.equals(estado)) {
            return false;
        }
        this.estado = estado;
        return true;
    }

    public double getFactorDosis() {
        return factorDosis;
    }

    public double getDosisAcumulada() {
        return dosisAcumulada;
    }

    public Jaula getJaula() {
        return jaula;
    }

    public void setJaula(Jaula jaula) {
        this.jaula = jaula;
    }

    public boolean tieneJaula() {
        return jaula != null;
    }
}
