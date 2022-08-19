package ve.usb.libGrafo

/* 
   Implementación del algoritmo DFS. 
   Con la creación de la instancia, se ejecuta el algoritmo DFS
   desde todos los vértices del grafo
*/
public class DFS(val g: Grafo) {
    val discovered: MutableList<Int>
    val finished: MutableList<Int>
    val pred: MutableList<Int?>
    val color: MutableList<Color>
    var time: Int

    init {
        discovered = mutableListOf()
        finished = mutableListOf()
        pred = mutableListOf()
        color = mutableListOf()

        // inicializamos las listas
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            discovered.add(Int.MAX_VALUE)
            finished.add(Int.MAX_VALUE)
            pred.add(null)
            color.add(Color.BLANCO)
        }
        
        time = 0

        for (i in 0 until g.obtenerNumeroDeVertices()) {
            if (color[i] == Color.BLANCO) {
                dfsVisit(g, i)
            }
        }
    }

    private fun dfsVisit(g: Grafo, u: Int) {
        time++
        discovered[u] = time
        color[u] = Color.GRIS

        for (lado in g.adyacentes(u)) {
            val v: Int = lado.b

            if (color[v] == Color.BLANCO) {
                pred[v] = u 
                dfsVisit(g, v)
            }
        }

        color[u] = Color.NEGRO
        time++
        finished[u] = time
    }

    /*
        Se devuelve el predecesor del vertice v.

        {P: El vertice v pertenece al grafo}
        {Q: true}
        
        Input: v -> Entero que representa un vertice
        Output: El predeecsor obtenido en dfs del vertice v
    */    
    fun obtenerPredecesor(v: Int) : Int? {
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("DFS.obtenerPredecesor(): El vertice $v no pertenece al grafo")
        }

        return pred[v]
    }

    /*
        Devuelve un par que contiene el tiempo incical y final del vertive v
        obtenido en DFS

        {P: El vertice v pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: Par (v.d, v.f)
    */
    fun obtenerTiempos(v: Int) : Pair<Int, Int> {
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("DFS.obtenerTiempos(): El vertice $v no pertenece al grafo")
        }

        return Pair(discovered[v], finished[v])
    }

    /*
        Determina si hay un camino desde el vertice u hasta el vertice v

        {P: Los vertice u y v pertenecen al grafo}
        {Q: true}

        Input: u -> Entero que representa un vertice del grafo
        Output: v -> Entero que representa un vertice del grafo
    */ 
    fun hayCamino(u: Int, v: Int) : Boolean {
        if (u < 0 || u >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("DFS.hayCamino(): El vertice $u no pertenece al grafo")
        }

        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("DFS.hayCamino(): El vertice $v no pertenece al grafo")
        }

        return discovered[u] < discovered[v] && finished[v] < finished[u] 
    }

    /*
        Devuelve un iterable del camino desde u hasta v

        {P: u y v son vertices del grafo}
        {Q: }
    */ 
    fun caminoDesdeHasta(u: Int, v: Int) : Iterable<Int>  {
        if (!this.hayCamino(u, v)) {
            throw RunTimeException("DFS.caminoDesdeHasta(): No hay camino desde el vertice $u hasta el vertice $v")
        }

        var verAux: Int = v
        val camino: MutableList<Int> = mutableListOf()
        while (verAux != u) {
            camino.add(0, verAux)
            verAux = pred[verAux]!!
        }

        camino.add(0, verAux)

        return camino.asIterable()
    }

    // Retorna true si hay lados del bosque o false en caso contrario.
    fun hayLadosDeBosque(): Boolean {
        for (i in pred) if (i != null) return true
        return false
    }
    
    // Retorna los lados del bosque obtenido por DFS.
    // Si no existen ese tipo de lados, entonces se lanza una RuntimeException.
    fun ladosDeBosque() : Iterator<Lado> {
        if (!this.hayLadosDeBosque()) {
            throw RunTimeException("DFS.ladosBosque(): No hay lados bosque en el grafo")
        }
        
        var lados: MutableList<Lado> = mutableListOf()

        for (lado in g) {
            val u: Int = lado.a 
            val v: Int = lado.b 

            if (pred[v] == u) lados.add(lado)
        }

        return lados.iterator()
    }

    // Retorna true si hay forward edges o false en caso contrario.
    fun hayLadosDeIda(): Boolean {
        for (lado in g) {
            val u: Int = lado.a
            val v: Int = lado.b 

            if (pred[v] != u && discovered[u] < discovered[v] && finished[v] < finished[u]) {
                return true
            }
        }

        return false
    }
    
    // Retorna los forward edges del bosque obtenido por DFS.
    // Si no existen ese tipo de lados, entonces se lanza una RuntimeException.
    fun ladosDeIda() : Iterator<Lado> {
        if (!this.hayLadosDeIda()) {
            throw RunTimeException("DFS.ladosDeIda: No hay lados de ida en el grafo")
        }

        val lados: MutableList<Lado> = mutableListOf()

        for (lado in g) {
            val u: Int = lado.a 
            val v: Int = lado.b 

            if (pred[v] != u && discovered[u] < discovered[v] && finished[v] < finished[u]) {
                lados.add(lado) 
            }
        }

        return lados.iterator()
    }

    // Retorna true si hay back edges o false en caso contrario.
    fun hayLadosDeVuelta(): Boolean {  
        for (lado in g) {
            val u: Int = lado.a 
            val v: Int = lado.b 

            if (discovered[v] < discovered[u] && finished[u] < finished[v]) {
                return true
            }
        }

        return false 
    }
    
    // Retorna los back edges del bosque obtenido por DFS.
    // Si no existen ese tipo de lados, entonces se lanza una RuntimeException.
    fun ladosDeVuelta() : Iterator<Lado> {
        if (!this.hayLadosDeVuelta()) {
            throw RunTimeException("DFS.ladosDeVuelta(): No hay lados de vuelta en el grafo")
        }

        var lados: MutableList<Lado> = mutableListOf()

        for (lado in g) { 
            val u: Int = lado.a 
            val v: Int = lado.b 

            if (discovered[v] <= discovered[u] && finished[u] <= finished[v]) {
                lados.add(lado)
            }
        }

        return lados.iterator()
    }

    // Retorna true si hay cross edges o false en caso contrario.
    fun hayLadosCruzados(): Boolean {
        for (lado in g) {
            val u: Int = lado.a 
            val v: Int = lado.b 

            if (finished[v] < discovered[u]) return true
        }

        return false
    }
    
    // Retorna los cross edges del bosque obtenido por DFS.
    // Si no existen ese tipo de lados, entonces se lanza una RuntimeException.
    fun ladosCruzados() : Iterator<Lado> {
        if (!this.hayLadosCruzados()) {
            throw RunTimeException("DFS.ladosCruzados(): No hay lados cruzados en el grafo")
        }

        val lados: MutableList<Lado> = mutableListOf()

        for (lado in g) {
            val u: Int = lado.a 
            val v: Int = lado.b 

            if (finished[v] < discovered[u]) lados.add(lado)
        }

        return lados.iterator()
    }

    // Imprime por la salida estándar el depth-first forest.
    fun mostrarBosqueDFS() {
        for (lado in this.ladosDeBosque()) {
            println("${lado.toString()} ")
        }

        println()
    }
    
}
