package ve.usb.libGrafo

public class AristaCosto(val x: Int,
			 val y: Int,
			 val costo: Double) : Comparable<AristaCosto>, Arista(x, y) {

    // Retorna el costo del arco
    fun obtenerCosto() : Double = costo

    // Representación en string de la arista
    override fun toString() : String = "($x, $y, $costo)"

    /* 
     Se comparan dos AristaCosto comparando con respecto a su costo
    */
    override fun compareTo(other: AristaCosto): Int {
         if (this.obtenerCosto() > other.obtenerCosto()) return 1
         if (this.obtenerCosto() < other.obtenerCosto()) return -1
         return 0
    }
} 
