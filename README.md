# NPhysics  
[![Build Status](https://travis-ci.com/DavidNexuss/NPhysics2.svg?branch=master)](https://travis-ci.com/DavidNexuss/NPhysics2)  
Simulador de físicas escrito en java.

Usa las siguientes bibliotecas:  
[Libgdx](https://libgdx.badlogicgames.com/)  
[Earcut4j](https://github.com/earcut4j/earcut4j)  
[JTS](https://github.com/locationtech/jts)  

[![Simulador de situaciones dinámicas](https://raw.githubusercontent.com/DavidNexuss/NPhysics2/master/desktop/assets/logo.png)](https://www.youtube.com/watch?v=aNLhFl5YnPc)  
YT: https://www.youtube.com/watch?v=aNLhFl5YnPc

## Cómo funciona?  
El objetivo del simulador es que puedas resolver problemas de estática, dibujando los objetos: sólidos, cuerdas, puntos de reacción, articulaciones y más..  
Una vez dibujados el programa calculara las fuerzas que tu indicaras como incógnita para que su valor de lugar a una situación estática. Esto se verificara en una simulacion en tiempo real del problema, dónde se verifica si realmente no hay movimiento (usando la biblioteca de JBox2D de Libgdx).

Posteriormente puedes editar el valor de las fuerzas a mano, para ver el resultado en la simulación.  

## Para compilar

El proyecto es gestionado usando gradle. Para compilar solo hay que ejecutar:  

```gradlew desktop:dist``` Para compilar el .jar  
```gradlew html:dist``` Para compilar la app web

