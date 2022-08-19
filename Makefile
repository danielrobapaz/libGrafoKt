KC=	kotlinc
KFLAG=	-cp
GLIB=	ve/usb/libGrafo

all:	libGrafoKt.jar

libGrafoKt.jar: $(GLIB)/Color.class\
		$(GLIB)/RunTimeException.class\
		$(GLIB)/Lado.class\
		$(GLIB)/Arco.class\
		$(GLIB)/ArcoCosto.class\
		$(GLIB)/Arista.class\
		$(GLIB)/AristaCosto.class\
		$(GLIB)/Grafo.class\
		$(GLIB)/GrafoDirigido.class\
		$(GLIB)/GrafoDirigidoCosto.class\
		$(GLIB)/GrafoNoDirigido.class\
		$(GLIB)/GrafoNoDirigidoCosto.class\
		$(GLIB)/BFS.class\
		$(GLIB)/DFS.class\
		$(GLIB)/CicloDigrafo.class\
		$(GLIB)/OrdenamientoTopologico.class\
		$(GLIB)/Utilidades.class\
		$(GLIB)/CFC.class\
		$(GLIB)/ComponentesConexasDFS.class\
		$(GLIB)/MinHeap.class\
		$(GLIB)/ColaDePrioridad.class\
		$(GLIB)/PrimAMC.class\
		$(GLIB)/CCM_BellmanFord.class\
		$(GLIB)/CCM_Dijkstra.class

	jar -cvf $@ $(GLIB)/*.class 

$(GLIB)/Color.class: $(GLIB)/Color.kt
	$(KC) $(GLIB)/Color.kt

$(GLIB)/RunTimeException.class: $(GLIB)/RunTimeException.kt
	$(KC) $(GLIB)/RunTimeException.kt

$(GLIB)/Lado.class: $(GLIB)/Lado.kt
	$(KC) $(GLIB)/Lado.kt

$(GLIB)/Arco.class: $(GLIB)/Arco.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Arco.kt

$(GLIB)/ArcoCosto.class: $(GLIB)/ArcoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ArcoCosto.kt

$(GLIB)/Arista.class: $(GLIB)/Arista.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Arista.kt

$(GLIB)/AristaCosto.class: $(GLIB)/AristaCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/AristaCosto.kt

$(GLIB)/Grafo.class: $(GLIB)/Grafo.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Grafo.kt

$(GLIB)/GrafoDirigido.class: $(GLIB)/GrafoDirigido.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoDirigido.kt

$(GLIB)/GrafoDirigidoCosto.class: $(GLIB)/GrafoDirigidoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoDirigidoCosto.kt

$(GLIB)/GrafoNoDirigido.class: $(GLIB)/GrafoNoDirigido.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoNoDirigido.kt

$(GLIB)/GrafoNoDirigidoCosto.class: $(GLIB)/GrafoNoDirigidoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoNoDirigidoCosto.kt

$(GLIB)/BFS.class: $(GLIB)/BFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/BFS.kt

$(GLIB)/DFS.class: $(GLIB)/DFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/DFS.kt

$(GLIB)/CicloDigrafo.class: $(GLIB)/CicloDigrafo.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CicloDigrafo.kt

$(GLIB)/Utilidades.class: $(GLIB)/Utilidades.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Utilidades.kt

$(GLIB)/OrdenamientoTopologico.class: $(GLIB)/OrdenamientoTopologico.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/OrdenamientoTopologico.kt

$(GLIB)/CFC.class: $(GLIB)/CFC.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CFC.kt

$(GLIB)/ComponentesConexasDFS.class: $(GLIB)/ComponentesConexasDFS.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ComponentesConexasDFS.kt

$(GLIB)/MinHeap.class: $(GLIB)/MinHeap.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/MinHeap.kt

$(GLIB)/ColaDePrioridad.class: $(GLIB)/ColaDePrioridad.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ColaDePrioridad.kt

$(GLIB)/PrimAMC.class: $(GLIB)/PrimAMC.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/PrimAMC.kt

$(GLIB)/CCM_BellmanFord.class: $(GLIB)/CCM_BellmanFord.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CCM_BellmanFord.kt

$(GLIB)/CCM_Dijkstra.class: $(GLIB)/CCM_Dijkstra.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CCM_Dijkstra.kt
	
clean:
	rm -rf libGrafoKt.jar META-INF $(GLIB)/*.class
