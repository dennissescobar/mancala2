package juegos.base;


/** La interfaz representa un jugador como figura en el juego. No se trata del
 *  agente sino del "bando" que el agente representaría. Por ejemplo, en el 
 *  Tatetí, los jugadores serían dos: "Xs" y "Os"; y en el Ajedrez: "Blancas" y 
 *  "Negras".
 */
public interface Jugador {

	/** Juego al cual corresponde el jugador. */
	public Juego juego();
	
}
