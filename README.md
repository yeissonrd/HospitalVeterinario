# Hospital Veterinario – Evaluación POO en Java

Sistema de consola en Java para gestionar los pacientes de una clínica veterinaria.

- **Lenguaje:** Java 17 o superior, solo por consola.
- **Datos:** solo vectores de tamaño fijo (`Paciente[10]` y `Jaula[6]`). No se usan `ArrayList`, colecciones, archivos ni bases de datos.

---

## 1. ¿Qué hice?

Construí un programa que permite al personal de la clínica:

- **Ingresar** pacientes de tres tipos: perros, gatos y aves exóticas (máximo 10, sin nombres repetidos).
- **Vacunar** perros. Un perro sin vacuna entra en cuarentena y sale de ella al vacunarlo.
- **Asignar jaula** automáticamente según el peso y el tipo de animal (6 jaulas en las zonas A y B).
- **Medicar** controlando la dosis máxima por aplicación, el tope acumulado y el estrés de los gatos.
- **Dar de alta** a los pacientes en tratamiento, liberando su jaula.
- **Ver un reporte** con todos los pacientes, su estado, su jaula y las jaulas libres.

El programa arranca con 3 pacientes (Rocky, Misifu y Kiwi) y 6 jaulas cargados en `cargarDatosIniciales()`.

### Clases que creé (un archivo `.java` por clase)

| Clase | Hereda de | Responsabilidad |
|---|---|---|
| `Paciente` | – (abstracta) | Atributos y comportamiento común: nombre, peso, edad, estado, factorDosis, dosisAcumulada y jaula |
| `Perro` | `Paciente` | Agrega raza y vacunado. factorDosis = 10, peso máximo 80 kg |
| `Gato` | `Paciente` | Agrega nivelEstres (0 a 10). factorDosis = 6, peso máximo 15 kg |
| `AveExotica` | `Paciente` | Agrega envergadura (cm). factorDosis = 3, peso máximo 15 kg |
| `Jaula` | – | Número, tamaño (S, M, L), zona (A o B) y ocupante |
| `Hospital` | – | Toda la lógica de gestión y los vectores |
| `Main` | – | Única clase con `Scanner` y `System.out` (lee los comandos y muestra los mensajes) |

---

## 2. ¿Cómo lo hice?

### 2.1 Generalización
Primero identifiqué lo que tienen en común Perro, Gato y AveExotica y lo puse en una clase general: `Paciente`. Así los atributos comunes (nombre, peso, edad, estado, dosis, jaula) y los cálculos comunes (dosis máxima, tope acumulado, tamaño de jaula según el peso) se escriben **una sola vez**.

`Paciente` es **abstracta** porque en el hospital no existe un paciente "genérico": siempre es un perro, un gato o un ave. Además obliga a cada subclase a definir `getPesoMaximo()`, `getZona()` y `getTipo()`.

### 2.2 Herencia
- Cada subclase usa `extends Paciente` y en su constructor llama a `super(nombre, peso, edad, FACTOR_DOSIS)`, pasando su propio factor de dosis.
- Las subclases solo agregan lo que las hace diferentes, sin repetir código.
- Usé **sobrescritura (`@Override`)** para los casos especiales:
  - `AveExotica.esValido()` agrega la validación de la envergadura.
  - `AveExotica.getTamanoRequerido()`: si la envergadura es mayor a 60 cm, necesita mínimo jaula M.
  - `Gato.registrarDosis()` llama a `super.registrarDosis()` y además sube el estrés 2 puntos (tope 10).
  - `Gato.tieneEstresAlto()` devuelve true con estrés de 8 o más.
- Los tres tipos se guardan en el **mismo vector** `Paciente[]` (polimorfismo): el Hospital llama `p.getZona()` o `p.registrarDosis(mg)` y cada objeto responde según su clase, sin necesidad de `if` por cada tipo.

### 2.3 Encapsulamiento
- Todos los atributos son `private`.
- Se accede a ellos con getters y setters.
- Los setters **validan** y, si el valor es inválido, **lo rechazan sin modificar el atributo** (devuelven `false`). Ejemplos: `setPeso` rechaza pesos ≤ 0 o mayores al máximo del animal, `setEdad` solo acepta de 0 a 30, `setTamano` solo S, M o L, y `setZona` solo A o B.
- Los constructores usan esos mismos setters, así la validación está en un solo lugar.

