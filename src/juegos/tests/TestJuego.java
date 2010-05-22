package juegos.tests;

import java.util.*;

import juegos.Partida;
import juegos.agentes.AgenteTraza;
import juegos.base.*;
import juegos.mancala.Mancala;
import junit.framework.TestCase;

/** Clase de base para testcases destinados a verificar un componente de juego.
 */
public class TestJuego extends TestCase {
	
	/** Verifica un juego con una traza dada como una lista de String. 
	 */
	public Partida testJuego(Juego juego, String... traza) {
		List<String> lista = new ArrayList<String>();
		for (int i = 0; i < traza.length; i++) {
			lista.add(traza[i]);
		}
		return testJuego(juego, lista);
	}
	
	/** Verifica un juego con un Iterator del Iterable.
	 */
	public Partida testJuego(Juego juego, Iterable<String> traza) {
		return testJuego(juego, traza.iterator());
	}
	
	/** Verifica un juego con una traza. Cada String representa una jugada, 
	 *  de la forma (jugador +"\t"+ movimiento).
	 *  @see AgenteTraza 
	 */
	public Partida testJuego(Juego juego, Iterator<String> traza) {
		// Construye los agentes de la partida.
		Jugador[] jugadores = juego.jugadores();
		Agente[] agentes = new Agente[jugadores.length];
		for (int i = 0; i < agentes.length; i++) {
			agentes[i] = new AgenteTraza(traza);
		}
		
		return Partida.completa(juego, agentes);
	}

	/** Caso de prueba de ejemplo con el Tateti y una traza de jugadas. 
	 */
	public void testTateti() {
		Partida partida = testJuego(Mancala.JUEGO, 
			"Xs\tB2", "Os\tA2", "Xs\tA1", "Os\tC3",
			"Xs\tC1", "Os\tB1", "Xs\tA3");
		double[] resultados = partida.resultados();
		assertTrue("Xs debería haber ganado.", resultados[0] > 0);
		assertTrue("Os debería haber perdido.", resultados[1] < 0);
	}
}
