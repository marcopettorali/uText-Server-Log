package utext;

import com.thoughtworks.xstream.*;
import java.io.*;
import java.text.*;
import java.util.*;

class Data implements Serializable{//01
    private String data;
    private String formatoData;
    
    public Data(String formatoData){
        DateFormat formatoDataAttuale = new SimpleDateFormat(formatoData);
        Date dataAttuale = new Date();
        this.data = formatoDataAttuale.format(dataAttuale);
        this.formatoData = formatoData;
    }
}

class Ora implements Serializable{//01
    private String ora;
    private String formatoOra;
    
    public Ora(String formatoOra){
        DateFormat formatoOraAttuale = new SimpleDateFormat(formatoOra);
        Date dataAttuale = new Date();
        this.ora = formatoOraAttuale.format(dataAttuale);
        this.formatoOra = formatoOra;
    }
}

public class EventoNavigazioneGUI implements Serializable{
    private String indirizzoIPClient;
    private Data data;
    private Ora ora;
    private String nomeEvento;
    
    public EventoNavigazioneGUI(String evento, String username){
        nomeEvento = evento;
        data = new Data("dd/MM/yyyy");
        ora = new Ora("HH:mm:ss");
        
    }
    
    public EventoNavigazioneGUI(String xml){
        
        XStream xs = new XStream();
        xs.useAttributeFor(Data.class, "formatoData");
        xs.useAttributeFor(Ora.class, "formatoOra");
        EventoNavigazioneGUI evento = (EventoNavigazioneGUI) xs.fromXML(xml);
        
        this.indirizzoIPClient = evento.indirizzoIPClient;
        this.data = evento.data;
        this.ora = evento.ora;
        this.nomeEvento = evento.nomeEvento;
    }
    
    public void setIndirizzoIPClient(String IPClient){
        indirizzoIPClient = IPClient;
    }
  
    public String getNomeEvento(){
        return nomeEvento;
    }
    
    @Override
    public String toString(){//02
        XStream xs = new XStream();
        xs.useAttributeFor(Data.class, "formatoData");
        xs.useAttributeFor(Ora.class, "formatoOra");
        return xs.toXML(this);
    }
  
}

/*

NOTE:
(01) Classi di appoggio per strutturare il messaggio XML in modo tale che i campi formatoOra e formatoData siano rappresentati come attributi dei rispettivi elementi XML
     <ora> e <data>. Questo perche', come su indicazione delle regole di buona progettazione XML, il campo 'formatoOra' puo' variare tra poche scelte e, solitamente, l'opzione
     di default e' quella corretta.

(02) Metodo overrided toString() in cui si evidenzia la struttura del messaggio XML, con gli attributi formatoData e formatoOra.


*/