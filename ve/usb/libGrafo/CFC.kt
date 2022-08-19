package ve.usb.libGrafo
import java.util.ArrayDeque

/*
  Obtiene las componentes fuertementes conexas de un grafo 
  La componentes fuertementes conexas se determinan cuando 
  se crea un nuevo objeto de esta clase, es decir, en el constructor.
*/
public class CFC(val g: GrafoDirigido) {
	var idCFC: Int
	val cfcVer: MutableList<Int> // contiene el id de cfc de cada vertice
	val color: MutableList<Color> = mutableListOf()
	
	init {
		val gInverso: GrafoDirigido = digrafoInverso(g)
		val ordenTopo: Iterable<Int> = OrdenamientoTopologico(g).obtenerOrdenTopologico()
		idCFC = -1
		cfcVer = mutableListOf()

		for (i in 0 until g.obtenerNumeroDeVertices()) {
			color.add(Color.BLANCO)
		}

		for (i in 0 until g.obtenerNumeroDeVertices()) {
			cfcVer.add(Int.MAX_VALUE)
		}

		val s: ArrayDeque<Int> = ArrayDeque<Int>()
		for (i in ordenTopo) {
			if (color[i] == Color.BLANCO) {
				idCFC++
				s.push(i)

				while (!s.isEmpty()) {
					val u: Int = s.pop()
					cfcVer[u] = idCFC
					for (lado in gInverso.adyacentes(u)) {
						val v: Int = lado.b 
						if (color[v] == Color.BLANCO) {
							color[v] = Color.NEGRO 
							s.push(v)
						}
					}
				}
			}
		}
	}
    
	/*
		Determina si dos vertices estan en la misma componente

		Para esto vemos si cfcVer[u] == cfcVer[v]

		{P: v y u son vertices del grafo}
		{Q: true}

		Input: v -> Entero que representa un vertice del grafo
		Output: true si ambos vertices estan en la misma cfc
	*/
    fun estanEnLaMismaCFC(v: Int, u: Int) : Boolean {
		if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
			throw RunTimeException("CFC.estanEnLaMismaCFC(): El vertice $v no pertenece al grafo")
		}

		if (u < 0 || u >= g.obtenerNumeroDeVertices()) {
			throw RunTimeException("CFC.estanEnLaMismaCFC(): El vertice $v no pertenece al grafo")
		}

		return cfcVer[u] == cfcVer[v]
    }

    // Indica el número de componentes fuertemente conexas del digrafo.
    fun numeroDeCFC() : Int = idCFC + 1

    /*
		Devuelve el identificador de la cfc del vertice v

		{P: El vertice v pertenece al grafo}
		{Q: true}

		Input: v -> Entero que representa un vertice del grafo
		Output: Un entero que representa la cfc en la que prertenece el vertice v 
	*/
    fun obtenerIdentificadorCFC(v: Int) : Int {
		if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
			throw RunTimeException("CFC.obtenerIdentificadorCFC(): El vertice $v no pertenece al grafo")
		}

		return cfcVer[v]
    }
    
    /*
		Devuelve un iterable de CFCs.
		Cada elemento del iterador es una cfc
	*/
    fun  obternerCFC() : Iterable<MutableSet<Int>> {
		var cfcs: MutableList<MutableSet<Int>> = mutableListOf()

		for (i in 0 until idCFC + 1) {
			cfcs.add(mutableSetOf())
		}

		for (i in 0 until g.obtenerNumeroDeVertices()) {
			cfcs[cfcVer[i]].add(i)
		}

		return cfcs.asIterable()
    }

    /*
		Devuelve el grafo de componentes.

		Para esto recorremos los lados grafo.
		Para cada lado (u,v), si u y v estan distintas
		cfc agregamos el lado (obtenerIdentificadorCFC(u), obtenerIdentificadosCFC(v)).
    */
    fun obtenerGrafoComponente() : GrafoDirigido {
		var grafoComponente: GrafoDirigido = GrafoDirigido(idCFC)

		for (lado in g) {
			val u: Int = lado.a 
			val v: Int = lado.b

			if (!this.estanEnLaMismaCFC(u, v)) {
				grafoComponente.agregarArco(Arco(cfcVer[u], cfcVer[v]))
			}
		}

		return grafoComponente
    }
}