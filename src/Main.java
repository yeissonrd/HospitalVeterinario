import java.util.Scanner;

/**
 * Unica clase que usa Scanner y System.out.
 * Lee un comando por linea, se lo pasa al Hospital y muestra la respuesta.
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hospital hospital = new Hospital();

        while (sc.hasNextLine()) {
            String linea = sc.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            String[] partes = linea.split("\\s+");
            String comando = partes[0].toLowerCase();

            if (comando.equals("salir")) {
                break;
            }
            System.out.println(ejecutar(hospital, comando, partes));
        }
        sc.close();
    }

    /** Decide que metodo del Hospital llamar segun el comando. */
    private static String ejecutar(Hospital hospital, String comando, String[] partes) {
        try {
            switch (comando) {
                case "ingresar":
                    return ingresar(hospital, partes);
                case "vacunar":
                    if (partes.length != 2) return "ERROR: datos invalidos";
                    return hospital.vacunar(partes[1]);
                case "asignar":
                    if (partes.length != 2) return "ERROR: datos invalidos";
                    return hospital.asignar(partes[1]);
                case "medicar":
                    if (partes.length != 3) return "ERROR: datos invalidos";
                    return hospital.medicar(partes[1], Double.parseDouble(partes[2]));
                case "alta":
                    if (partes.length != 2) return "ERROR: datos invalidos";
                    return hospital.alta(partes[1]);
                case "reporte":
                    return hospital.reporte();
                default:
                    return "ERROR: comando desconocido";
            }
        } catch (NumberFormatException e) {
            // Escribieron letras donde iba un numero
            return "ERROR: datos invalidos";
        }
    }

    /** ingresar perro|gato|ave ... */
    private static String ingresar(Hospital hospital, String[] partes) {
        if (partes.length < 2) {
            return "ERROR: comando desconocido";
        }
        String tipo = partes[1].toLowerCase();
        switch (tipo) {
            case "perro":
                // ingresar perro <nombre> <peso> <edad> <raza> <si/no>
                if (partes.length != 7) return "ERROR: datos invalidos";
                String vacuna = partes[6].toLowerCase();
                if (!vacuna.equals("si") && !vacuna.equals("no")) {
                    return "ERROR: datos invalidos";
                }
                return hospital.ingresarPerro(partes[2], Double.parseDouble(partes[3]),
                        Integer.parseInt(partes[4]), partes[5], vacuna.equals("si"));
            case "gato":
                // ingresar gato <nombre> <peso> <edad>
                if (partes.length != 5) return "ERROR: datos invalidos";
                return hospital.ingresarGato(partes[2], Double.parseDouble(partes[3]),
                        Integer.parseInt(partes[4]));
            case "ave":
                // ingresar ave <nombre> <peso> <edad> <envergadura>
                if (partes.length != 6) return "ERROR: datos invalidos";
                return hospital.ingresarAve(partes[2], Double.parseDouble(partes[3]),
                        Integer.parseInt(partes[4]), Double.parseDouble(partes[5]));
            default:
                return "ERROR: comando desconocido";
        }
    }
}
