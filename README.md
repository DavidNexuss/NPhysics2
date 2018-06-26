# NPhysics  
[![Build Status](https://travis-ci.com/DavidNexuss/NPhysics2.svg?branch=master)](https://travis-ci.com/DavidNexuss/NPhysics2)  
Simulador de f�sicas escrito en java, usa [Libgdx](https://libgdx.badlogicgames.com/), y [Earcut4j](https://github.com/earcut4j/earcut4j)  

[![Simulador de situaciones din�micas](https://img.youtube.com/vi/MVjDmQk1HLE/0.jpg)](https://www.youtube.com/watch?v=MVjDmQk1HLE)  
YT: https://www.youtube.com/watch?v=MVjDmQk1HLE

## �C�mo funciona?  
El objetivo del simulador es que puedas resolver problemas de est�tica, dibujando los objetos: s�lidos, cuerdas, puntos de reacci�n, articulaciones y m�s..  
Una vez dibujados el programa calculara las fuerzas que tu indicaras como inc�gnita para que su valor de lugar a una situaci�n est�tica. Esto se verificara en una simulacion en tiempo real del problema, d�nde se verifica si realmente no hay movimiento (usando la biblioteca de JBox2D de Libgdx).

Posteriormente puedes editar el valor de las fuerzas a mano, para ver el resultado en la simulaci�n.
