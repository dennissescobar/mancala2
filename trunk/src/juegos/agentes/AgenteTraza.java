package juegos.agentes;

import java.util.Iterator;

import juegos.base.*;
import juegos.tests.TestJuego;
import junit.framework.Assert;

/** Agente pensado para correr casos de prueba sobre la lógica de juego, en base
 *  a trazas de jugadas correctas. La traza debe estar formada por una lista de
 *  jugadas. Cada una de éstas representada con un String, resultado de 
 *  concatenar el jugador con el movimiento, separados con tabuladores. 
 *  Por ejemplo "Blancas\te1".
 *  @see TestJuego
 */
public class AgenteTraza implements Agente {
	private Jugador jugador;
	private final Iterator<String> traza;
	private Movimiento elegido;
	
	public AgenteTraza(Iterator<String> traza) {
		this.traza = traza;
	}

	@Override public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador; 
	}

	/** La decisión se toma de la traza. Se controla que el jugador esté 
	 *  habilitado para mover, y que su movimiento sea válido.  
	 */
	@Override public Movimiento decision(Estado estado) {
		Assert.assertNotNull("El jugador no ha sido asignado.", this.jugador);
		Assert.assertTrue("La traza ha terminado prematuramente.", traza.hasNext());
		
		// Parsing de la jugada.
		String[] jugada = traza.next().split("\\t+");
		Assert.assertTrue("Formato no válido de traza.", jugada.length >= 2);
		if (!jugador.toString().equalsIgnoreCase(jugada[0])) {
			Assert.fail("Se esperaba que moviese el jugador "+ jugador +
					", pero la traza indica que debe mover "+ jugada[0] +
					" en el estado:\n"+ estado);
		}
		// Búsqueda del movimiento.
		Movimiento[] movs = estado.movimientos(jugador);
		if (movs == null || movs.length == 0) {
			Assert.fail("No hay movimientos válidos para el jugador "+ jugador + 
					" en el estado \n"+ estado);
		}
		elegido = null;
		for (int i = 0; i < movs.length; i++) {
			if (movs[i].toString().equalsIgnoreCase(jugada[1])) {
				elegido = movs[i];
				break;
			}
		}
		if (elegido == null) {
			Assert.fail("No hay un movimiento '"+ jugada[1] + 
					"' para el jugador "+ jugador +" en el estado:\n"+ estado);
		}
		return elegido;
	}

	/** Controla que no haya más movimientos en la traza. 
	 */
	@Override public void fin(Estado estado) {
		Assert.assertFalse("El juego ha terminado pero la traza no.", traza.hasNext());
	}

	@Override public Jugador jugador() {
		return jugador;
	}

	/** Si se llama luego de decision(), se controla que el movimiento realizado
	 *  sea efectivamente el elegido. 
	 */
	@Override public void movimiento(Movimiento movimiento, Estado estado) {
		// Chequea que el movimiento que se realice sea el elegido en decision().
		if (elegido != null) {
			if (!elegido.equals(movimiento)) {
				Assert.fail("El movimiento que el jugado "+ jugador +" eligió es "+ elegido +
						" pero se ejecutó "+ movimiento +". Estado:\n"+ estado);
			}
			elegido = null;
		}
	}
}
