# Catálogo de la Biblioteca FISEI

Práctica individual de Estructura de Datos: implementación recursiva de un árbol
general para organizar la biblioteca y un árbol binario de búsqueda (ABB) por
código para cada estante.

## Requisitos

- JDK 8 o superior.
- Visual Studio Code (opcional) con soporte para Java.

## Compilar y ejecutar

Desde la carpeta `BibliotecaFISEI`, en PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp out app.Main
```

El menú permite cargar la estructura, consultar la jerarquía, agregar y buscar
unidades, ver estadísticas, operar con libros, imprimir recorridos con traza y
ejecutar los retos del ABB. Selecciona `1` para cargar los datos iniciales.

Para ejecutar las pruebas:

```powershell
java -cp out app.PruebasBiblioteca
```

La salida esperada termina en `12/12 pruebas OK`.

## Datos iniciales

```text
BIB Biblioteca FISEI
  SW Software
    SW-E1 Estante 1 de Software: 60, 35, 80, 20, 45, 70, 90, 40, 75
    SW-E2 Estante 2 de Software: 15, 10, 25
  TI Tecnologías de la Información
    TI-E1 Estante 1 de TI: 50
  TEL Telecomunicaciones
```

La biblioteca tiene 7 unidades, 4 hojas, altura 2 (en aristas) y 13 libros.
Los detalles de los recorridos, las trazas y las eliminaciones están en
[`TRAZAS.md`](TRAZAS.md).

## Organización

```text
src/
├── enums/    TipoUnidad.java, TipoRecorrido.java
├── modelo/   Libro.java, UnidadBiblioteca.java
├── negocio/  NodoUnidad.java, NodoLibro.java,
│             ArbolBiblioteca.java, CatalogoABB.java
└── app/      Main.java, DatosPrueba.java, PruebasBiblioteca.java
```

Las operaciones de los dos árboles (búsqueda, inserción, recorridos, métricas,
validación, eliminación y reconstrucción) están implementadas con recursividad.
No se usan `TreeSet`, `TreeMap`, `PriorityQueue`, `NavigableSet`,
`NavigableMap`, `Collections.sort` ni `List.sort`.

## Preparación de la entrega

- Crear el repositorio público `Deber-Arboles-ApellidoNombre`.
- Crear y usar la rama `desarrollo`, realizar commits propios y abrir un pull
  request hacia `main`; revisar que el resultado quede integrado en `main`.
- Adjuntar en Moodle el PDF solicitado con enlace al repositorio, capturas
  propias de la ejecución, las pruebas, commits y pull request, y la reflexión.
- Completar en [`TRAZAS.md`](TRAZAS.md) los códigos derivados de los últimos
  cuatro dígitos de la cédula. No se necesita subir ni compartir ese número.
- Ejecutar el proyecto y explicar las decisiones de implementación antes de
  entregar: la práctica es individual.

El repositorio remoto, los commits, el pull request y las capturas de ejecución
deben generarse desde la cuenta y el equipo del estudiante; no se simulan aquí.
