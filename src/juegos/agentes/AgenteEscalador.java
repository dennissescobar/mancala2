package juegos.agentes;
import juegos.mancala.Mancala.EstadoMancala;
import juegos.mancala.Mancala.EstadoMancala.MovimientoMancala;
import juegos.mancala.TableroMancala;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import juegos.base.*;

/**
 * Agente que utiliza Minimax y 1 heuristica.
 * La heuristica contabiliza la cantidad de fichas ganadas (del lado del jugador)
 */
public class AgenteEscalador implements Agente {

    private Jugador jugador;
	private final Random random;


    /**
     * Constructor del agente, es necesaria una profundidad máxima para realizar
     * el MiniMax, es recomendable una profundidad no superior a 8 si se quieren
     * tiempos de respuesta aceptables.
     */
    public AgenteEscalador(long seed){
    	//se necesita un numero aleatorio para realizar la dispersion
		random = new Random(seed);       
    }
    
	public AgenteEscalador() {
		this((long)(Math.random() * Long.MAX_VALUE));
	}
    

    /**
     * Jugador que representa el agente.
     */
    public Jugador jugador() {
    	return jugador;
    }


    
    
    public Movimiento decision(Estado estadoreal) {
        Movimiento[] movs=estadoreal.movimientos(this.jugador);
        EstadoMancala estadom=(EstadoMancala) estadoreal;
        
		Movimiento mejormovimiento= movs[random.nextInt(movs.length)];

        
		//System.out.println("MOV AL AZAR: "+((MovimientoMancala)mejormovimiento).toString());
		//System.out.println("VECINOS: ");
        
        List<Movimiento> vecinos=vecinos(mejormovimiento,movs);

        
        Estado estadoactual=estadom.clone().siguiente(mejormovimiento);
        Double puntajeactual=heuristicaCantidadSemillas((EstadoMancala)(estadoactual));

        Double mejorpuntaje=puntajeactual;
        Double puntajevecino=0.0;
        
        Estado estadovecino;
        
        Boolean encontradomaximo=false;
        
        Double puntajeanterior=mejorpuntaje;

        while (!encontradomaximo){
        	//System.out.println("MEJOR MOV TEMP: "+mejormovimiento.toString()+" con puntaje: "+ mejorpuntaje);
        	puntajeanterior=mejorpuntaje;
            for (Movimiento vecino:vecinos){
            	
            	estadovecino=estadom.clone().siguiente(vecino);
            	puntajevecino=heuristicaCantidadSemillas((EstadoMancala)(estadovecino));
            	//System.out.println("PUNTAJE VECINO "+vecino.toString()+": "+puntajevecino);
            	
            	if (puntajevecino>mejorpuntaje){
            		
            		mejorpuntaje=puntajevecino;
            		mejormovimiento=vecino;
            	}
            }
            //System.out.println("MEJOR PUNTAJE: "+mejorpuntaje+", PUNTAJE ANT:"+puntajeanterior);
        	encontradomaximo=mejorpuntaje==puntajeanterior;
        	
        	vecinos=vecinos(mejormovimiento,movs);
        }
        
       
   		System.out.println("encontrado maximo: "+mejormovimiento.toString());

        return mejormovimiento;
    }    

    /** Indica al agente que ha comenzado una partida. Se pasan el jugador al
     *  cual el agente personifica, y el estado inicial de juego.
     */
    public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;
    }


    public void movimiento(Movimiento movimiento, Estado estado) {
        //no es necesaria implementación.
    }

    public void fin(Estado estado) {
        //no es necesaria implementación.
    }

   /** Calcula el valor heuristico de cantidad de semillas del lado
    * del jugador.
    * Cuenta también las semillas en el almacen (pero sin darle valor extra).
    */
    private Double heuristicaCantidadSemillas(EstadoMancala estado){
         TableroMancala tablero=estado.tablero;
         Double valor=0.0;
         int limiteinferior;
         int limitesuperior;
         if(this.jugador.toString().equals("As")) {
             limiteinferior=0;
             limitesuperior=6;
         }
         else {
             limiteinferior=7;
             limitesuperior=13;
         }
         for (int i=limiteinferior;i<=limitesuperior;i++){
            valor=valor+tablero.getSemillas(i);
         }
         return valor;
    }



 
    
    /*
     * Retorna los vecinos de una movimiento, por ejemplo, si se quieren los vecinos
     * del movimiento A3 (2), se tiene que los vecinos son 1 y 3 (A2 y A4).
     * */
    private List<Movimiento> vecinos(Movimiento mov, Movimiento[] movs){
    	
    	int posicion=0;
        while (!mov.equals(movs[posicion])){
        	posicion++;
        }
        

    	List<Movimiento> vecinos=new ArrayList<Movimiento>();
    	if (posicion-1>=0){
    		Movimiento vecinoinf=((MovimientoMancala)movs[posicion-1]);
    		vecinos.add(vecinoinf);
    	}
    	if (posicion+1<movs.length){
    		Movimiento vecinosup=((MovimientoMancala)movs[posicion+1]);
    		vecinos.add(vecinosup);
    	}

    	return vecinos;
    }

}
