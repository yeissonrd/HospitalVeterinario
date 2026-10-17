/**
 * Contiene toda la logica de gestion del hospital.
 * No lee ni imprime nada: cada operacion devuelve el mensaje
 * como String y la clase Main es la que lo muestra en consola.
 */
public class Hospital {

    private static final int MAX_PACIENTES = 10;
    private static final int MAX_JAULAS = 6;

    // Datos guardados solo en vectores de tamano fijo
    private Paciente[] pacientes;
    private int contadorPacientes;
    private Jaula[] jaulas;

    public Hospital() {
        pacientes = new Paciente[MAX_PACIENTES];
        contadorPacientes = 0;
        jaulas = new Jaula[MAX_JAULAS];
        cargarDatosIniciales();
    }

    /** Datos con los que arranca el sistema (todos sin jaula). */
    private void cargarDatosIniciales() {
        jaulas[0] = new Jaula(1, "S", "A");
        jaulas[1] = new Jaula(2, "M", "A");
        jaulas[2] = new Jaula(3, "M", "A");
        jaulas[3] = new Jaula(4, "L", "A");
        jaulas[4] = new Jaula(5, "S", "B");
        jaulas[5] = new Jaula(6, "M", "B");

        agregarPaciente(new Perro("Rocky", 30.0, 4, "labrador", true));
        agregarPaciente(new Gato("Misifu", 4.0, 3, 0));
        agregarPaciente(new AveExotica("Kiwi", 0.8, 2, 70));
    }

    // ===================== INGRESAR (R1, R2, R3) =====================

