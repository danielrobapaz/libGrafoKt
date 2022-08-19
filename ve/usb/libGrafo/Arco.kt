package ve.usb.libGrafo

public open class Arco(val inicio: Int, val fin: Int) : Lado(inicio, fin) {

    // Retorna el vértice inicial del arco
    fun fuente() : Int = inicio

    // Retorna el vértice final del arco
    fun sumidero() : Int = fin

    // Representación del arco
    override fun toString() : String = "<$inicio , $fin>" 

    // Returna true si dos arcos son iguales
    fun esIgual(other: Arco): Boolean = this.fuente() == other.fuente() && this.sumidero() == other.sumidero()
} 
