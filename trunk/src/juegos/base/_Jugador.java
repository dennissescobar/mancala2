package juegos.base;


/** Clase utilitaria que permite implementar rápidamente implementaciones de la
 *  interfaz Jugador para juegos. 
 */
public class _Jugador implements Jugador {
	protected final Juego juego;
	protected final String nombre;
	
	public _Jugador(Juego juego, String nombre) {
		this.juego = juego;
		this.nombre = nombre;
	}

	@Override public int hashCode() {
		return (31 + juego.hashCode()) * 31 + nombre.hashCode();
	}

	@Override public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		_Jugador other = (_Jugador) obj;
		return juego.equals(other.juego) && nombre.equals(other.nombre);
	}

	@Override public String toString() {
		return nombre;
	}

	@Override public Juego juego() {
		return juego;
	}
	
	/** Construye un conjunto de jugadores para un juego a partir de una lista 
	 *	de nombres. 
	 */
	public static Jugador[] jugadores(Juego juego, String... nombres) {
		Jugador[] result = new Jugador[nombres.length];
		for (int i = 0; i < nombres.length; i++) {
			result[i] = new _Jugador(juego, nombres[i]);
		}
		return result;
	}
}