    public String ingresarPerro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        return ingresar(new Perro(nombre, peso, edad, raza, vacunado));
    }

    public String ingresarGato(String nombre, double peso, int edad) {
        return ingresar(new Gato(nombre, peso, edad, 0));
    }

    public String ingresarAve(String nombre, double peso, int edad, double envergadura) {
        return ingresar(new AveExotica(nombre, peso, edad, envergadura));
    }

    /** Logica comun para los tres tipos. Orden: datos, duplicado, lleno. */
    private String ingresar(Paciente nuevo) {
        if (!nuevo.esValido()) {
            return "ERROR: datos invalidos";
        }
        if (buscarIndice(nuevo.getNombre()) != -1) {
            return "ERROR: nombre duplicado";
        }
        if (contadorPacientes >= MAX_PACIENTES) {
            return "ERROR: hospital lleno";
        }
        agregarPaciente(nuevo);
        if (Paciente.CUARENTENA.equals(nuevo.getEstado())) {
            return "OK: " + nuevo.getNombre() + " ingresado en CUARENTENA";
        }
        return "OK: " + nuevo.getNombre() + " ingresado";
    }

    // ===================== VACUNAR =====================

    public String vacunar(String nombre) {
        Paciente p = buscarPaciente(nombre);
        if (p == null) {
            return "ERROR: paciente no encontrado";
        }
        if (!(p instanceof Perro)) {
            return "ERROR: solo aplica a perros";
        }
        Perro perro = (Perro) p;
        if (perro.isVacunado()) {
            return "ERROR: ya vacunado";
        }
        perro.vacunar();
        return "OK: " + perro.getNombre() + " vacunado (pasa a INGRESADO)";
    }

    // ===================== ASIGNAR (R4, R5, R6) =====================

    public String asignar(String nombre) {
        Paciente p = buscarPaciente(nombre);
        if (p == null) {
            return "ERROR: paciente no encontrado";
        }
        if (Paciente.CUARENTENA.equals(p.getEstado())) {
            return "ERROR: paciente en cuarentena";
        }
        if (p.tieneJaula()) {
            return "ERROR: paciente ya tiene jaula";
        }
        Jaula jaula = buscarJaulaCompatible(p);
        if (jaula == null) {
            return "ERROR: no hay jaula compatible";
        }
        // Enlace en ambos sentidos: jaula -> paciente y paciente -> jaula
        jaula.setOcupante(p);
        p.setJaula(jaula);
        return "OK: " + p.getNombre() + " asignado a jaula " + jaula.getNumero();
    }

    /** Primera jaula libre con el tamano y la zona que necesita el paciente. */
    private Jaula buscarJaulaCompatible(Paciente p) {
        for (int i = 0; i < jaulas.length; i++) {
            Jaula j = jaulas[i];
            if (j.estaLibre()
                    && j.getTamano().equals(p.getTamanoRequerido())
                    && j.getZona().equals(p.getZona())) {
                return j;
            }
        }
        return null;
    }

    // ===================== MEDICAR (R7, R8, R9) =====================

    public String medicar(String nombre, double mg) {
        Paciente p = buscarPaciente(nombre);
        if (p == null) {
            return "ERROR: paciente no encontrado";
        }
        if (mg <= 0) {
            return "ERROR: datos invalidos";
        }
        if (Paciente.CUARENTENA.equals(p.getEstado())) {
            return "ERROR: paciente en cuarentena";
        }
        if (!p.tieneJaula()) {
            return "ERROR: paciente sin jaula";
        }
        if (p.tieneEstresAlto()) {
            return "ERROR: gato con estres alto";
        }
        if (mg > p.getDosisMaxima()) {
            return "ERROR: dosis excede el maximo";
        }
        if (p.getDosisAcumulada() + mg > p.getTopeAcumulado()) {
            return "ERROR: tope acumulado superado";
        }
        p.registrarDosis(mg);   // si es Gato, tambien sube su estres
        return "OK: " + p.getNombre() + " medicado con " + formatearNumero(mg) + " mg";
    }

    // ===================== ALTA (R10) =====================

    public String alta(String nombre) {
        int indice = buscarIndice(nombre);
        if (indice == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente p = pacientes[indice];
        if (!Paciente.EN_TRATAMIENTO.equals(p.getEstado())) {
            return "ERROR: paciente no apto para alta";
        }
        // Liberar la jaula
        if (p.tieneJaula()) {
            p.getJaula().setOcupante(null);
            p.setJaula(null);
        }
        eliminarCompactando(indice);
        return "OK: " + p.getNombre() + " dado de alta";
    }

    // ===================== REPORTE =====================

    public String reporte() {
        String texto = "--- REPORTE ---\n";
        texto += "Pacientes: " + contadorPacientes + "/" + MAX_PACIENTES + "\n";
        for (int i = 0; i < contadorPacientes; i++) {
            Paciente p = pacientes[i];
            String numeroJaula = p.tieneJaula() ? String.valueOf(p.getJaula().getNumero()) : "-";
            texto += p.getNombre() + " | " + p.getTipo() + " | "
                    + unDecimal(p.getPeso()) + " kg | " + p.getEstado()
                    + " | jaula: " + numeroJaula + "\n";
        }
        texto += "Jaulas libres: " + contarJaulasLibres();
        return texto;
    }

    // ===================== MANEJO DE VECTORES =====================

    /** Agrega al final del vector y aumenta el contador. */
    private void agregarPaciente(Paciente p) {
        pacientes[contadorPacientes] = p;
        contadorPacientes++;
    }

    /** Busqueda lineal por nombre sin distinguir mayusculas. -1 si no existe. */
    private int buscarIndice(String nombre) {
        for (int i = 0; i < contadorPacientes; i++) {
            if (pacientes[i].getNombre().equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private Paciente buscarPaciente(String nombre) {
        int indice = buscarIndice(nombre);
        if (indice == -1) {
            return null;
        }
        return pacientes[indice];
    }

    /** Corre una posicion a la izquierda los siguientes y baja el contador. */
    private void eliminarCompactando(int indice) {
        for (int i = indice; i < contadorPacientes - 1; i++) {
            pacientes[i] = pacientes[i + 1];
        }
        pacientes[contadorPacientes - 1] = null;
        contadorPacientes--;
    }

    private int contarJaulasLibres() {
        int libres = 0;
        for (int i = 0; i < jaulas.length; i++) {
            if (jaulas[i].estaLibre()) {
                libres++;
            }
        }
        return libres;
    }

    // ===================== FORMATO DE NUMEROS =====================

    /** Peso con un decimal y punto (ej: 30.0, 0.8). */
    private String unDecimal(double valor) {
        return String.valueOf(Math.round(valor * 10) / 10.0);
    }

    /** Muestra 50 en vez de 50.0, y 12.5 tal cual. */
    private String formatearNumero(double valor) {
        if (valor == Math.floor(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }
}
