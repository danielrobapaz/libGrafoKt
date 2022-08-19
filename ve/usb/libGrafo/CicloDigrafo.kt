package ve.usb.libGrafo

/* 
   Determina la existencia de ciclo en grafos dirigiso usando
   dfs. 

   Para esto ejecutamos dfs de manera usual y revisamos si existen lados de vuelta.

   Si hay un ciclo existen lados de vuelta. 

   Tiempo de ejecucion O(|V|+|E|)
*/
public class CicloDigrafo(val g: GrafoDirigido) {
    val grafoDFS: DFS
    val ciclo: Boolean

    init {
        grafoDFS = DFS(g)
        ciclo = grafoDFS.hayLadosDeVuelta() 
    }

    // Retorna true si el digrafo G tiene un ciclo, false en caso contrario
    fun existeUnCiclo() : Boolean = ciclo

    /* 
        Devuelve un ciclo del grafo.
        Para esto revisamos un lado de vuelta (u, v)
        y vamos revisando los predecesores de u hasta llegar a v

        {P: El grafo tiene un ciclo}
        {Q: true}
    */
    fun cicloEncontrado() : Iterable<Int> {
        var ciclo: MutableList<Int> = mutableListOf()
        var ladoInicial: Lado = grafoDFS.ladosDeVuelta().next()
        var u: Int = ladoInicial.a
        var v: Int = ladoInicial.b
        var primerVer: Int = u

        while (u != v) {
            ciclo.add(0, u)
            u = grafoDFS.obtenerPredecesor(u)!!
        }
        ciclo.add(0, v)
        ciclo.add(0, primerVer)

        return ciclo.asIterable()
    }
} 
