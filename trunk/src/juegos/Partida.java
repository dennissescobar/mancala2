package juegos;

import java.util.*;
import juegos.base.*;

/** Controlador básico de una partida. 
 */
public class Partida {

	/** Agentes que participan en la partida. El orden corresponde con el orden
	 *  de los jugadores en el juego.
	 */
	public final Agente[] agentes;
	
	/** Historia de movimientos de la partida.
	 */
	public final List<Movimiento> movimientos = new ArrayList<Movimiento>();
	
	protected Estado actual;
	protected double[] resultados;
	
	/** Construye una partida comenzando por un estado de juego dado. */
	public Partida(Estado inicio, Agente... agentes) {
		actual = inicio;
		this.agentes = agentes;
		
		Jugador[] jugadores = inicio.jugadores();
		for (int i = 0; i < agentes.length; i++) {
			agentes[i].comienzo(jugadores[i], inicio);
		}
	}
	
	/** Construye una partida a partir del estado inicial del juego. */
	public Partida(Juego juego, Agente... agentes) {
		this(juego.inicio(), agentes);
	}
	
	/** Realiza una jugada. Supone que en el juego solo hay un jugador 
	 *  habilitado para mover por turno. 
	 */
	public Movimiento jugada() {
		if (resultados == null) { // La partida no se ha terminado.
			for (Agente agente : agentes) {
				Movimiento[] movs = actual.movimientos(agente.jugador());
				// Supone un solo agente habilitado por vez.
				if (movs != null && movs.length > 0) {
					Movimiento accion = agente.decision(actual);
					actual = actual.siguiente(accion);
					for (int i = 0; i < agentes.length; i++) {
						agentes[i].movimiento(accion, actual);
					}
					movimientos.add(accion);
					return accion;
				}
			}
			resultados = new double[agentes.length];
			for (int i = 0; i < resultados.length; i++) {
				resultados[i] = actual.resultado(agentes[i].jugador());
				agentes[i].fin(actual);
			}
			return null;
		} else {
			throw new RuntimeException("¡La partida ha terminado!");
		}
	}
	
	/** Retorna true si la partida ha llegado a un estado final de juego. 
	 */
	public boolean terminada() {
		return resultados != null;
	}
	
	/** Realiza jugadas hasta terminar la partida. 
	 */
	public Estado completa() {
		while (jugada() != null);
		return actual;
	}

	/** Resultados de una partida completa.
	 */
	public double[] resultados() {
		return resultados;
	}
	
	/** Retorna el estado actual de la partida.
	 */
	public Estado actual() {
		return actual;
	}
	
	/** El agente correspondiente con el jugador en la partida.
	 */
	public Agente agente(Jugador jugador) {
		for (Agente agente : agentes) {
			if (agente.jugador().equals(jugador)) {
				return agente;
			}
		}
		return null;
	}
	
	@Override public String toString() {
		StringBuilder buffer = new StringBuilder();
		Juego juego = actual.juego();
		buffer.append(";\t").append(juego).append('\n');
		for (int i = 0; i < agentes.length; i++) {
			buffer.append(";\t").append(juego.jugadores()[i])
				.append('\t').append(agentes[i]).append('\n');
		}
		int i = 1;
		for (Movimiento mov : movimientos) {
			buffer.append(i)
				.append('\t').append(mov.jugador())
				.append('\t').append(mov)
				.append('\n');
			i++;
		}
		for (i = 0; i < agentes.length; i++) {
			double r = resultados[i];
			buffer.append(r < 0.0 ? "-" : r > 0.0 ? "+" : "=")
				.append('\t').append(juego.jugadores()[i])
				.append('\t').append(String.format("%8.4f", resultados[i]))
				.append('\n');
		}
		return buffer.toString();
	}
	
////////////////////////////////////////////////////////////////////////////////

	/** Retorna una partida completa a partir del estado inicial del juego. */
	public static Partida completa(Juego juego, Agente... agentes) {
		Partida partida = new Partida(juego, agentes);
		partida.completa();
		return partida;
	}
}
