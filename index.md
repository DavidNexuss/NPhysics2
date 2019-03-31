<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro" rel="stylesheet" type="text/css">
<style>
.inner {width: 1200px;} 
body {
    font-family: 'Source Sans Pro', sans-serif;
    font-size: 20px;
    font-weight: 400;
    line-height: 1.6;
    color: #666;
    letter-spacing: 1.5px;
}

.imgscreen{

    width: 100%
}
#main-content {width: 80%;}
</style>
# ¿Qué es NPhysics?

Este es un trabajo de investigación de bachillerato, el propósito de este es la creación de un Simulador de Dinámica que permita resolver problemas de estática planteados por el usuario, un conjunto de problemas físicos y mecánicos donde el objetivo que se busca es encontrar el equilibrio de las fuerzas en el sistema. Una vez este solucione el sistema ejecutará una simulación en tiempo real donde se supondrá que es una situación dinámica y se comprobará si realmente hay movimiento. Tanto la simulación como el planteamiento del problema se llevarán a cabo en un entorno gráfico y de fácil uso.

# Simulaciones de ejemplo
## Ejemplo de un brazo robótico  
<iframe width="100%" height="500" id="1" src="https://www.youtube.com/embed/Fn4RER__GAM" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

## Cálculo del par motor
<iframe width="100%" height="500" id="2" src="https://www.youtube.com/embed/QfJBSMPoN-0" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

## Simulación con agua
<iframe width="100%" height="500" id="3" src="https://www.youtube.com/embed/cf9TsXBXMsg" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

# Aplicaciones

El programa que he desarrollado permite de una forma eficiente resolver problemas que podrían necesitar de ensayos reales de los que la mayoría seguramente serían destructivos. Tampoco sería justo ni sensato proponer ningún programa informático como sustituto del ensayo empírico de una estructura pero sí permiten muchísima flexibilidad a esta alternativa, se pueden simular gran variedad de estructuras en cuestiones de segundos sin falta de ningún gasto económico o material.
Por otro lado, también creé el programa con el propósito de que todo el mundo que estuviese interesado en el mundo de la mecánica lo pudiera utilizar y sobre todo poder despertar el interés por la mecánica, la física, y la ciencia en general a un público general.

# Instalación

NPhysics es un programa portable, no es necesaria ninguna interfaz de instalación y se puede ejecutar desde la carpeta contenedora del programa. Se puede o bien descargar la última versión estable ya compilada desde GitHub o compilar el proyecto por uno mismo.

## Descargar

El programa se puede descargar desde la página de GitHub en su última versión estable ya compilada, en cada lanzamiento existen 3 versiones del programa comprimido:

+ *NPhysics.zip* (Para descomprimir usando cualquier programa de descompresión)
+ *NPhysics.7z* (Para descomprimir usando 7z)
+ *NPhysics.exe* (Autoejecutable para Windows que descomprime el programa)

NPhysics esta escrito en Java lo que permite que el programa sea multiplataforma, se puede ejecutar en cualquier distribución de Linux, Windows y MacOS. Siempre y cuando se tenga instalado [JRE](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) o [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)  

Una vez descargado y descomprimido solo hay que ejecutar el archivo *NPhysics.jar*, por defecto el programa se ejecuta en pantalla pero se puede ejecutar en modo ventana con el archivo *window.bat* (solo en Windows).

También se puede ejecutar desde un terminal escribiendo la orden ```java -jar NPhysics.jar```, en pantalla completa, y ```java -jar NPhysics.jar window```, en modo ventana. 
## Para compilar

En caso que se desee usar la última versión del programa en su último commit se puede compilar el programa directamente desde el repositorio.
El proyecto es gestionado usando gradle. Para compilar los archivos binarios solo hay que ejecutar la siguiente orden en la raíz del repositorio:  

```gradlew desktop:dist```  

Esta ordena compilará el programa y copiara el ejecutable en la carpeta ```desktop/build/libs```, esta es la carpeta en la que se generará el programa compilado cada vez.  
Una vez compilado hay que copiar todo el contenido de la carpeta ```desktop/assets``` a la carpeta donde se encuentra el compilado.  

### Para ejecutar

