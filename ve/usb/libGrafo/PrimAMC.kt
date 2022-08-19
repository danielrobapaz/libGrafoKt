package ve.usb.libGrafo

/*
    Determina el árbol mínimo cobertor de un 
    grafo no dirigido usando el algoritmo de Prim.

    El grafo de entrada debe ser conexo
*/
public class PrimAMC(val g: GrafoNoDirigidoCosto) {
    val key: MutableList<Double>
    val pred: MutableList<Int?>

    init {

        // Verificamos que sea conexo
        val gAux: GrafoNoDirigido = GrafoNoDirigido(g.obtenerNumeroDeVertices())
        for (lado in g) {
            gAux.agregarArista(Arista(lado.a, lado.b))
        }

        if (ComponentesConexasDFS(gAux).nCC() != 1) {
            throw RunTimeException("PrimAMC.init: El grafo dado no es conexo")
        }
        
        key = mutableListOf()
        pred = mutableListOf()

        for (i in 0 until g.obtenerNumeroDeVertices()) {
            key.add(Double.POSITIVE_INFINITY)
            pred.add(null)
        }

        key[0] = 0.0

        val q: ColaDePrioridad = ColaDePrioridad()
        val estanEnCola: MutableList<Boolean> = mutableListOf()
        for (i in 0 until g.obtenerNumeroDeVertices()) {
            q.add(i, key[i])
            estanEnCola.add(true) // todos los vertices estan en la cola 
        }

        while (!q.vacia()) {
            val u: Int = q.extraerMinimo()
            estanEnCola[u] = false
            for (lado in g.adyacentes(u)) {
                val v: Int = lado.b 

                if (estanEnCola[v] && lado.obtenerCosto() < key[v]) {
                    pred[v] = u
                    key[v] = lado.obtenerCosto()
                    q.decreaseKey(v, key[v])
                }
            }
        }

    }
    // Retorna un objeto iterable que contiene los lados del árbol mínimo cobertor.
    fun obtenerLados() : Iterable<Arista> {
        var lados: MutableList<Arista> = mutableListOf()

        for (i in 0 until g.obtenerNumeroDeVertices()) {
            if (pred[i] != null) {
                lados.add(Arista(pred[i]!!, i))
            }
        }

        return lados.asIterable()
    }
    
    // Retorna el costo del árbol mínimo cobertor. 
    fun obtenerCosto() : Double {
        var costo: Double = 0.0

        for (i in 0 until g.obtenerNumeroDeVertices()) {
            costo += key[i]
        }

        return costo
    }
}
