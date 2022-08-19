package ve.usb.libGrafo

/*
 Implementación del Algoritmo de Bellman-Ford para encontrar los
 caminos de costo mínimo desde un vértice fuente s fijo.
 Si el vértice s no existe en el grafo se retorna un RuntimeException.
 */
public class CCM_BellmanFord(val g: GrafoDirigidoCosto, val s: Int) {
    val dist: MutableList<Double>
    val pred: MutableList<Int?>
    var ciclo: Boolean

    init {
        if (s < 0 || s >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("CCM_BellmanFord.init(): El vertice $s no pertenece al grafo")
        }
        var u: Int
        var v: Int

        dist = mutableListOf()
        pred = mutableListOf()
        ciclo = false

        // Inicializamos fuente fija s
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            dist.add(Double.POSITIVE_INFINITY)
            pred.add(null)
        }
        dist[s] = 0.0

        for (i in 0 until g.obtenerNumeroDeVertices()) {
            for (lado in g) {
                u = lado.a 
                v = lado.b

                relajacion(u, v, lado.obtenerCosto())
            }
        }

        for (lado in g) {
            u = lado.a 
            v = lado.b

            if (dist[v] > dist[u] + lado.obtenerCosto()) {
                ciclo = true
            }
        }
    }

    private fun relajacion(u: Int, v: Int, w: Double) {
        if (dist[v] > dist[u] + w) {
            dist[v] = dist[u] + w
            pred[v] = u
        }
    }

    // Retorna cierto si hay un ciclo negativo en el camino hasta los vértices alcanzables desde s
    fun tieneCicloNegativo() : Boolean = ciclo

    // Retorna los arcos del ciclo negativo con la forma <u, v>, <v, w>, ... ,<y, x>, <x, u>  
    //fun obtenerCicloNegativo() : Iterable<ArcoCosto> { }

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
            throw RunTimeException("CCM_BellmanFord.existeUnCamino(): El vertice $v no pertenece al grafo")
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

        if (!this.existeUnCamino(v)) {
            throw RunTimeException("CCM_BellmanFord.costo(): No existe camino desde $s hasta $v")
        }

        return dist[v]
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
