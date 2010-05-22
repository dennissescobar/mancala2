package juegos.torneos;

import juegos.base.*;

/** Interfaz de todos aquellos controladores de torneos. Un torneo se define 
 *  como un conjunto de partidas entre varios agentes siguiendo una determinada
 *  organización. Puede ser simplemente "todos contra todos", "eliminatoria" o
 *  esquemas más elaborados.  
 */
public interface Torneo {

	/** Juego del torneo.
	 */
	public Juego juego();
	
	/** Agentes que participan en el torneo. 
	 */
	public Agente[] agentes();
	
	/** Correr todas las partidas acumulando las estadísticas. Retorna la 
	 *  cantidad total de partidas jugadas.  
	 */
	public int completar();
	
	/** Cantidad de partidas que jugó el agente. Retorna null si el agente no
	 *  participa del torneo.
	 */
	public Double partidasJugadas(Agente agente);
	
	/** Cantidad de partidas ganadas por el agente. Retorna null si el agente no
	 *  participa del torneo.
	 */
	public Double partidasGanadas(Agente agente);
	
	/** Cantidad de partidas perdidas por el agente. Retorna null si el agente 
	 *  no participa del torneo.
	 */
	public Double partidasPerdidas(Agente agente);
	
	/** Resultado promedio del agente en todas las partidas que jugó. Retorna 
	 *  null si el agente no participa del torneo.
	 */
	public Double resultadoPromedio(Agente agente);
	
	/** Todas las estadísticas antes mencionadas en un vector. Las posiciones
	 *  serían:
	 *  [0] = partidasJugadas
	 *  [1] = partidasGanadas
	 *  [2] = partidasPerdidas
	 *  [3] = resultadoPromedio
	 *  Posiciones superiores se pueden usar como extensión. Si el agente no 
	 *  forma parte del torneo retorna null.
	 */
	public double[] estadisticas(Agente agente);
}
