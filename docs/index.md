NPhysics[](title)
# Simulador de dinàmica amb una calculadora de estàtica
---  
# Abstract

## Català

Aquest és un treball de recerca de batxillerat, el propòsit d'aquest es la creació d'un simulador de dinàmica que permeti resoldre problemes d'estàtica plantejats per l'usuari, un conjunt de problemes físics i mecànics on l'objectiu que es busca es trobar l'equilibri de les forces en el sistema. Una vegada aquest solucione el sistema s'executarà una simulació en temps real on es suposarà que es una situació dinàmica i es comprovarà si realment hi ha moviment. Tant la simulació com el plantejament del problema es duran a terme en un entorn gràfic i de fàcil ús. 

## Castellano

Este es un trabajo de investigación de bachillerato, el propósito de este es la creación de un simulador de dinámica que permita resolver problemas de estática planteados por el usuario, un conjunto de problemas físicos y mecánicos donde el objetivo que se busca es encontrar el equilibrio de las fuerzas en el sistema. Una vez este solucione el sistema ejecutará una simulación en tiempo real donde se supondrá que es una situación dinámica y se comprobará si realmente hay movimiento. Tanto la simulación como el planteamiento del problema se llevarán a cabo en un entorno gráfico y de fácil uso.

## English

This is a baccalaureate research project, the purpose of this is to create a dynamic simulator that allows solving static problems posed by the user, a set of physical and mechanical problems where the objective is to find the balance of the forces in the system. Once this solves the system, it will execute a simulation in real time where it will be assumed that it is a dynamic situation and it will be checked if there is fact any movement. Both the simulation and the approach of the problem will be carried out in a graphical friendly environment

# Índex

1. Introducció
2. Plantejament del programa
3. Elecció del software de desenvolupament
    - Llenguatge de programació
    - Biblioteques
        - Elecció de la API gràfica per renderitzar
        - Elecció de la biblioteca per executar la simulació dinàmica
        - Configuració de totes les dependencies.
    - Elecció d'un entorn de desenvolupament
4. Desenvolupament del entorn d'edició
    - Mètodes d'entrada
    - Renderització 
        - Projecció ortogràfica utilitzant matrius 3x3
        - Programació de la *graphics pipeline*
5. Desenvolupament del entorn interactiu de la simulació dinàmica
    - Interacció amb Box2D
        - Triangulació dels objectes
        - Aplicació de forces predefinides
        - Aplicació de moments predefinits
        - Aplicació de joints
    

# Introducció

Com ja he explicat al abstract el objectiu d'aquest treball es desenvolupar un programa que permeti resoldre problemes d'estàtica i comprovar els resultats en un simulador de dinàmica amb una interfície senzilla d'utilitzar. La meva motivació per emprendre aquest projecte sorgeix en part de l'emoció de crear un programa robust de codi lliure que permeti fer simulacions de dinàmica i per l'altra part per poder crear una ferramenta amb propòsits educatius que serveixi per apropar la mecànica a tota perona interessada.

Amb els ja adquirits coneixements de C++ i Java estic motivat per fer aquest gran projecte.

Per tant, els objectius del programa són:

- La interacció de l'usuari amb el simulador de dinàmica

- La possibilitat de resoldre problemes de estàtica i poder comprovar la solució al simulador 

