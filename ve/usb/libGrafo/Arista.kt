package ve.usb.libGrafo

public open class Arista(val v: Int, val u: Int) : Lado(v, u) {

    // Representación en string de la arista
    override fun toString() : String = "($v, $u)"

    fun esIgual(other: Arista): Boolean {
        return (this.v == other.v && this.u == other.u) || (this.v == other.u && this.u == other.v)
    }
} 