# NPhysics  
[![Build Status](https://travis-ci.com/DavidNexuss/NPhysics2.svg?branch=master)](https://travis-ci.com/DavidNexuss/NPhysics2)  
Simulador de físicas escrito en java.

Usa las siguientes bibliotecas:  
[Libgdx](https://libgdx.badlogicgames.com/)  
[Earcut4j](https://github.com/earcut4j/earcut4j)  
[JTS](https://github.com/locationtech/jts)  

[![Simulador de situaciones din�micas](https://img.youtube.com/vi/MVjDmQk1HLE/0.jpg)](https://www.youtube.com/watch?v=MVjDmQk1HLE)  
YT: https://www.youtube.com/watch?v=MVjDmQk1HLE

## Cómo funciona?  
El objetivo del simulador es que puedas resolver problemas de estática, dibujando los objetos: sólidos, cuerdas, puntos de reacción, articulaciones y más..  
Una vez dibujados el programa calculara las fuerzas que tu indicaras como incógnita para que su valor de lugar a una situación estática. Esto se verificara en una simulacion en tiempo real del problema, dónde se verifica si realmente no hay movimiento (usando la biblioteca de JBox2D de Libgdx).

Posteriormente puedes editar el valor de las fuerzas a mano, para ver el resultado en la simulación.  

## Para compilar

El proyecto es gestionado usando gradle. Para compilar solo hay que ejecutar:
(En windows gradlew, en linux y mac gradle)  

```shell  gradle desktop:dist``` Para compilar el .jar  
```shell  gradle html:dist``` Para compilar la app web

