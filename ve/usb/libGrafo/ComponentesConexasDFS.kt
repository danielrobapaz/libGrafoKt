package ve.usb.libGrafo
import java.util.ArrayDeque
/*
  Determina las componentes conexas de un grafo no dirigido usando DFS. 
  La componentes conexas se determinan cuando 
  se crea un nuevo objeto de esta clase.

  Para evitar stackOverFlow se uso una version de DFS iterativo
*/
public class ComponentesConexasDFS(val g: GrafoNoDirigido) {
	var idCFC: Int
	val cfcVer: MutableList<Int>
	val color: MutableList<Color>

	init {
		idCFC = -1
		cfcVer = mutableListOf()
		color = mutableListOf()

		for (i in 0 until g.obtenerNumeroDeVertices()) {
			cfcVer.add(Int.MAX_VALUE)
		}
		
		for (i in 0 until g.obtenerNumeroDeVertices()) {
			color.add(Color.BLANCO)
		}

		val s: ArrayDeque<Int> = ArrayDeque<Int>()
		for (i in 0 until g.obtenerNumeroDeVertices()) {
			if (color[i] == Color.BLANCO) {
				s.push(i)
				idCFC++

				while (!s.isEmpty()) {
					val u: Int = s.pop()
					cfcVer[u] = idCFC
					for (lado in g.adyacentes(u)) {
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
		Determina si dos vertices estan en la misma componente conexa

		{P: v y u son vertices del grafo}
		{Q: true}

		Intput: v -> Entero que representa un vertice del grafo
				u -> Entero que representa un vertice del grafo
		Output: true -> si u y v estan en la misma cc
				false -> caso contrarior
	*/ 
    fun estanMismaComponente(v: Int, u: Int) : Boolean {
		if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
			throw RunTimeException("ComponentesConexasDFS.estanMismaComponente(): El verticec $v no pertenece al grafo")
		}

		if (u < 0 || u >= g.obtenerNumeroDeVertices()) {
			throw RunTimeException("ComponentesConexasDFS.estanMismaComponente(): El verticec $u no pertenece al grafo")
		}

		return cfcVer[v] == cfcVer[u] 
    }

    // Indica el número de componentes conexas
    fun nCC() : Int = idCFC + 1

    /*
		Devuelve el identificador de la componente conexa de un vertice

		{P: v es un vertice del grafo}
		{Q: true}

		Input: v -> Entero que representa un vertice del grafo
		Output: El identificador de cc del vertice v
	*/
    fun obtenerComponente(v: Int) : Int {
		if (v < 0 || v >= g.obtenerNumeroDeVertices()) {
			throw RunTimeException("ComponentesConexasDFS.obtenerComponente(): El vertice $v no pertenece al grafo")
		}

		return cfcVer[v]
    }

    /*
		Devuelve la cantidad de vertices de un componente conexa dada

		{P: 0 <= compID <= idCFC}
		{Q: true}

		Input: compID -> Entero que representa un identificador de 	
						una componente conexa
		Output: La cantidad de vertices que tiene la componente conexa
	*/
    fun numVerticesDeLaComponente(compID: Int) : Int {
		if (compID < 0 || compID > idCFC) {
			throw RunTimeException("ComponentesConexasDFS.numDeVerticesDeLaComponente(): La compomnente $compID no existe")
		}

		var cont: Int = 0
		for (i in cfcVer) if (i == compID) cont++
		return cont
    }
}
