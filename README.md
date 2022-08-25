# libGrafoKt
libreria de algoritmos de grafos (algos 3)

Contiene la implementacion del tipo de dato grafo y algoritmos sobre grafos

Los grafos de pueden generar de dos maneras.
    1. Con el numero de vertices (se cargan los grafos manualmente)
    2. Desde un archivo

    El formato del archivo es el que sigue:
        En la primera linea el numero de vertices del grafo
        En la segunda linea el numero de lados
        Luego siguen tantas lineas como lados tenga el grafo
        
        Si el grafo es sin costo el formato de cada linea es dos enteros que representan cada vertices separados por un espacio
        Si el grafo es con costo el formato de cada linea es dos enteros y un real separados por un espacio.

        Los enteros representan los extremos de los lados. Y el real representa el costo del lado

Cada algoritmo en encuentra implementado como clases. Los algoritmos se ejecutan al momento de crear una instancia de la clase

Para cada clase (algoritmo) se tienen metodos que trabajan con los resultado del algoritmo. Por ejemplo luego de ejecutar DFS, hay metodos para hallar caminos entre vertices.