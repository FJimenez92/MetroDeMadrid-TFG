1.- Introducción

1.1.- Objetivo

El propósito general del presente Trabajo de Fin de Grado será el de desarrollar un sistema de toma de decisiones en un grafo, concretamente un sistema que encuentre el camino más corto entre dos estaciones de una red de metro de una ciudad. Esta aplicación encontrará el trayecto de menor tiempo entre dos puntos sirviéndose del algoritmo de búsqueda A* estudiado en la asignatura de Inteligencia Artificial. Así mismo, durante el Trabajo de Fin de Grado, se aplicaron las técnicas de desarrollo, control y gestión de proyectos estudiadas en las asignaturas de Interacción Persona-Ordenador, e Ingeniería de Software para mejora y optimización del tiempo de desarrollo.

La red de Metro elegida para el proyecto es la del Metro de Madrid, con motivo del centenario de su inauguración, y la representación gráfica de la plataforma será ejecutada a través de una aplicación de escritorio en Java, a través de la cual el usuario selecciona dos estaciones de la red, una de comienzo del trayecto y una de final, y mediante el algoritmo de búsqueda descrito anteriormente el programa calculará y mostrará el trayecto más corto, contando con los posibles transbordos asociados a las estaciones. Además, no solo calculará el trayecto que menor tiempo emplearía, sino que además estimaremos el tiempo que tomaría realizar dicho trayecto, ya que utilizaremos herramientas de geoposicionamiento y, ayudados de datos de velocidades medias de los trenes, intentaremos ofrecer un tiempo aproximado de trayecto para la distancia que separa ambas estaciones.

2.- Trabajos previos

Para el desarrollo del trabajo planteado, uno de los puntos críticos es una correcta definición de la base de datos de las estaciones que conformarán el plano de Metro. Para el funcionamiento del algoritmo, es necesario identificar cada una de las estaciones en su localización real para poder calcular la distancia tanto entre estaciones adyacentes como la distancia hasta la estación objetivo.

En primer lugar se optó por buscar un dataset que facilitara la mayor información posible, como de localización como de transbordos, por lo que se partió del dataset que proporciona la Comunidad de Madrid en su portal. Sin embargo, a pesar de la cantidad de información de la que disponía (accesos para minusválidos, aparcamientos, etc), este dataset no reunía la información de los transbordos ni sus coordenadas, al menos de una manera clara y concisa, por lo que se optó por la generación de un dataset propio que contuviese la información que el algoritmo A* necesita para su correcto funcionamiento.

Así pues, el dataset, para el que se escogió el CSV como tipo de archivo por su comodidad tanto para leer como para escribir datos en él, debía estar estructurado de la siguiente manera: cada una de las líneas del documento representará un nodo del algoritmo, teniendo las estaciones con transbordo tantos nodos como líneas confluyan en dicha estación. Así pues, para cada nodo, se define la línea a la que pertenece el nodo, su nombre, y sus coordenadas X e Y. De esta manera, el algoritmo iterará entre los nodos más cercanos de una misma línea, y podrá realizar transbordos de línea entre nodos cuya localización sea la misma.

Para la obtención de las coordenadas X e Y, se ha utilizado la herramienta de localización de Google Maps para cada una de las estaciones de la red de metro, mientras que para la información de los transbordos se ha optado por transcribirla a mano desde la página web del Metro de Madrid (www.metrodemadrid.com).

Por último, uno de los problemas para la implementación del algoritmo fue la necesidad de calcular el tiempo empleado en los transbordos para una estimación más ajustada a la realidad del camino mínimo. Desafortunadamente, no existe información acerca de las distancias de los transbordos puesto que dependen de la arquitectura de cada estación. Por tanto, se optó por una estimación que diera una solución a este problema: se ha definido una jerarquía entre las distintas líneas, simulando una distancia entre ellas estándar en función de la línea a la que pertenecen. De esta manera, se ha definido que cada línea tiene una profundidad bajo tierra uniforme, siendo la línea 1 la que menor profundidad tiene y, la 12, la que más. Así, podemos calcular fácilmente la distancia entre una línea y otra dentro de la misma estación.

3.- EL ALGORITMO A*

3.1.- Definición

Un algoritmo de búsqueda es aquel encargado de identificar un elemento con unas características concretas dentro de un grupo de datos. El algoritmo A* es un tipo de algoritmo de búsqueda empleado para el cálculo de caminos mínimos en una red. Es un algoritmo heurístico; es decir, se vale de una función de evaluación heurística para realizar los cálculos, mediante los cuales etiquetará los diferentes nodos de la red y determinará la probabilidad de dichos nodos de pertenecer al camino óptimo.

Para el algoritmo A* disponemos de dos funciones para dicha evaluación, una de ellas para calcular la distancia al siguiente nodo, y una segunda para calcular la distancia hasta el nodo destino. Por tanto, si se desea encontrar el camino más corto desde el nodo de origen S hasta el nodo destino T, un nodo intermedio N de la red queda definido por la siguiente función:

f(N)= g(N) + h(N)

Siendo g(N) la distancia del camino desde el nodo de origen S hasta el nodo intermedio N, y h(N) la distancia desde el nodo intermedio N hasta el nodo de destino T.

Así pues, cuando menor sea el valor de la función f de un nodo concreto, mayor probabilidad tendrá de encontrarse en el camino mínimo que se busca. El algoritmo debe mantener una lista ordenada por valor creciente de mérito de los nodos que pueden ser explorados, y de ahí seleccionará el de menor valor, que será el primero de la lista. El algoritmo empezará analizando el nodo que se toma como origen para el problema del camino más corto. Calculará su mérito, y a continuación pasará a explorar sus nodos sucesores, es decir, los nodos con los que esté unido por un enlace este nodo de origen. De esos nodos con los que tenga nexo se calculará su mérito, y se continuará con la ejecución del algoritmo evaluando los adyacentes al de mayor mérito. Cuando el algoritmo evalúa el nodo destino del problema será el momento en que el algoritmo termine y ya se dispondría de la solución óptima.

3.2.- Desarrollo

Una vez consolidada la lógica y la estructura de datos que define cada estación de metro, se procede a plasmar en código el funcionamiento del algoritmo A*. La lógica del algoritmo permite dos maneras distintas de afrontar los cálculos: ponderando los nodos por distancia o por tiempo empleado en recorrerla, siendo esta última la empleada para el proyecto, sabiendo que el Metro de Madrid tiene una velocidad media de 31km/h. Para estimar el tiempo empleado en recorrer la distancia entre dos estaciones, damos por hecho que el recorrido se hace en línea recta, puesto que las vías por las que circulan los trenes no suelen tener curvas significativas.

Para el cálculo del camino mínimo entre ambas estaciones se ha utilizado una lista cerrada y una lista abierta. La lista abierta la hemos implementado con una PriorityQueue (biblioteca propia de la asignatura de Algoritmos y Estructura de Datos). Esta cola con prioridad nos permite, con un simple método como es removeMin(), obtener y eliminar el elemento de menor valor de esta. En cuanto a la lista cerrada con un simple array de Stations nos sirve, puesto que lo único que nos interesa es saber las paradas que contiene.

Más específicamente para el cálculo de la función heurística, lo primero a realizar es calcular la distancia del nodo inicial al nodo destino. Dicha distancia se obtiene desde el anterior nodo consolidado hasta el nodo actual en línea recta. Posteriormente, obtenemos la distancia en línea recta entre el nodo actual y el destino, por lo que el valor de f es la suma de ambas distancias, como quedó definido anteriormente.

