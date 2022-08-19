package ve.usb.libGrafo

// Retorna el digrafo inverso de un grafo
fun digrafoInverso(g: GrafoDirigido) : GrafoDirigido {
    var gInverso: GrafoDirigido = GrafoDirigido(g.obtenerNumeroDeVertices())

    for (lado in g) {
        gInverso.agregarArco(Arco(lado.b, lado.a))
    }

    return gInverso
}