### 2.4 Separación dominio / consola
Las clases del dominio no leen ni imprimen nada. Cada método de `Hospital` devuelve el mensaje (`"OK: ..."` o `"ERROR: ..."`) como `String`, y `Main` es quien lo imprime.

`Main` lee cada línea, la divide por espacios con `split("\\s+")` en un vector `partes` y, según la primera palabra, llama al método del Hospital correspondiente. Si un número viene mal escrito, se atrapa la `NumberFormatException` y se muestra `ERROR: datos invalidos`.

### 2.5 Manejo de vectores (en `Hospital`)
- **Contador:** `contadorPacientes` indica cuántas posiciones del vector están ocupadas.
- **Búsqueda:** `buscarIndice(nombre)` recorre el vector comparando con `equalsIgnoreCase` (el nombre es único sin distinguir mayúsculas). Devuelve -1 si no existe.
- **Límites:** máximo 10 pacientes (`ERROR: hospital lleno`).
- **Compactación:** al dar de alta, los pacientes siguientes se corren una posición a la izquierda, la última queda en `null` y el contador baja.

### 2.6 Reglas de negocio
Cada comando revisa las validaciones en el orden que pide el enunciado. La primera que falla devuelve el error y no cambia nada.

| Regla | Dónde se cumple |
|---|---|
| R1 – Datos válidos | Setters + `esValido()` |
| R2 – Máximo 10, nombre único. Orden: datos, duplicado, lleno | `Hospital.ingresar()` |
| R3 – Perro no vacunado entra en CUARENTENA | Constructor de `Perro` |
| R4 – Tamaño de jaula según peso (ave > 60 cm mínimo M) | `getTamanoRequerido()` |
| R5 – Perros y gatos en zona A, aves en zona B | `getZona()` |
| R6 – Primera jaula libre compatible, enlace jaula ↔ paciente | `Hospital.asignar()` |
| R7 – Dosis máxima = peso × factorDosis; acumulado ≤ 3 × máxima | `getDosisMaxima()`, `getTopeAcumulado()` |
| R8 – Orden de validaciones de medicar | `Hospital.medicar()` |
| R9 – Suma al acumulado, pasa a EN_TRATAMIENTO, gato +2 de estrés | `registrarDosis()` |
| R10 – Alta solo en EN_TRATAMIENTO, libera jaula y compacta | `Hospital.alta()` |

### 2.7 Decisión tomada
En la regla R6 ("jaula que cumpla tamaño") interpreté que el tamaño de la jaula debe ser **exactamente** el que pide la regla R4.

### 2.8 Control de versiones
El código está en GitHub con 2 commits: el primero con el núcleo del dominio (`Paciente`, subclases y `Jaula`) y el segundo con `Hospital`, el menú en `Main` y las reglas completas.

---

## 3. Cómo ejecutarlo

Desde la carpeta `src`:

```bash
javac *.java
java Main
```

En IntelliJ IDEA basta con abrir `Main.java` y darle a **Run**. El programa no muestra menú: espera que escribas un comando y presiones Enter.

### Comandos

```text
ingresar perro <nombre> <peso> <edad> <raza> <si/no>
ingresar gato <nombre> <peso> <edad>
ingresar ave <nombre> <peso> <edad> <envergadura>
vacunar <nombre>
asignar <nombre>
medicar <nombre> <mg>
alta <nombre>
reporte
salir
```

### Ejemplo de uso

```text
asignar Rocky
OK: Rocky asignado a jaula 4
medicar Rocky 300
OK: Rocky medicado con 300 mg
ingresar perro Toby 12.5 3 criollo no
OK: Toby ingresado en CUARENTENA
reporte
--- REPORTE ---
Pacientes: 4/10
Rocky | Perro | 30.0 kg | EN_TRATAMIENTO | jaula: 4
Misifu | Gato | 4.0 kg | INGRESADO | jaula: -
Kiwi | AveExotica | 0.8 kg | INGRESADO | jaula: -
Toby | Perro | 12.5 kg | CUARENTENA | jaula: -
Jaulas libres: 5
```
