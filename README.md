# ¿Qué es NPhysics?

Este es un trabajo de investigación de bachillerato, el propósito de este es la creación de un Simulador de Dinámica que permita resolver problemas de estática planteados por el usuario, un conjunto de problemas físicos y mecánicos donde el objetivo que se busca es encontrar el equilibrio de las fuerzas en el sistema. Una vez este solucione el sistema ejecutará una simulación en tiempo real donde se supondrá que es una situación dinámica y se comprobará si realmente hay movimiento. Tanto la simulación como el planteamiento del problema se llevarán a cabo en un entorno gráfico y de fácil uso.

[![Simulador de situaciones dinámicas](https://raw.githubusercontent.com/DavidNexuss/NPhysics2/master/desktop/assets/logo.png)](https://www.youtube.com/watch?v=aNLhFl5YnPc)  
YT: https://www.youtube.com/watch?v=aNLhFl5YnPc  

## Cómo funciona?  
El objetivo del simulador es que puedas resolver problemas de estática, dibujando los objetos: sólidos, cuerdas, puntos de reacción, articulaciones y más..  
Una vez dibujados el programa calculara las fuerzas que tu indicaras como incógnita para que su valor de lugar a una situación estática. Esto se verificara en una simulacion en tiempo real del problema, dónde se verifica si realmente no hay movimiento (usando la biblioteca de JBox2D de Libgdx).

Posteriormente puedes editar el valor de las fuerzas a mano, para ver el resultado en la simulación.
# Instalación

NPhysics es un programa portable, no es necesaria ninguna interfaz de instalación y se puede ejecutar desde la carpeta contenedora del programa. Se puede o bien descargar la última versión estable ya compilada desde GitHub o compilar el proyecto por uno mismo.

## Descargar

El programa se puede descargar desde la página de GitHub en su última versión estable ya compilada, en cada lanzamiento existen 3 versiones del programa comprimido:

+ *NPhysics.zip* (Para descomprimir usando cualquier programa de descompresión)
+ *NPhysics.7z* (Para descomprimir usando 7z)
+ *NPhysics.exe* (Autoejecutable para Windows que descomprime el programa)

NPhysics esta escrito en Java lo que permite que el programa sea multiplataforma, se puede ejecutar en cualquier distribución de Linux, Windows y MacOS.

Una vez descargado y descomprimido solo hay que ejecutar el archivo *NPhysics.jar*, por defecto el programa se ejecuta en pantalla pero se puede ejecutar en modo ventana con el archivo *window.bat* (solo en Windows).

También se puede ejecutar desde un terminal escribiendo la orden ```java -jar NPhysics.jar```, en pantalla completa, y ```java -jar NPhysics.jar window```, en modo ventana. 
## Para compilar

En caso que se desee usar la última versión del programa en su último commit se puede compilar el programa directamente desde el repositorio.
El proyecto es gestionado usando gradle. Para compilar los archivos binarios solo hay que ejecutar la siguiente orden en la raíz del repositorio:  

```gradlew desktop:dist```  

Esta ordena compilará el programa y copiara el ejecutable en la carpeta ```desktop/build/libs```, esta es la carpeta en la que se generará el programa compilado cada vez.  
Una vez compilado hay que copiar todo el contenido de la carpeta ```desktop/assets``` a la carpeta donde se encuentra el compilado.  

### Para ejecutar

Para ejecutar el programa ya compilado solo hay que ejecutar la orden ```java -jar desktop-1.0```  
