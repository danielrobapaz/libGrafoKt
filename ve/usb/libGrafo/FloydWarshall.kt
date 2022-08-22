package ve.usb.libGrafo

/*
    Implementación del algoritmo de Floyd-Warshall para encontrar los
    caminos de costo mínimo todos los pares de vértices de un grafo.
    El constructor recibe como entrada una matriz de costos, que corresponde
    a la matriz de costos asociado a un digrafo con pesos en los lados.
    La matriz de costos es construida con la función dada en clase.
    Se asume que la matriz de costos corresponde a un digrafo sin ciclos negativos.
    Si el digrafo tiene ciclo negativo, el resultado del algoritmo no es especificado.
    La matriz de entrada es cuadrada de dimensiones nxn, donde n es el número de 
    vértices del grafo. Si la matriz de entrada no es cuadrada, entoces se lanza una RuntimeException.
*/
public class FloydWarshall(val W : Array<Array<Double>>) {
    val dMatriz: Array<Array<Double>>
    val predMatriz: Array<Array<Int?>>
    val nVer: Int

    init {
        nVer = W.size
        for (r in W) {
            if (r.size != nVer) {
                throw RunTimeException("FloydWarshall.init(): La matriz dada de no es cuadrada")
            }
        }

        dMatriz = W 
        predMatriz = Array<Array<Int?>>(nVer, {Array<Int?>(nVer, {null})})
        for (i in 0 until nVer) {
            for (j in 0 until nVer) {
                if (i == j || dMatriz[i][j] == Double.POSITIVE_INFINITY) {
                    predMatriz[i][j] = null
                } else {
                    predMatriz[i][j] = i
                }
            }
        }

        for (k in 0 until nVer) {
            for (i in 0 until nVer) {
                for (j in 0 until nVer) {
                    if (dMatriz[i][j] > dMatriz[i][k] + dMatriz[k][i]) {
                        dMatriz[i][j] = dMatriz[i][k] + dMatriz[k][i]
                        predMatriz[i][j] = predMatriz[k][j]
                    }
                }
            }
        }
    }

    // Retorna la matriz con las distancias de los caminos de costo mínimo
    // entre todos los pares de vértices.
    fun obtenerMatrizDistancia() : Array<Array<Double>> = dMatriz

    // Retorna la matriz con los predecesores de todos los vértices en los caminos de costo mínimo
    // entre todos los pares de vértices.
    fun obtenerMatrizPredecesores() : Array<Array<Int?>> = predMatriz
    
    /* 
        Devuelve el costo del camino de costo minimo desde el vertice u
        hasta el vertice v

        {P: u y v son vertices del grafo
            existe un camino desde u hasta v}
        {Q: true}

        Input: u -> Entero que representa un vertice del grafo
               v -> Entero que representa un vertice del grafo
        Output: El costo del camino de costo minimo desde u hasta v

        Tiempo de ejecucion O(1)
    */
    fun costo(u: Int, v: Int) : Double { 
        if (u < 0 || u >= nVer) {
            throw RunTimeException("FloydWarshall.costo(): El vertice $u no pertenece al grafo")
        }

        if (v < 0 || v >= nVer) {
            throw RunTimeException("FloydWarhsall.costo(): El vertice $v no pertenece al grafo")
        }

        return dMatriz[u][v]
    }

    /* 
        Determina si hay un camino desde u hasta v

        {P: u y v son vertices del grafo}
        {Q: true}

        Input: u -> Entero que representa un vertice del grafo
               v -> Entero que representa un vertice del grafo
        Output: true si existe un camino desde u hasta v
                false caso contrario
        
        Tiempo de ejecucion O(1)
    */
    fun existeUnCamino(u: Int, v: Int) : Boolean {
        if (u < 0 || u >= nVer) {
            throw RunTimeException("FloydWarshall.existeUnCamino(): El vertice $u no pertenece al grafo")
        }

        if (v < 0 || v >= nVer) {
            throw RunTimeException("FloydWarhsall.existeUnCamino(): El vertice $v no pertenece al grafo")
        }

        return u == v || predMatriz[u][v] != null
    }

    /* 
        Devuelve los arcos del camino de costo minimo desde u hasta v

        {P: u y v son vertices del grafo
            existe un camino desde u hasta v}
        {Q: true}

        Input: u -> Entero que representa un vertice del grafo
               v -> Entero que representa un vertice del grafo
        Output: Un iterable de los arcos del camino de costo minimo desde y hasta v
    
        Tiempo de ejecucion O(W.rows)
    */
    fun obtenerCaminoDeCostoMinimo(u: Int, v: Int) : Iterable<Arco> {
        if (!this.existeUnCamino(u, v)) {
            throw RunTimeException("FloydWarshall.obtenerCaminoDeCostoMinimo(): No existe un camino desde u hasta v")
        }

        var currVer: Int = v
        val camino: MutableList<Arco> = mutableListOf()
        while (currVer != u) {
            val pred: Int? = predMatriz[u][currVer]
            camino.add(Arco(pred!!, currVer))
            currVer = pred
        }

        return camino.asIterable()
    }
}
