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
    private int profundidad;
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

    /**
     * Decisión de movimiento que deberá realizar el agente basado en un estado.
     * Para cada posible movimiento brinda un puntaje utilizando minimax y
     * reglas heuristicas. Elige el mejor.
     */
    public Movimiento decision(Estado estadoreal) {
        Movimiento[] movs=estadoreal.movimientos(this.jugador);
        
        EstadoMancala estadom=(EstadoMancala) estadoreal;
        Movimiento mejormovimiento=null;
        System.out.println("MOVIMIENTOS: ");
        for (Movimiento movimiento : movs) {
        	System.out.println(((MovimientoMancala)movimiento).getMov());
        }
        
		
		Movimiento puntoalazar= movs[random.nextInt(movs.length)];
        System.out.println("MOV AL AZAR: "+((MovimientoMancala)puntoalazar).getMov());
        System.out.println("VECINOS: ");
        List<Integer> vecinos=vecinos(puntoalazar,movs);
        for (Integer vecino:vecinos){
        	System.out.print("VECI: "+vecino);
        }
        
        System.out.println("PUNTAJE MOVAZAR: "+heuristicaCantidadSemillas((EstadoMancala)(puntoalazar.estado())));
	
        /*
        Double puntaje=Double.MIN_VALUE;
        for (Movimiento movimiento : movs) {
            Estado estadohijo=estadom.clone().siguiente(movimiento);
            Double puntajeminimax=minimaxAB(oponente(this.jugador, estadohijo.jugadores()),estadohijo,Double.MIN_VALUE,Double.MAX_VALUE, 0);
            if (puntajeminimax>=puntaje){
              mejormovimiento=movimiento;
              puntaje=puntajeminimax;

            }
        }*/
        return puntoalazar;
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


    /** Algoritmo de MiniMax con poda alfa beta y profundidad máxima.
     */
    private Double minimaxAB(Jugador jugador, Estado estado, Double alfa, Double beta, int profundidad){
        if (!(estado.resultado(jugador)==null)) {
            return estado.resultado(jugador);
        }
        if(profundidad>this.profundidad){
            Double valor=heuristicaCantidadSemillas((EstadoMancala)estado);
            return valor;
        }
        List<Estado> children=estadosHijos(estado);
        if (soyYo(jugador)){//turno de max
            Double score;
            for (Estado estado1 : children) {
                score = minimaxAB(oponente(jugador, estado1.jugadores()), estado1,alfa,beta, profundidad+1);
                if (score>alfa){
                    alfa=score;
                }
                if (alfa>beta){
                    return alfa;
                }
            }
            return alfa;
        }else{//turno de min
            Double score;
            for (Estado estado1 : children) {
                score = minimaxAB(oponente(jugador, estado1.jugadores()), estado1,alfa,beta, profundidad+1);
                if (score<beta){
                    beta=score;
                }
                if (alfa>beta){
                    return beta;
                }
            }
            return beta;
        }
    }

    /** Lista de estados hijos de un estado luego de aplicado un movimiento.
     * Necesario para realizar el MiniMax.
     */
    private List<Estado> estadosHijos(Estado estado){
        EstadoMancala estadom=(EstadoMancala)estado;
        Jugador jugadorEstado=estadom.jugadores()[estadom.jugadoractual];
        Movimiento[] movimientos=estadom.movimientos(jugadorEstado);
        List<Estado> estados=new ArrayList<Estado>();
        int cantmovs=0;
        if(movimientos!=null)
            cantmovs=movimientos.length;
        for (int i=0; i<cantmovs;i++){
            estados.add(estadom.clone().siguiente(movimientos[i]));
        }
        return estados;
    }

    /** Le permite al agente saber si es el quien juega o el oponente:
     * Una pregunta filosofica, soy yooo?? para saber quien soy realmente.
     */
    private boolean soyYo(Jugador jugador){
        return this.jugador==jugador;
    }

   /** Devuelve el oponente de un jugador dado.
    */
    private Jugador oponente(Jugador jugador,Jugador[] jugadores){
        for (Jugador jugador1 : jugadores) {
            if (jugador1!=jugador){
                return jugador1;
            }
        }
        return null;
    }
    
    
    /*
     * Retorna los vecinos de una movimiento, por ejemplo, si se quieren los vecinos
     * del movimiento A3 (2), se tiene que los vecinos son 1 y 3 (A2 y A4).
     * */
    private List<Integer> vecinos(Movimiento mov, Movimiento[] movs){
    	
    	int posicion=0;
        while (!mov.equals(movs[posicion])){
        	posicion++;
        }
        

    	List<Integer> vecinos=new ArrayList<Integer>();
    	if (posicion-1>=0){
    		Integer vecinoinf=((MovimientoMancala)movs[posicion-1]).getMov();
    		vecinos.add(vecinoinf);
    	}
    	if (posicion+1<movs.length){
    		Integer vecinosup=((MovimientoMancala)movs[posicion+1]).getMov();
    		vecinos.add(vecinosup);
    	}

    	return vecinos;
    }

}
