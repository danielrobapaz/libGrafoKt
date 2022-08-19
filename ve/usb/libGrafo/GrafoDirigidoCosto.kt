package ve.usb.libGrafo
import java.io.File

/*
    Clase que implementa un Grafo dirigido costo.
    Se usa la representacion de listas de adyacencias.

    Para las listas de adyacencias se usaran MutableList<ArcoCosto>

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
public class GrafoDirigidoCosto : Grafo {
    val listasAdy: MutableList<MutableList<ArcoCosto>>
    val listaArcos: MutableList<ArcoCosto>
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

        listaArcos = mutableListOf()
    }

    // Se construye un grafo a partir de un archivo 
    constructor(nombreArchivo: String) {
        var infoArchivo: List<String> = File(nombreArchivo).readLines()

        nVertices = infoArchivo[0].toInt()
        nLados = infoArchivo[1].toInt()

        listasAdy = mutableListOf()
        for (i in 0 until nVertices) {
            listasAdy.add(mutableListOf())
        } 
        
        listaArcos = mutableListOf()

        for (i in 2 until infoArchivo.size) {
            val arcoLineaArchivo = infoArchivo[i].split(" ")
            val u: Int = arcoLineaArchivo[0].toInt()
            val v: Int = arcoLineaArchivo[1].toInt()
            val costo: Double = arcoLineaArchivo[2].toDouble()

            this.agregarArcoCosto(ArcoCosto(u, v, costo))
        }
    }

    /* 
        Se agrega un arcoCosto al digrafo

        Si el arco no se encuenrra en el grafo, se agrega y se devuelve trua
        Se devuelve false caso contrario

        {P: Los vertices del arco pertenecen al grafo}
        {Q: true si el arco no se encuentra en el grafo
            false caso contrario}

        Input: a -> ArcoCosto
        Output: true si se a;ade el arco al grafo
                false caso contrario

        Tiempo de ejecucion O(|E|)
    */
    fun agregarArcoCosto(a: ArcoCosto) : Boolean {
        if (a.fuente() < 0 || a.fuente() > nVertices) {
            throw RunTimeException("GrafoDirigidoCosto.agregarArcoCosto(): El vertice ${a.fuente()} no pertenecen al grafo")
        }

        if (a.sumidero() < 0 || a.sumidero() > nVertices) {
            throw RunTimeException("GrafoDirigidoCosto.agregarArcoCosto(): El vertice ${a.sumidero()} no pertenece al grafo")
        }

        // Revisamos si el arco esta en el grafo
        var existeArco: Boolean = false
        for (ady in listasAdy[a.fuente()]) {
            if (ady.esIgual(a)) existeArco = true
        }

        if (!existeArco) {
            listaArcos.add(a)
            listasAdy[a.fuente()].add(a)
            nLados++

            return true
        }

        return false
    }

    /* 
        Retorna el grado del vertice
        
        Si el vértice no pertenece al grafo se lanza una RunTimeException

        El grafo del vertice  

        {P: El vertice pertenece al grafo}
        {Q: true}

        Input: v -> Vertice del grafo al cual se desea calcular su grado
        Output: |g.ady[v]|

        Tiempo de ejecucion O(listaAdy[v].size)
    */
    override fun grado(v: Int) : Int {
        if (v < 0 || v > nVertices) {
            throw RunTimeException("GrafoDirigido.grado(): El vertice $v no pertenece al grafo")
        }

        return this.gradoExterior(v) + this.gradoInterior(v)
    }

    /* 
        Devuelve el grafo exterior del vertice v

        Si el vertice pertenece al grafo se devuelve RunTimeException

        Para calcular esto recorremos todas las listas de adyacencias contando la 
        cantidad de arcos tales que su sumidero sea v

        {P: v es un vertice que pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: Grado exterior del vertice v
    */
    fun gradoExterior(v: Int) : Int {
        if (v < 0 || v > nVertices) {
            throw RunTimeException("GrafoDirigido.gradoExterior(): El vertice $v no pertenece al grafo")
        }

        var grado: Int = 0

        for (listaAdyVer in listasAdy) {
            for (a in listaAdyVer) {
                if (a.sumidero() == v) grado++
            }
        }
        return grado
    }

    /*
        Devuelve el grafo interior del vertice v

        Si el vertice pertenece al grafo se devuelve RunTimeException

        Para esto contamos cuandos elementos tiene g.ady[v]

        {P: v es un vertice que pertenece al grafo}
        {Q: true}

        Input: v -> Entero que representa un vertice del grafo
        Output: Grado interior del vertice v
    */
    fun gradoInterior(v: Int) : Int {
        if (v < 0 || v > nVertices) {
            throw RunTimeException("GrafoDirigido.gradoInterior(): El vertice $v no pertenece al grafo")
        }

        return listasAdy[v].size
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int = nLados

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int = nVertices
    
    /* 
        Retorna los arcos adyacentes al vertice v.
        Esto es lo arcos que tengan fuente v 

        Si el vertice v no pertenece al grafo se lanza RunTimeException

        {P: El vertice v pertenece al grafo}
        {Q: Un iterador sobre los arcos del grafo que tengan como fuente a v}

        input: v -> Entero que representa un vertice del grafo
        Output: Los arcos que tengan como fuente a v
    */
    override fun adyacentes(v: Int) : Iterable<ArcoCosto> {
        if (v < 0 || v > nVertices) {
            throw RunTimeException("GrafoDirigido.adyacentes(): El vertice $v no pertenece al grafo")
        }

        return listasAdy[v].asIterable()
    }

    // Retorna todos los lados del digrafo con costo
    override operator fun iterator() : Iterator<ArcoCosto> = listaArcos.iterator()

    
    // String que muestra el contenido del grafo
    override fun toString() : String {
        var conjuntoV: String = "V = {"
        for (i in 0 until nVertices) {
            conjuntoV += " $i ,"
        }
        conjuntoV += "}"

        var conjuntoE: String = "E = {"
        for (a in listaArcos) {
            conjuntoE += " ${a.toString()}, "
        }
        conjuntoE += "}"

        return "$conjuntoV \nconjuntoE"
    }

}
