# PCD-Assignment3
PCD Assignment #03 - v 0.9-20230530
				
L’assignment è articolato in tre punti, relativi alla programmazione concorrente basata su scambio di messaggi - usando gli attori come paradigma di riferimento -  e alla programmazione distribuita.

1. [Actor programming] 

Sviluppare una soluzione ad attori al problema descritto nell'assignment #01 (considerando il punto 2, GUI inclusa). 

2. [Distributed Programming with Asynchronous Message Passing]

Si vuole realizzare il prototipo di un'applicazione distribuita di "Cooperative Pixel Art". L'applicazione deve permettere a più utenti di condividere, visualizzare e modificare collaborativamente una griglia che rappresenta un'immagine in pixel.   Nel repo è disponibile un esempio (parziale) di versione centralizzata, a singolo utente (package pcd.ass03.example).

In particolare:

 ogni utente può modificare l'immagine selezionando ("colorando") mediante il puntatore del mouse gli elementi della griglia (come mostrato nell'esempio fornito)
 le variazioni apportate da un utente devono essere opportunamente visualizzate anche dagli altri utenti, in  modo che tutti gli utenti vedano sempre il medesimo stato della griglia, in modo consistente.   In particolare:
  se un utente visualizza la griglia allo stato s, ogni altro utente deve aver visualizzato o visualizzare lo stato s
  se ev1 e ev2 sono due eventi che concernono la griglia per cui ev1 →  ev2  per un utente ui, allora ev1 →  ev2  per ogni altro utente uj
 gli utenti devono potersi aggiungere (e uscire)  dinamicamente, contattando uno qualsiasi degli utenti che già partecipano all'applicazione, in modo peer-to-peer [*]
 ogni utente deve poter percepire dove si trova il puntatore del mouse di tutti gli altri utenti che stanno collaborando

Si richiede di sviluppare una soluzione utilizzando un approccio basato su scambio di messaggi, attori distribuiti oppure MOM. 

[*] Nel caso si adotti un approccio basato su MOM, è possibile considerare – come punto di contatto – il nodo dove c'è il broker (o uno dei nodi dove c'è il broker, nel caso si considerino MOM che supportano forme di clustering/federazione).

3. [Distributed Programming with Distributed Objects]

Si chiede di realizzare la medesima applicazione descritta al punto 2) tuttavia utilizzando un approccio basato su Distributed Object Computing, utilizzando Java RMI.

LA CONSEGNA

La consegna consiste in una cartella “Assignment-03” compressa (formato zip)  da sottoporre sul sito del corso, contenente tre sottodirectory (ex1, ex2 e ex3), una per esercizio. Ogni sottodirectory deve contenere: 
una directory src con i sorgenti del programma
una directory doc con breve relazione in PDF (report.pdf), che includa analisi del problema e  dell’architettura proposta, utilizzando i diagrammi ritenuti più efficaci allo scopo.
