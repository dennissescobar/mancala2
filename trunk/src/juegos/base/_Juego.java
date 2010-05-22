package juegos.base;


/** Clase utilitaria para implementar juegos más rápidamente.
 */
public abstract class _Juego implements Juego {
	protected final String nombre;
	protected final Jugador[] jugadores;
	
	public _Juego(String nombre, Jugador... jugadores) {
		this.nombre = nombre;
		this.jugadores = jugadores;
	}
	public _Juego(String nombre, String... jugadores) {
		this.nombre = nombre;
		this.jugadores = _Jugador.jugadores(this, jugadores);
	}	
	
	@Override public Jugador[] jugadores() {
		return jugadores;
	}
	
	public abstract Estado inicio(Jugador... jugadores);
	
	@Override public String toString() {
		return nombre;
	}
}
