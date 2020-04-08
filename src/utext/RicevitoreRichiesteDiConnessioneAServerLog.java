package utext;

import java.io.*;
import java.net.*;
import java.util.*;

public class RicevitoreRichiesteDiConnessioneAServerLog {
    
    private final static int porta = 9977;
    private static ServerSocket socketDiAscolto;
    public static List<RicevitoreEventiNavigazioneGUI> listaConnessioni;
    private static BufferedWriter scrittore;
    
    public synchronized static void scriviSuFileDiLog(String messaggio){//01
        try {
            scrittore = new BufferedWriter(new FileWriter("log.txt", true));
            scrittore.append(messaggio + "\n");
            scrittore.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Scritto " + messaggio + " su file di log");
    }
    
    public static void main(String[] args) {
        System.out.println("uText Server di log: Sto avviando l'applicazione:");
        try {
            socketDiAscolto = new ServerSocket(porta, 10);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        listaConnessioni = new ArrayList<>();
        
        System.out.println("Pronto.");
        while(true){//02
            try{
                Socket sock = socketDiAscolto.accept();
                RicevitoreEventiNavigazioneGUI connessione = new RicevitoreEventiNavigazioneGUI(sock);
                listaConnessioni.add(connessione);
                connessione.setDaemon(true);
                connessione.start();
                System.out.println("Nuova connessione accettata");
            }catch(IOException ioe){
                System.err.println(ioe.getMessage());
            }
        }
    }
    
}

/*

NOTE:
(01) Metodo synchronized per la scrittura sul file di archiviazione degli eventi di log. La mutua esclusione e' necessaria per
     evitare che due oggetti RicevitoreEventiNavigazioneGUI tentino di scrivere contemporaneamente sullo stesso file.

(02) Continuamente si cercano nuove richieste di connessione al server di log. Dopo averne accettata una, si crea l'oggetto
     RicevitoreEventiNavigazioneGUI corrispondente a quella specifica connessione TCP, avviato come daemon per evitare che la chiusura
     del server di log sia impedita da eventuali connessioni ancora attive con i client.



*/