package ve.usb.libGrafo

/*
 Implementación del algoritmo de Johnson para encontrar los
 caminos de costo mínimo entre todos los pares de vértices de un grafo.
 El constructor recibe como entrada un digrafo con costos en los arcos.
 */
public class Johnson(val g: GrafoDirigidoCosto) {
    val dMatriz: Array<Array<Double>>
    val predMatriz: Array<Array<Int?>>
    val cicloNegativo: Boolean
    
    init {
        val s: Int = g.obtenerNumeroDeVertices()
        // Creamos gAux , tal que V' = V U {s}. 
        val gAux: GrafoDirigidoCosto = GrafoDirigidoCosto(s + 1)
        for (l in g) {
            gAux.agregarArcoCosto(l) // Agregamos los lados de de g
        }
        for (v in 0 until g.obtenerNumeroDeVertices()) {
            gAux.agregarArcoCosto(ArcoCosto(s, v, 0.0)) // Agregamos los arcos (s, v)
        }

        // Ejecutamos bellman ford
        val grafoBellmanFord: CCM_BellmanFord = CCM_BellmanFord(gAux, s)
        cicloNegativo = grafoBellmanFord.tieneCicloNegativo()
        if (cicloNegativo) {
            throw RunTimeException("Jhonson.init(): El grafo dado tiene un ciclo negativo")
        }

        val h: MutableList<Double> = mutableListOf()
        for (v in 0 until gAux.obtenerNumeroDeVertices()) {
            h.add(grafoBellmanFord.costo(v))
        }

        // Creamos un grafo modificado con los nuevo valores de los lados
        val gPrima: GrafoDirigidoCosto = GrafoDirigidoCosto(gAux.obtenerNumeroDeVertices())
        for (lado in gAux) {
            val u: Int = lado.a 
            val v: Int = lado.b
            gPrima.agregarArcoCosto(ArcoCosto(u, v, lado.obtenerCosto() + h[u] - h[v])) 
        }

        // Creamos nuestra matriz de costos y de predecesores
        dMatriz = Array<Array<Double>>(s, {Array(s, {Double.POSITIVE_INFINITY})})
        predMatriz = Array<Array<Int?>>(s, {Array(s, {null})})

        for (u in 0 until g.obtenerNumeroDeVertices()) {
            val dijkstra: CCM_Dijkstra = CCM_Dijkstra(gPrima, u) 
            for (v in 0 until g.obtenerNumeroDeVertices()) {
                dMatriz[u][v] = dijkstra.costo(v) + h[v] - h[u]
                predMatriz[u][v] = dijkstra.obtenerPredecesor(v)
            }
        }
    }
    
    // Retorna true si hay un ciclo negativo, false en caso contrario.
    fun hayCicloNegativo() : Boolean = cicloNegativo
    
    // Retorna la matriz con las distancias de los caminos de costo mínimo
    // entre todos los pares de vértices. Si hay un ciclo negativo se lanza un un RuntimeException.
    fun obtenerMatrizDistancia() : Array<Array<Double>> = dMatriz 

    // Retorna la matriz con los predecesores de todos los vértices en los caminos de costo mínimo
    // entre todos los pares de vértices.
    fun obtenerMatrizPredecesores() : Array<Array<Int?>> = predMatriz
    
    /* 
        Devuelve el costo del camino de costo minimo desde el vertice y hasta
        el vertice v

        {P: u y v son vertices del grafo}
        {Q: true}

        Input: u -> Entero que representa un vertice del grafo
               v -> Entero que representa un vertice del grafo
        Output: el costo del camino de costo minimo desde u hasta v

        Tiempo de ejecucion O(1)
    */
    fun costo(u: Int, v: Int) : Double {
        if (u < 0 || u >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("Jhonson.costo(): El vertice $u no pertenece al grafo")
        }

        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("Jhonson.costo(): El vertice $u no pertenece al grafo")
        }

        return dMatriz[u][v]
    }

    // Retorna cierto si hay un camino desde u hasta el vértice v.
    // Si alguno de los dos vértices  no existe, se retorna un RuntimeException.
    // Si hay un ciclo negativo se lanza un un RuntimeException.
    fun existeUnCamino(u: Int, v: Int) : Boolean { 
        if (u < 0 || u >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("Jhonson.existeUnCamino(): El vertice $u no pertenece al grafo")
        }

        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("Jhonson.existeUnCamino(): El vertice $v no pertenece al grafo")
        }

        return u == v || predMatriz[u][v] != null
    }

    /* 
        Devuelve los arcos del camino de costo minimo desde el vertice u 
        hasta el vertice v

        {P: u y  v son vertices del grafo
            existe un camino desde u hasta v}
    */
    fun obtenerCaminoDeCostoMinimo(u: Int, v: Int) : Iterable<Arco> { 
        if (!this.existeUnCamino(u, v)) {
            throw RunTimeException("Jhonson.obtenerCaminoDeCostoMinimo(): No existe un camino desde u hasta v")
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
