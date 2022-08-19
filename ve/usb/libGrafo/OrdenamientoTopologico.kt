package ve.usb.libGrafo
import java.util.ArrayDeque

/*
  Determina el orden topológico de un DAG. El ordenamiento topológico
  se determina en el constructor de la clase.  
*/
public class OrdenamientoTopologico(val g: GrafoDirigido) {
    val color: MutableList<Color>
	val ordenTopo: MutableList<Int>

    init {
        color = mutableListOf()
		ordenTopo = mutableListOf()

        // inicializamos las listas
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            color.add(Color.BLANCO)
        }
        
        val s: ArrayDeque<Int> = ArrayDeque<Int>()
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            if (color[i] == Color.BLANCO) {
                s.push(i)

                while(!s.isEmpty()) {
                    val u: Int = s.pop()
                    for (lado in g.adyacentes(u)) {
                        val v: Int = lado.b
                        if (color[v] == Color.BLANCO) {
                            color[v] = Color.GRIS
                            s.push(v)
                        }
                    }
                    ordenTopo.add(0, u)
                }
            }
        }
    }

    // Retorna true si el grafo g es un DAG, de lo contrario retorna false
    fun esDAG() : Boolean {
		return !CicloDigrafo(g).existeUnCiclo()
	}

	//Retorna el ordenamiento topologico obtenido en el grafo
    fun obtenerOrdenTopologico() : Iterable<Int> = ordenTopo.asIterable()
}
