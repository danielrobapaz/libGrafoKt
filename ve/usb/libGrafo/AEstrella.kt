package ve.usb.libGrafo
import java.util.PriorityQueue

/*
 Implementación del algoritmo de A* para encontrar un camino
 desde un vértice fuente s fijo hasta alguno de los vértices objetivos. 
 La implementación debe usar como cola de prioridad un heap.
 
 Los parármetros de entradas son los siguientes:
 g: Digrafo con los costos en los lados. 
 s: Vértice de partida.
 objs: Conjunto de vértices objetivos. 
 hHat: función estimativa de h llamada h sombrero. Recibe como entrada un vértice v y retorna 
    el costo estimado desde ese vértice v, hasta cualquiera de los vértices 
    de la meta en objs.

 Como precondiciones se tiene que:
    * Si el grafo de entrada tiene un lado con costo negativo,  entonces se retorna una RuntimeException.
    * Si el vértice s no pertenece al grafo, entonces se retorna una RuntimeException.
    * Si alguno de los vértices en objs no pertenece al grafo, entonces se retorna una RuntimeException.
    * Al menos uno de los vértices de objs debe ser alcanzable desde s. Esta precondición no se chequea por razones de eficiencia.
*/
public class AEstrella(val g: GrafoDirigidoCosto, 
		      val s: Int,
		      val objs: Set<Int>,
		      val hHat: (Int) -> Double) {
   
   val fHat: MutableList<Double>
   val pred: MutableList<Int?>
   val verticeAlcanzado: Int

   init {
      for (lado in g) {
         if (lado.obtenerCosto() < 0) {
            throw RunTimeException("AEstrella.init(): El grafo tiene un lado de costo negativo")
         }
      }

      if (s < 0 || s >= g.obtenerNumeroDeVertices()) {
         throw RunTimeException("AEstrella.init(): El vertice $s no pertenece al grafo")
      }

      for (v in objs) {
         if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
            throw RunTimeException("AEstrella.int(): El vertice $v del conjunto de objetivo no pertenece al grafo")
         }
      }

      fHat = mutableListOf()
      pred = mutableListOf()
      val closedContains: MutableList<Boolean> = mutableListOf()
      val openQueue: PriorityQueue<Pair<Int, Double>> = PriorityQueue(compareBy{ it.second }) 
      val openQueueContains: MutableList<Boolean> = mutableListOf()
      val objsContains: MutableList<Boolean> = mutableListOf()
      for (i in 0 until g.obtenerNumeroDeVertices()) {
         fHat.add(Double.POSITIVE_INFINITY)
         pred.add(null)         
         closedContains.add(false) // closed no tiene ningun vertice
         openQueueContains.add(false) // open no tiene ningun vertice
         objsContains.add(false)
      }

      for (v in objs) objsContains[v] = true // marcamos los vertices que estan en objs

      fHat[s] = hHat(s)
      var u: Int = s

      while (!objsContains[u]) {
         closedContains[u] = true

         for (lado in g.adyacentes(u)) {
            val v: Int = lado.b
            if (!closedContains[v]) {
               val fNew: Double = fHat[u] - hHat(u) + lado.obtenerCosto() + hHat(v)

               if (!openQueueContains[v]) {
                  fHat[v] = fNew 
                  pred[v] = u
                  openQueue.add(Pair(v, fHat[v]))
                  openQueueContains[v] = true
               
               } else {
                  if (fNew < fHat[v]) {
                     openQueue.remove(Pair(v, fHat[v])) // decrease key 
                     fHat[v] = fNew
                     pred[v] = u
                     openQueue.add(Pair(v, fHat[v]))
                  }
               }
            }
         }

         u = openQueue.remove().first
         openQueueContains[u] = false
      }

      verticeAlcanzado = u 
   }

   // Retorna el vértice meta del conjunto objs que fue alcanzado por el algoritmo.
   fun objetivoAlcanzado() : Int = verticeAlcanzado

   // Retorna el costo del camino desde s hasta el vértice objetivo alcanzado. 
   fun costo() : Double = fHat[verticeAlcanzado]

   // Retorna los arcos del camino desde s hasta el vértice objetivo alcanzado. 
   fun obtenerCamino() : Iterable<ArcoCosto> { 
      var v: Int = verticeAlcanzado
      val camino: MutableList<ArcoCosto> = mutableListOf()
      while (v != s) {
         var u: Int = pred[v]!!
         camino.add(ArcoCosto(u, v, fHat[v] - fHat[u]))
         v = pred[v]!!
      }

      return camino.asIterable()
   }
}
