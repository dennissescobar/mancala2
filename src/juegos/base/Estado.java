package juegos.base;


/** Un estado representa la situación del tablero, piezas y cualquier otro 
 *  elemento del juego en el momento en que uno o más jugadores deben ejecutar
 *  una de varias acciones posibles.
 */
public interface Estado {
	
	/** Juego al que corresponde el estado.
	 *  @see Juego 
	 */
	public Juego juego();
	
	/** Jugadores que están participando de la partida.
	 *  @see Jugador 
	 */
	public Jugador[] jugadores();
	
	/* La lista de posibles movimientos en el estado para el jugador. Si el
	 * jugador no está habilitado a mover, se puede retornar un vector vacío o
	 * null.
	 */
	public Movimiento[] movimientos(Jugador jugador);
	
	/** Resultado en el estado para el jugador dado. Debe retornar 0 para 
	 * 	indicar empate, un número positivo para victoria y un número negativo 
	 *  para derrota. Debe retornar null si el estado de juego no es final.
	 */
	public Double resultado(Jugador jugador);
	
	/** Obtiene el siguiente estado, resultante de ejecutar el movimiento. 
	 */
	public Estado siguiente(Movimiento movimiento);
}
