<style>
.inner {width: 60%;} 
#main-content {width: 80%;}
</style>
## Welcome to GitHub Pages

Este es un trabajo de investigación de bachillerato, el propósito de este es la creación de un Simulador de Dinámica que permita resolver problemas de estática planteados por el usuario, un conjunto de problemas físicos y mecánicos donde el objetivo que se busca es encontrar el equilibrio de las fuerzas en el sistema. Una vez este solucione el sistema ejecutará una simulación en tiempo real donde se supondrá que es una situación dinámica y se comprobará si realmente hay movimiento. Tanto la simulación como el planteamiento del problema se llevarán a cabo en un entorno gráfico y de fácil uso.

### Ejemplo de un brazo robótico  
<iframe width="100%" height="500" src="https://www.youtube.com/embed/Fn4RER__GAM" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

## Para compilar

El proyecto es gestionado usando gradle. Para compilar solo hay que ejecutar:  

```gradlew desktop:dist``` Para compilar el .jar  
```gradlew html:dist``` Para compilar la app web

Usa las siguientes bibliotecas:  
[Libgdx](https://libgdx.badlogicgames.com/)  
[Earcut4j](https://github.com/earcut4j/earcut4j)  
[JTS](https://github.com/locationtech/jts)  