package ve.usb.libGrafo
import java.io.File

/*
    Clase que implementa un Grafo dirigido.
    Se usa la representacion de listas de adyacencias.

    Para las listas de adyacencias se usaran MutableList<Arista>

    La clase cuenta con dos constructores.
        Un constructor recibe como entrada un entero n y se crea un grafo de n vertices y ningun lado

        El otro contructor recibe como entrada un archivo que contiene la informacion del grafo.

    El formato del archivo debe ser el que sigue:
        La primera linea contiene el numero de vertices, n.
        La segunda linea contiene el numero de lados, m.
        Las siguientes m lineas contiene los lados del grafo.
            El formato de los lados son dos enteros separados por un espacio
*/
public class GrafoNoDirigido: Grafo {
    val listasAdy: MutableList<MutableList<Arista>>
    val listaAristas: MutableList<Arista>
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

    /*
        Se construye un grafo a partir de un archivo
    */  
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

            this.agregarArista(Arista(u, v))
        }
    }

    /* 
        Se agrega una arista al grafo

        Si la arista no se encuentra en el grafo, se agrega y se devuelve true.
        Se devuelve false caso contrario

        {P: Los vertices de la arista pertenecen al grafo}
        {Q: E = union(E, arista nueva)}

        Input: a -> Arista 
        Output: true si se a;ade la arista al grafo
                false caso contrario

        Tiempo de ejecucion O(|E|)
    */
    fun agregarArista(a: Arista) : Boolean {
        if (a.u < 0 || a.u >= nVertices) {
            throw RunTimeException("GrafoNoDirigido.agregarArista(): El vertice ${a.u} no pertenece al grafo")
        }

        if (a.v < 0 || a.v >= nVertices) {
            throw RunTimeException("GrafoNoDirigido.agregarArista(): El vertice ${a.v} no pertenece al grafo")
        }

        // Revisamos si el arco esta en el grafo
        var existeArista: Boolean = false
        for (ady in listasAdy[a.v]) {
            if (ady.esIgual(a)) existeArista = true
        }

        if (!existeArista) {
            listaAristas.add(a)
            listaAristas.add(Arista(a.u, a.v))
            listasAdy[a.v].add(a)
            listasAdy[a.u].add(Arista(a.u, a.v))
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

        Si el vertice v no pertenece al grafo se lanza RunRimeException

        {P: El vertice v pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: Un iterador de los lados adyacentes al vertice v
    */
    override fun adyacentes(v: Int) : Iterable<Arista> {
        if (v < 0 || v >= nVertices) {
            throw RunTimeException("GrafoNoDirigido.adyacentes(): El vertice $v no pertenece al grafo")
        }

        return listasAdy[v].asIterable()
    }
    
    // Retorna todos los lados del grafo no dirigido
    override operator fun iterator() : Iterator<Arista> = listaAristas.iterator()

    
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
            throw RunTimeException("GrafoNoDirigido.grado(): El vertice $v no pertenece al grafo")
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
