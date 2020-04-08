package utext;

import java.io.*;
import java.net.*;
import javax.xml.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.xml.sax.*;

class RicevitoreEventiNavigazioneGUI extends Thread{//01
    Socket socket;
    
    public RicevitoreEventiNavigazioneGUI(Socket sock){
        socket = sock;
    }
    
    private static boolean validaXML(String xml){//02
        
        try{//02.a
            Writer scrittore = new FileWriter("messaggioDiLog.xml");
            scrittore.write(xml);
            scrittore.close();
        }catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
        
        File fileSchemaXML = new File("messaggioDiLog.xsd");
        Source fileMessaggioDiLog = new StreamSource("messaggioDiLog.xml");
        
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schemaMessaggioDiLog = schemaFactory.newSchema(fileSchemaXML);
            Validator validatore = schemaMessaggioDiLog.newValidator();
            validatore.validate(fileMessaggioDiLog);
            System.out.println("Validazione riuscita: " + fileMessaggioDiLog.getSystemId());
            return true;
        } catch (SAXException | IOException e) {
            System.out.println("Validazione non possibile: " + fileMessaggioDiLog.getSystemId());
            return false;
        }    
    }
    
    @Override
    public void run(){
        DataInputStream dis = null;
        try{
           dis = new DataInputStream(socket.getInputStream());
           
        }catch(IOException ioe){
            System.err.println(ioe.getMessage());
        }
        
        while(true){//03
            try {
                String xml = dis.readUTF();
                if(xml.equals("LOGOUT")){
                    try{
                        dis.close();
                        socket.close();
                    }catch(IOException ioe){
                        System.err.println(ioe.getMessage());
                    }
                    RicevitoreRichiesteDiConnessioneAServerLog.listaConnessioni.remove(this);
                    return;
                }
                if(!validaXML(xml)) return;
                EventoNavigazioneGUI evento = new EventoNavigazioneGUI(xml);
                evento.setIndirizzoIPClient(socket.getInetAddress().toString());
                System.out.println("Ricevuto nuovo evento di log \"" + evento.getNomeEvento() + "\"da " + socket.getInetAddress().toString());
                RicevitoreRichiesteDiConnessioneAServerLog.scriviSuFileDiLog(evento.toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}

/*

NOTE:
(01) Classe che gestisce la semplice connessione tra un solo client e il server di log per la generazione di statistiche sull'
     utilizzo della GUI, secondo l'approccio server multithreaded.

(02) Metodo che valida un EventoDiNavigazioneGUI in entrata, inviato dal client gestito dall'istanza di RicevitoreEventiNavigazioneGUI:
     (02.a) Appoggio l'evento di log ricevuto su un file per poterlo caricare successivamente e procedere con la validazione.

(03) Continuamente, il ricevitore si mette in ascolto per rilevare un nuovo messaggio di log inviato dal client. Questo viene validato
     e successivamente aggiunto al file di archiviazione degli eventi di log.


*/