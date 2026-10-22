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

La ejecución verificada de las pruebas se conserva en
[`docs/resultado-pruebas.txt`](docs/resultado-pruebas.txt); termina en
`13/13 pruebas OK`:

```text
OK  biblioteca con solo la raíz
OK  estructura inicial
OK  niveles y búsqueda sin distinguir mayúsculas
OK  reglas de inserción y códigos únicos
OK  recorridos del estante SW-E1
OK  duplicado, camino y rango
OK  eliminación de hoja, un hijo y dos hijos
OK  modificación conserva código y valida cambios
OK  operaciones sobre libro inexistente
OK  validaciones de datos de libro
OK  ABB inválido por nieto mal ubicado
OK  reconstrucción directa desde preorden
OK  inserción de tres códigos demostrativos
13/13 pruebas OK
```
GitHub Actions compila el proyecto y ejecuta la misma suite en cada push a
`main`/`desarrollo` y en pull requests hacia `main`.

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
Esa guía muestra además una ejecución reproducible con los códigos de ejemplo
`11, 22, 33`; no son códigos personales derivados de una cédula. Sustitúyelos
por tus resultados antes de presentar las trazas personales.

El informe de la práctica se encuentra en
[`Deber-Arboles-VillacresFernando.pdf`](Deber-Arboles-VillacresFernando.pdf).
Su resultado debe coincidir con la suite actual: `13/13 pruebas OK`.

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

- El repositorio público de este proyecto es
  [Deber-Arboles-VillacresFernando](https://github.com/fercho123909/Deber-Arboles-VillacresFernando).
- El pull request [#1](https://github.com/fercho123909/Deber-Arboles-VillacresFernando/pull/1)
  de `desarrollo` a `main` fue integrado. La rama `main` contiene la versión
  entregable del proyecto.
- Adjuntar en Moodle el PDF solicitado con enlace al repositorio, capturas
  propias de la ejecución, pruebas, historial de commits y pull request, y la reflexión.
- Completar en [`TRAZAS.md`](TRAZAS.md) los códigos derivados de los últimos
  cuatro dígitos de la cédula. No se necesita subir ni compartir ese número.
- Ejecutar el proyecto y explicar las decisiones de implementación antes de
  entregar: la práctica es individual.

El archivo [`docs/resultado-pruebas.txt`](docs/resultado-pruebas.txt) conserva
la salida real de la suite local. Para Moodle, agrega además capturas propias
de la ejecución y de las páginas del historial de commits y del pull request;
el archivo de texto no las sustituye. Los códigos `11, 22, 33` de las trazas
son demostrativos y deben reemplazarse por los valores personales calculados
antes de entregar.
