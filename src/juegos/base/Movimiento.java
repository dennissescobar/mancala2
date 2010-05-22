package juegos.base;


/** Un Movimiento representa una posible acción del jugador sobre el estado de
 *  juego. Puede ser el movimiento de una pieza, un desafio, o simplemente pasar
 *  el turno. 
 */
public interface Movimiento {

	/** Estado al cual aplica este movimiento.
	 *  @see Estado 
	 */
	public Estado estado();
	
	/** Jugador que puede realizar este movimiento.
	 *  @see Jugador 
	 */
	public Jugador jugador();
}
