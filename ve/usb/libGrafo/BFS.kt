package ve.usb.libGrafo
import java.util.LinkedList
import java.util.Queue
/* 
   Implementación del algoritmo BFS. 
   Con la creación de la instancia, se ejecuta el algoritmo BFS
   desde el vértice s

   Si el vertice s no pertenece al grafo se lanza RunTimeException
*/
public class BFS(val g: Grafo, val s: Int) {
    val dist: MutableList<Int>
    val pred: MutableList<Int?>
    val color: MutableList<Color>

    init {
        if (s < 0 || s >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("BFS.init(): El vertice $s no pertenece al grafo")
        }

        dist = mutableListOf()
        pred = mutableListOf()
        color = mutableListOf()

        // inicializamos las listas
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            dist.add(Int.MAX_VALUE)
            pred.add(null)
            color.add(Color.BLANCO)
        }

        // inicializamos el vertice fuente
        dist[s] = 0
        pred[s] = null
        color[s] = Color.GRIS

        val cola: Queue<Int> = LinkedList<Int>(listOf())
        cola.add(s)

        while (!cola.isEmpty()) {
            val u: Int = cola.remove()
            for (lado in g.adyacentes(u)) {
                val v: Int = lado.b 
                if (color[v] == Color.BLANCO) {
                    color[v] = Color.GRIS
                    dist[v] = dist[u] + 1
                    pred[v] = u
                    cola.add(v)
                }
            }

            color[u] = Color.NEGRO
        }
    }

    /*
        Se devuelve el predecesor del vertice v en el arbol de predecesores
    */
    fun obtenerPredecesor(v: Int) : Int? {
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("BFS.obtenerPredecesro(): El vertice $v no pertenece al grafo")
        }

        return pred[v]
    }

    /*
     Retorna la distancia, del camino obtenido por BFS, desde el vértice inicial s 
     hasta el un vértice v. En caso de que el vétice v no sea alcanzable desde s,
     entonces se retorna -1.
     En caso de que el vértice v no exista en el grafo se lanza una RuntimeException. 
     */
    fun obtenerDistancia(v: Int) : Int {
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("BFS.obtenerDistancia(): El vertice $v no pertenece al grafo")
        }

        if (hayCaminoHasta(v)) return dist[v]
        
        return -1
    }

    /*
        Determina si hay un camino desde el vertice s hasta el vertice v.

        Para esto vemos los predecesores. Si el predecesor de v no es nulo,
        existe un camino o si el vertice v es el vertice fuente.

        {P: El vertice v pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: true si hay un camino desde s hasta v
                false caso contrario
    */ 
    fun hayCaminoHasta(v: Int) : Boolean {
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("BFS.hayCaminoHasta(): El vertice $v no pertenece al grafo")
        }

        return pred[v] != null || v == s
    }

    /*
        Devuelve el camino de menos lados obtenido por BFS desde el vertice s
        hasta el vertice v

        {P: El vertice v pertenece al grafo,
            existe un camino desde s hasta v}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: Un iterable sobre los vertices del camino de menos lados
                desde el vertice s hasta v
    */ 
    fun caminoHasta(v: Int) : Iterable<Int>  {
        if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("BFS.caminoHasta(): El vertice $v no pertenece al grafo")
        }

        if (!this.hayCaminoHasta(v)) {
            throw RunTimeException("BFS.caminoHasta(): No hay camino desde el vertice $s hasta el vertice $s")
        }

        val camino: MutableList<Int> =  mutableListOf()
        var verAux: Int? = v 
        while(verAux != s) {
            camino.add(0, verAux!!)
            verAux = pred[verAux]!!
        }
        camino.add(0, verAux)

        return camino.asIterable()
    }

    // Imprime por la salida estándar el breadth-first tree
    fun mostrarArbolBFS() {
        println("ArbolBFS: ")
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            if (pred[i] != null) println("(${pred[i]}, $i)")
        }
    }
}
