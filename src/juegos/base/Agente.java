package juegos.base;


/** Agente que juega, representando un jugador.
 */
public interface Agente {

	/** Jugador al cual el agente representa.
	 *  @see Jugador. 
	 */
	public Jugador jugador();
	
	/** Elección de uno de los posibles movimientos en el estado del jugador del
	 *  agente.
	 *  @see Movimiento
	 */
    public Movimiento decision(Estado estado);
    
    /** Le marca al agente que ha comenzado una partida. Se pasan el jugador al
     *  cual el agente personifica, y el estado inicial de juego.  
     */
    public void comienzo(Jugador jugador, Estado estado);
    
    /** Le indica al agente que se ha realizado un movimiento (propio o no) en 
     *  la partida. Se pasan por parámetros el movimiento realizado y el estado
     *  al que se llega.  
     */
    public void movimiento(Movimiento movimiento, Estado estado);
    
    /** Le marca al agente que ha terminado una partida. El estado final es el
     *  que se pasa por parámetro.
     */
    public void fin(Estado estado);
}
