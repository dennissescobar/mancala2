package juegos.agentes;
import juegos.mancala.Mancala.EstadoMancala;

import juegos.mancala.TableroMancala;
import java.util.ArrayList;
import java.util.List;
import juegos.base.*;

/**
 * Agente que Jugará MUY MAL, capaz de perder muchas veces con AgenteAleatorio.
 * Agente que utiliza Minimax y 2 heuristicas para elegir la PEOR jugada.
 * Una heuristica contabiliza la cantidad de fichas ganadas (del lado del jugador)
 * Otra heuristica contabiliza la cantidad de fichas que pueden obtenerse en el
 * almacen "fichas seguras".
 * La segunda sirve también para otorgar puntaje extra a las fichas retenidas de
 * forma segura (en almacenes) y a capturar las fichas del contrincante para no
 * elegirlas como posibilidad.
 */
public class AgenteRover implements Agente {

    private Jugador jugador;
    private int profundidad;

    /**
     * Constructor del agente, es necesaria una profundidad máxima para realizar
     * el MiniMax, es recomendable una profundidad no superior a 8 si se quieren
     * tiempos de respuesta aceptables.
     */
    public AgenteRover(int profundidad){
        this.profundidad=profundidad;
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
        EstadoMancala estadom=(EstadoMancala)estadoreal;
        Movimiento peormovimiento=movs[0];
        Double puntaje=Double.MAX_VALUE;
        for (Movimiento movimiento : movs) {
            Estado estadohijo=estadom.clone().siguiente(movimiento);
            Double puntajeminimax=minimaxAB(oponente(this.jugador, estadohijo.jugadores()),estadohijo,Double.MIN_VALUE,Double.MAX_VALUE, 0);
            if (puntajeminimax<=puntaje){
              peormovimiento=movimiento;
              puntaje=puntajeminimax;

            }
        }
        return peormovimiento;
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

    /** Heuristica que brinda puntaje a capturar semillas, cuanto más semillas
     * se encuentren en el almacen, más puntaje se obtendrá.
     */
    private Double heuristicaSemillasEnAlmacen(EstadoMancala estado){
         TableroMancala tablero=estado.tablero;
         int almacen;
         if(this.jugador.toString().equals("As")) {
             almacen=6;
         }
         else {
             almacen=13;
         }
         return tablero.getSemillas(almacen)+0.0;
    }

    /** Algoritmo de MiniMax con poda alfa beta y profundidad máxima.
     */
    private Double minimaxAB(Jugador jugador, Estado estado, Double alfa, Double beta, int profundidad){
        if (!(estado.resultado(jugador)==null)) {
            return estado.resultado(jugador);
        }
        if(profundidad>this.profundidad){
            Double valor=heuristicaCantidadSemillas((EstadoMancala)estado)+heuristicaSemillasEnAlmacen((EstadoMancala)estado);
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

}
