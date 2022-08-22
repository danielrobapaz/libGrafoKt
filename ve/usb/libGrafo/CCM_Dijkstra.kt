package ve.usb.libGrafo

/*
 Implementación del algoritmo de Dijkstra para encontrar los
 caminos de costo mínimo desde un vértice fuente s fijo. La
 implementación debe usar como cola de prioridad un min-heap o un fibonacci heap.
 Si el grafo de entrada tiene un lado con costo negativo,  entonces se retorna una RuntimeException.
 Si el vértice s no pertenece al grafo, entonces se retorna una RuntimeException.
*/
public class CCM_Dijkstra(val g: GrafoDirigidoCosto, val s: Int) {
    val dist: MutableList<Double>
    val pred: MutableList<Int?>

    init {
        if (s < 0 || s >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("CCM_Dijkstra.init(): El vertice $s no pertenece al grafo")
        }

        for (lado in g) {
            if (lado.obtenerCosto() < 0) {
                throw RunTimeException("CCM_Dijkstra.init(): El grafo dado tiene un lado de costo negativo")
            }
        }

        dist = mutableListOf()
        pred = mutableListOf()
        // Inicializamos fuente fija
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            dist.add(Double.POSITIVE_INFINITY)
            pred.add(null)
        }
        dist[s] = 0.0

        // creamos y llenamos la cola de prioridad
        val q: ColaDePrioridad = ColaDePrioridad()
        for (i in 0 until g.obtenerNumeroDeVertices()) q.add(i, dist[i])

        while (!q.vacia()) {
            val u: Int = q.extraerMinimo()

            for (lado in g.adyacentes(u)) {
                val v: Int = lado.b 

                if (dist[v] > dist[u] + lado.obtenerCosto()) { // relajacion
                    dist[v] = dist[u] + lado.obtenerCosto()
                    pred[v] = u
                    q.decreaseKey(v, dist[v])
                }
            }
        }
    }

    /* 
        Determina si existe un camino desde el vertice fuente s
        hasta el vertice v

        {P: v es un vertice del grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice
        Output: true -> Si existe un camino desde s hasta v
    */
    fun existeUnCamino(v: Int) : Boolean { 
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("CCM_Dijkstra.existeUnCamino(): El vertice $v no pertenece al grafo")
        }

        return v == s || pred[v] != null
    }

    /* 
        Devuelve el costo del camino de costo minimo desde s hasta v

        {P: v es un vertice del grafo y existe un camino de s a v}
        {Q: true}

        Input: v -> Entero que representa un vertice
        Output: v.d
    */
    fun costo(v: Int) : Double { 
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("CCM_BellmanFord.costo(). El vertice $v no pertenece al grafo")
        }

        return dist[v]
    }

    /* 
        Dado un vertice, se devuelve el predecesor del vertice

        {P: El vertice v pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: El predecesor del vertice dado en el camino de costo minimo

        Tiempo de ejecucion O(1)
    */
    fun obtenerPredecesor(u: Int): Int? {
        if (u < 0 || u >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("CCM_Dijkstra.obtenerNumeroDeVertices(): El vertice $s no pertenece al grafo")
        }

        return pred[u]
    }

    /* 
        Devuelve los lados del camino de costo minimo desde s hasta v

        {P: v es un vertice del grafo y existe un camino de s hasta v}
        {Q: true}

        Input: v -> Entero que representa un vertice
        Output: Un iterable sobre los lados del ccm de s hasta v
    */
    fun obtenerCaminoDeCostoMinimo(v: Int) : Iterable<ArcoCosto> { 
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("CCM_BellmanFord.obtenerCaminoDeCostoMinimo(). El vertice $v no pertenece al grafo")
        }

        if (!this.existeUnCamino(v)) {
            throw RunTimeException("CCM_BellmanFord.obtenerCaminoDeCostoMinimo(): No existe camino desde $s hasta $v")
        }

        // obtenermos el camino p = <s, v1, v2, ..., v>
        val camino: MutableList<Int> = mutableListOf()
        var currVer: Int = v
        while (currVer != s) {
            camino.add(0, currVer)
            currVer = pred[currVer]!!
        }
        camino.add(0, currVer)

        // Obtenemos la secuencia de lados ccm = <(s, v1), (v1, v2), ... (vn, v)>
        val ccm: MutableList<ArcoCosto> = mutableListOf()
        for (i in 0 until camino.size - 1) {
            ccm.add(ArcoCosto(camino[i], camino[i + 1], dist[camino[i + 1]] - dist[camino[i]]))
        }

        return ccm.asIterable()
    }
}