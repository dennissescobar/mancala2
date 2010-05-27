package juegos.agentes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;

import juegos.base.*;

/** Interfaz humana basada en la consola. Despliega el estado de juego y los 
 *  posibles movimientos por la salida estándar y espera la decisión del usuario
 *  por la entrada estándar.   
 */
public class AgenteConsola implements Agente {
	private Jugador jugador;
	private final BufferedReader input;
	private final PrintStream output;
	
	/** Interfaz de usuario de caracteres, con entrada input y salida output. 
	 */
	public AgenteConsola(Reader input, PrintStream output) {
		this.input = new BufferedReader(input);
		this.output = output;
	}
	/** Inicia al agente con la salida estándar.
	 */
	public AgenteConsola(Reader input) {
		this(new BufferedReader(input), System.out);
	}
	/** Inicia al agente con la entrada y la salida estándar.  
	 */
	public AgenteConsola() {
		this(new InputStreamReader(System.in));
	}

	/** Imprime el estado de juego en la salida estándar, luego imprime los 
	 *  posibles movimientos. El usuario debe digitar exactamente la 
	 *  representación de texto de alguno de los movimientos, o se volverán a
	 *  desplegar los mismos hasta que se ingrese uno válido. 
	 */
	@Override public Movimiento decision(Estado estado) {
		Movimiento resultado = null;
		Movimiento[] movs = estado.movimientos(jugador);
		
		while (resultado == null) {
			output.print("Movimientos posibles: ");
			output.print(Arrays.toString(movs));
			output.print("\t");
			
			String entrada;
			try {
				entrada = input.readLine().trim();
			} catch (IOException e) {
				throw new RuntimeException("Unexpected IO error.", e);
			}
			for (int i = 0; i < movs.length; i++) {
				if (entrada.equals(movs[i].toString())) {
					resultado = movs[i];
				}
			}
		}		
		return resultado;
	}

	@Override public Jugador jugador() {
		return jugador;
	}

	@Override public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;
		output.println(String.format(
			"Ha comenzado una partida de %s, jugando como %s.",
			estado.juego(), jugador)
			);
		printEstado(estado);
	}
	
	private static final String[] DESENLACES = {"derrota", "empate", "victoria"};
	
	public static String desenlace(double resultado) {
		return DESENLACES[((int)Math.signum(resultado)) + 1];
	}
	
	@Override public void fin(Estado estado) {
		double resultado = estado.resultado(jugador);
		output.println(String.format(
			"La partida de %s ha terminado con %s (puntaje %.4f).",
			estado.juego(), desenlace(resultado), resultado
			));
	}
	
	@Override public void movimiento(Movimiento movimiento, Estado estado) {
		if (!jugador.equals(movimiento.jugador())) {
			output.println(String.format("Jugador %s mueve %s.", jugador, movimiento));
		}
		printEstado(estado);
	}
	
	protected void printEstado(Estado estado) {
		output.println("\t"+ estado.toString().replace("\n", "\n\t"));
	}
	
	@Override public String toString() {
		return "AgenteConsola()";
	}
}