Para ejecutar el programa ya compilado solo hay que ejecutar la orden ```java -jar desktop1.0```  
Usa las siguientes bibliotecas:  
[Libgdx](https://libgdx.badlogicgames.com/)  
[Earcut4j](https://github.com/earcut4j/earcut4j)  
[JTS](https://github.com/locationtech/jts)  

# Guía de uso  

Este apartado pretende servir como una pequeña guía de uso introductoria para mostrar las funciones del programa, desde poder crear objetos simples hasta poder incluir en la simulación muelles, poleas, ejes, motores, agua y más. Para hacer esta guía más explicatória y amena irá acompañada de una serie de videos por cada apartado en los que se mostrará los pasos a seguir.  

## Primeros pasos

Este apartado tiene como objetivo mostrar la creación de objetos simples, poder modificar sus atributos físicos y mostrar el manejo de los objetos y de las entidades básicas de la fase de diseño de la simulación.

El programa por defecto usará el inglés como lengua, si se desea cambiar de lengua pulse  ```F1``` para mostrar la configuración del programa, modifique el valor ```Language``` usando la barra desplazable y modifique su opción. Una vez hecho los cambios hay que reiniciar el programa para que surtan efecto.  
<img src="language.PNG" class="imgscreen">

Una vez configurado el idioma el programa esta listo para su uso, a partir de aquí ya no es necesario configurar nada más para usar el programa aunque le recomiendo leer y seguir la guía.  

<iframe width="100%" height="500" if="4" src="https://www.youtube.com/embed/9XXuS8zimsY" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

### Disposición de los elementos gráficos  

Una vez abierto el programa, este muestra una cuadrícula sobre un fondo azul claro, esta es la area de diseño, donde definiremos los objetos i las relaciones entre ellos. El programa tiene 3 menús principales: 

+ La barra superior que nos permite cambiar entre la fase de diseño y la de simulación
+ El menú lateral que nos permite seleccionar la herramienta a usar en la fase de diseño
+ La barra inferior que contiene un botón para borrar todos los objetos del diseño y otro para poder activar/desactivar la función del programa que autoajusta los elementos a la cuadrícula. 

Al pasar el cursor por encima de uno de los botones del menú lateral de las herramientas este mostrará en pantalla un breve texto que describe su función.  

### Movimiento del plano

Por defecto la herramienta seleccionada es la del **mouse** ![Alt](/menu/start.png), esta nos permite seleccionar objetos y mover el plano. Si pulsamos en un lugar vacío y mantenemos pulsado podremos arrastrar el plano, también podemos mover el eje de referencia pulsando la tecla ```A``` sobre el lugar deseado.

### Crear un objeto simple

Para crear un objeto o cuerpo simple con forma de polígono hay que pulsar usar la **herramienta de creación de polígono** ![Alt](/menu/shape.png). Para crear un polígono solo hay que clicar en el plano los puntos deseados que conformaran sus vértices, una vez terminado hay que volver a clicar sobre el primer vértice.

No hay que preocuparse por crear los puntos por separado si la posición deseada no es un punto el programa lo creara automáticamente.

Una vez el objeto esta creado se puede acceder a la ventana para cambiar sus propiedades físicas con la tecla ```Q```. En esta se muestran varias opciones listadas a continuación:

<img src="poligon.PNG" class="imgscreen">

+ Opciones del simulador
    - Simulación rigurosa para objetos rápidos (Como de rigurosa debe ser la simulación en casos extremos, dejar desmarcada por norma general)
    - Estado (Dinámico, Kinemático, Estático)
+ Velocidad lineal (en m/s)
    - Velocidad lineal en x 
    - Velocidad lineal en y 
+ Características del material
    - Masa (en kg)
    - Densidad (en kg/m^2)
    - Fricción (Coeficiente de fricción, entre 0 y 1)
    - Restitución (Coeficiente de restitución, entre 0 y 1)

La opción de simulación rigurosa para objetos rápidos solo se debe aplicar a aquellos objetos que experimenten comportamientos extraños como atravesar otros objetos. Esta opción le marcará al simulador que sea más riguroso en el momento de calcular las colisiones.

La velocidad lineal es la velocidad inicial del objeto.

Después de cambiar cualquier valor se recomienda pulsar ```Intro``` para que los cambios surtan efecto.

Con el objeto seleccionado se puede pulsar la tecla ```C``` sobre cualquier punto para crear un duplicado del objeto.  

### Simulación

Una vez dispuestos los objetos se puede pasar a la fase de Simulación con pulsar su botón en la barra superior. El fondo cambia a un color negro y la simulación empieza. En esta fase se puede coger objetos con el ratón y manipularlos. El programa muestra las fuerzas que actúan sobre los objetos (excepto las producidas por colisiones).

| Tipo de Fuerza | Color |
|---|---|
| Fuerzas descritas por el usuario | Rojo |
| Fuerza de gravedad | Amarillo |
| Fuerzas de reacción  | Verde |
| Tensión de las cuerdas | Naranja |  

También puede mostrar el momento lineal de los objetos pulsando la tecla ```I```, por defecto esta función esta desactivada. Cuando se simulan muchos objetos a la vez puede parecer muy caótico que el programa muestre toda esta información, por eso existe una función que permite esconder todas las flechas, valores y oscurecer los objetos, y solo mostrar resaltada la información del objeto seleccionado. Esta función se activa con la tecla ```U```.

Para volver a la fase de diseño solo hay que pulsar su botón en la barra superior.

---  
## Definir Fuerzas

Una vez definido el objeto en la fase de edición se le puede añadir fuerzas que se aplicarán en la simulación. Para crear una fuerza primero hay que seleccionar el objeto en el modo de diseño. Luego usar la **herramienta de creación de fuerzas** ![Alt](/menu/force.png).  

Para crear la fuerza hay que clicar en el punto del plano que sera su origen, luego en el punto en que el vector de la fuerza termina, esto es solo para poder definir la dirección del vector de una forma visual, para poder definir la magnitud del vector de una forma más precisa se selecciona el vector y este mostrará en pantalla la ventana para poder editar sus propiedades. El vector seleccionado se pinta de Amarillo. 

<img src="fuerza.PNG" class="imgscreen">

+ Establecer vector origen
    Permite establecer las coordenadas del origen del vector con respecto al eje de referencia
+ Establecer vector de fuerza
    Permite establecer las componentes del vector, su módulo y su ángulo, en el caso de que se quiera trabajar con coordenadas cartesianas o polares
    - *Establecer esta fuerza como una incógnita*
        Establece este vector como una incógnita, más adelante se explica esta función.
+ Tipo de vector
    Esta opción permite definir como debe evolucionar la posición del vector conforme se ejecute la simulación:
    + *Mundial* hace que el vector siempre se aplique en la misma posición mundial (no relativa al objeto).
    + *Relativa en traslación* hace que el origen del vector sea relativo a la posición y ángulo del objeto pero su dirección será siempre la misma.
    + *Relativa* hace que el origen y la dirección del vector sean relativos a la posición y rotación del objeto.

Una vez modificados los parámetros deseados cierre la ventana con ```Cerrar``` y podrá ver actuar la fuerza en la fase de simulación.
Para poder eliminar una fuerza hay que seleccionarla y pulsar ```Suprimir```.


## Articulaciones

El programa permite crear varios tipos de articulaciones para poder simular escenarios mas complejos. Esta es una lista de todas las articulaciones o herramientas para definir relaciones entre dos objetos del simulador.

Para la creación de articulaciones es necesario seleccionar los objetos deseados sobre los cuales se aplicará la articulación. Algunas requieren de un objeto seleccionado y otras de 2, para seleccionar dos elementos al mismo tiempo hay que mantener pulsada la tecla ```Shift``` y clicar sobre ambos.

Algunas de estas tienen propiedades que pueden ser modificadas para poder acceder al panel de propiedades de estas hay que seleccionar la articulación y al hacerlo se mostrará el panel.

Una vez en la simulación las articulaciones dibujaran sus fuerzas de reacción (de color verde) en el lugar pertinente.  

+ Ejes y motores
+ Vías prismáticas
+ Cuerdas
+ Muelles
+ Poleas

<iframe width="100%" height="500" if="5" src="https://www.youtube.com/embed/fGHbkg4_ygM" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

### Ejes y motores

Los ejes fijos son la articulación más fácil de colocar, para definirlos se selecciona el objeto deseado. Una vez seleccionado aparecerá la barra lateral para herramientas que requieran de la selección de un objeto. La primera de ellas es la de *creación de un eje/motor*![Alt](/menu/axis.png) (La misma herramienta hace ambas funciones ya que el funcionar de motor o de eje dependerá de que valor le demos a la propiedad de par motor). Una vez escogida la herramienta para colocarlo es tan simple como clicar en el punto donde se desea el soporte.

Su tabla de propiedades consiste en dos campos para definir su posición relativa al eje de referencia y dos más para definir sus propiedades físicas. La primera es el par motor que hará, si el valor es 0 actuará como eje y si es diferente de 0 como motor. El segundo establece la velocidad angular máxima a la que hará girar este cuerpo el motor (en rad/s).

### Vías prismáticas

Las vías prismáticas limitan el movimiento de un objeto a un solo eje. Para añadir uno a un objeto primero se selecciona el objeto y la herramienta de creación de *vías prismáticas*![Alt](/menu/rollaxis.png). Su colocación es similar sino igual a la de los ejes.  

Las vías prismáticas dibujan sobre su icono una linea roja que indica el eje sobre el que se podrá desplazar el objeto. Su panel de propiedades tiene dos campos para ajustar la posición de la articulación y uno para definir el ángulo de la vía.

### Cuerdas

Las cuerdas unen dos objetos y establecen la distancia máxima que podría haber entre sus dos puntos de soporte, esta es su única implicación física. Para colocar una cuerda se deben seleccionar los dos objetos deseados. Aparecerá un menú para colocar articulaciones que relacionen dos objetos, escoja la herramienta para crear una *cuerda* ![Alt](/menu/segment.png). Para crear la cuerda una vez seleccionada su herramienta hay que clicar los puntos de soporte, primero el del primer objeto y luego el del segundo (el orden es irrelevante). Una vez la cuerda esta colocada se puede modificar moviendo sus puntos de soporte.  

Las cuerdas no tienen ninguna propiedad definible.

### Muelles

Los muelles relacionan dos objetos de una forma similar a las cuerdas, solo que en la cuerdas teníamos una tensión aplicada en ambos puntos de soporte cuando se superaba una condición, los muelles por otra parte solo aplican una fuerza a uno de los dos objetos y esta fuerza es proporcional a la distancia que los separa de manera que ```F = -kx``` (la ley de Hooke). Sin entrar en todas las implicaciones de la teoría añadir muelles es bastante sencillo. Hay que seleccionar dos objetos, como se hacia en las cuerdas, solo que esta vez se escoge la herramienta de creación de muelles ![Alt](/menu/spring.png). Una vez escogida al igual que en las cuerdas se selecciona un punto de cada objeto que actuarán como soportes. Una vez hecho el muelle ya estará creado.

Una vez creado se le pueden definir su constante de elasticidad k (N/m). La opción esta en su panel de propiedades.  

Es necesario definir uno de los dos objetos como estático, para que la simulación sepa sobre quien aplicar la fuerza. Se puede combinar con la vía prismática para asegurar el movimiento del objeto en la recta que pasa por los dos puntos de soporte del muelle.

### Poleas

Para crear una polea entre dos objetos se necesita de antemano dos puntos que sirvan de soportes, se pueden crea con la herramienta de crear puntos ![Alt](/menu/point.png), se colocan de la siguiente manera:

Luego hay que seleccionar ambos objetos y escoger la herramienta *polea* ![Alt](/menu/pulley.png), primero seleccionamos un punto del objeto luego su punto de soporte, luego el punto de soporte del objeto secundario y luego un punto del objeto secundario. Una vez hecho este proceso se crea la polea.

El panel de propiedades de la polea tiene una opción para modificar el valor de la relación de transmisión de la polea.

## Solucionador de situaciones estáticas

El programa tiene la funcionalidad de calcular el valor exacto que debe tener una fuerza aplicada en un punto y dirección concreto para provocar una situación estática en el objeto deseado. Hay que crear una fuerza que nos servirá de incógnita y definir su dirección, luego el programa se encargara de encontrar su valor para poder cumplir con las condiciones de una situación estática.

Por como funciona el algoritmo que calcula la fuerza solo se puede resolver en situaciones en que intervenga el par que genera esta fuerza, por ejemplo, si todas las fuerzas son definidas sobre el mismo punto incluyendo la incógnita el programa no podrá encontrar valor. Por suerte el conjunto de problemas más interesantes no son de este tipo y el algoritmo los podrá resolver. Si en nuestro problema intervienen varias incógnitas y objetos es recomendable ejecutar el algoritmo por partes, solucionando primero las fuerzas que menos intervengan en el problema.

<iframe width="100%" height="500" src="https://www.youtube.com/embed/dwM0EW825JU" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

Las fuerzas marcadas como incógnita se renderizan en rojo, para que el programa las calcule o bien se puede pulsar la tecla ```P``` con el objeto seleccionado o pulsando el botón de ```Solucionar``` que hay en el panel de propiedades del objeto.