package ve.usb.libGrafo
import java.io.File

/* 
    Clase que implementa un Grafo no dirigido con costo
    Se usa la representacion de listas de adyacencias.

    para las listas de adyacencias se usaran MutableList<AristaCosto>


    La clase cuenta con dos constructores.
        Un constructor recibe como entrada un entero n y se crea un grafo de n vertices y ningun lado

        El otro contructor recibe como entrada un archivo que contiene la informacion del grafo.

    El formato del archivo debe ser el que sigue:
        La primera linea contiene el numero de vertices, n.
        La segunda linea contiene el numero de lados, m.
        Las siguientes m lineas contiene los lados del grafo.
            El formato de los lados son dos enteros separados por un espacio y un real 
            que representa el costo del lado
*/  
public class GrafoNoDirigidoCosto: Grafo {
    val listasAdy: MutableList<MutableList<AristaCosto>>
    val listaAristas: MutableList<AristaCosto>
    val nVertices: Int
    var nLados: Int

    // Se construye un grafo a partir del número de vértices
    constructor(numDeVertices: Int) {
        nVertices = numDeVertices
        nLados = 0

        listasAdy = mutableListOf()
        for (i in 0 until nVertices) {
            listasAdy.add(mutableListOf())
        }

        listaAristas = mutableListOf()
    }

    // Se construye un grafo a partir de un archivo 
    constructor(nombreArchivo: String) {
        var infoArchivo: List<String> = File(nombreArchivo).readLines()

        nVertices = infoArchivo[0].toInt()
        nLados = 0

        listasAdy = mutableListOf()
        for (i in 0 until nVertices) {
            listasAdy.add(mutableListOf())
        }

        listaAristas = mutableListOf()

        for (i in 2 until infoArchivo.size) {
            var aristaLineaArchivo = infoArchivo[i].split(" ")
            val u: Int = aristaLineaArchivo[0].toInt()
            val v: Int = aristaLineaArchivo[1].toInt()
            val costo: Double = aristaLineaArchivo[2].toDouble()

            this.agregarAristaCosto(AristaCosto(u, v, costo))
        }
    }

    /* 
        Se agrega una arista costo al grafo

        Si la arista no se encuentra en el grafo, se agrega y se devuelve true
        Se devuelve false caso contrario

        {P: Los vertice de la arista pertenecen al grafo}
        {Q: E = union(E, arista costo nueva)}

        Input: a -> AristaCosto
        Output: true si se a;ade la arista al grafo
                false caso contrario

        Tiempo de ejecucion O(|E|)
    */
    fun agregarAristaCosto(a: AristaCosto) : Boolean {
        if (a.x < 0 || a.x >= nVertices) {
            throw RunTimeException("GrafoNoDirigidoCosto.agregarAristaCosto(): El vertice ${a.x} no pertenece al grafo")
        }

        if (a.y < 0 || a.y >= nVertices) {
            throw RunTimeException("GrafoNoDirigidoCosto.agregarAristaCosto(): El vertice ${a.y} no pertenece al grafo")
        }

        // Revisamos si la arista esta en el grafo
        var existeArista: Boolean = false
        for (ady in listasAdy[a.x]) {
            if (ady.esIgual(a)) existeArista = true
        }

        if (!existeArista) {
            listaAristas.add(a)
            listaAristas.add(AristaCosto(a.y, a.x, a.obtenerCosto()))
            listasAdy[a.x].add(a)
            listasAdy[a.y].add(AristaCosto(a.y, a.x, a.obtenerCosto()))
            nLados++

            return true
        }

        return false
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int = nLados

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int = nVertices

    /* 
        Retorna las aristas adyacentes al vertice v
        esto es lo arcos que tengan un extremo v

        Si el vertice v no pertenece al grafo se lanza RunTimeException

        {P: El vertice v pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: Un iterador de los lados adyacentes al vertice v
    */
    override fun adyacentes(v: Int) : Iterable<AristaCosto> {
        if (v < 0 || v >= nVertices) {
            throw RunTimeException("GrafoNoDirigido.adyacentes(): El vertice $v no pertenece al grafo")
        }

        return listasAdy[v].asIterable()
    }

    // Retorna todos los lados del grafo no dirigido
    override operator fun iterator() : Iterator<AristaCosto> = listaAristas.iterator()
    
    /* 
        Se devuelve el grado del vertice v

        {P: El vertice v debe pertenecer al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: La cantidad de lados que tienen un extremo v
        
        Tiempo de ejecucion O(|E)
    */
    override fun grado(v: Int) : Int {
        if (v < 0 || v >= nVertices) {
            throw RunTimeException("GrafoNoDirigidoCosto.grado(): El vertice $v no pertenece al grafo")
        }

        return listasAdy[v].size    
    }

    // Retorna un string con una representación del grafo, en donde se nuestra todo su contenido
    override fun toString() : String {
        var conjuntoV: String = "V = {"
        for (i in 0 until nVertices) {
            conjuntoV += " $i "
        }
        conjuntoV += "}"

        var conjuntoE: String = "E = {"
        for (a in listaAristas) {
            conjuntoE += " ${a.toString()} "
        }
        conjuntoE += "}"

        return "$conjuntoV \n$conjuntoE"
    
    }
}
