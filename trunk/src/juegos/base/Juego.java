package juegos.base;


/** Interfaz de los componentes que representan juegos. Su cometido es crear 
 *  nuevas partidas, e indicar los jugadores que pueden participar en éstas. 
 */
public interface Juego {

	/* Método que se encarga de iniciar las condiciones del juego y retorna el
	 * correspondiente estado. La lista de jugadores corresponde con los 
	 * jugadores que van a participar. La implementación debe soportar un vector
	 * vacío o nulo para indicar una opción por defecto.
	 */
	public Estado inicio(Jugador... jugadores);
	
	/** Los jugadores que pueden participar en el juego. 
	 *  @see Jugador */
	public Jugador[] jugadores();
	
}
