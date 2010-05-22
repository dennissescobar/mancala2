package juegos.agentes;

import java.util.Random;

import juegos.base.*;

/** Agente que siempre mueve al azar. 
 */
public class AgenteAleatorio implements Agente {
	private Jugador jugador;
	private final long seed;
	private final Random random;
	
	/** Agente aleatorio, iniciando el generador de números pseudoaleatorios con
	 *  la semilla dada.  
	 */
	public AgenteAleatorio(long seed) {
		this.seed = seed;
		random = new Random(seed);
	}
	/** Agente aleatorio con semilla definida al azar. 
	 */
	public AgenteAleatorio() {
		this((long)(Math.random() * Long.MAX_VALUE));
	}

	@Override public Jugador jugador() {
		return jugador;
	}
	
	@Override public Movimiento decision(Estado estado) {
		Movimiento[] movs = estado.movimientos(jugador);
		return movs[random.nextInt(movs.length)];
	}
	
	@Override public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;	
	}
	
	@Override public void fin(Estado estado) {
		// No hay implementación.
	}
	
	@Override public void movimiento(Movimiento movimiento, Estado estado) {
		// No hay implementación.
	}
	
	@Override public String toString() {
		return String.format("AgenteAleatorio(seed=0x%X)", seed);
	}
}
