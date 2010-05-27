package juegos.torneos;

import java.util.*;

import juegos.Partida;
import juegos.agentes.*;
import juegos.base.Agente;
import juegos.base.Juego;
import juegos.mancala.Mancala;

/** Torneo que reproduce una eliminatoria, donde cada eliminación se decide con
 *  un número dado de partidas. El vector de agentes queda ordenado con los que 
 *  llegaron más alto, de ganadores a perdedores. 
 */
public class TorneoEliminatoria extends _Torneo {
	public final int nroPartidas;
	
	public TorneoEliminatoria(int nroPartidas, Juego juego, Agente... agentes) {
		super(juego, agentes);
		this.nroPartidas = nroPartidas;
	}

	@Override public int completar() {
		int cantidad = 0;
		for (int serie = agentes.length; serie > 1; ) {	
			serie = eliminatoria(serie, agentes);
			cantidad += serie * nroPartidas;
		}
		return cantidad;
	}
	
	/** Realiza una eliminatoria entre los agentes entre las posiciones 0 y 
	 *  hasta. Los pares se construyen entre los extremos hasta llegar a la 
	 *  mitad. Si el subvector no tiene largo par, el del medio juega con el 
	 *  anterior.
	 *  El subvector se ordena con los ganadores en las primeras posiciones. 
	 */
	public int eliminatoria(int hasta, Agente... agentes) {
		Agente[] participantes = new Agente[2];
		for (int i = 0; i < hasta / 2 + hasta % 2; i++) {
			int i1 = i;
			int i2 = hasta - 1 - i;
			if (i2 <= i1) {
				i1 = i2-1;
			}
			participantes[0] = agentes[i1];
			participantes[1] = agentes[i2];
			int ganadas1 = 0, ganadas2 = 0;
			for (int p = 0; p < nroPartidas; p++) {
				Partida partida = Partida.completa(juego, participantes);
				acumular(partida.actual(), participantes);
				if (partida.resultados()[0] > 0) {
					ganadas1++;
				} else {
					ganadas2++;
				}
			}
			if (ganadas1 < ganadas2) { // intercambian posiciones
				Agente temp = agentes[i1];
				agentes[i1] = agentes[i2];
				agentes[i2] = temp;
				double[] tempEs = estadisticas[i1]; // las estadísticas también
				estadisticas[i1] = estadisticas[i2];
				estadisticas[i2] = tempEs;
			}
		}
		return hasta / 2 + hasta % 2;
	}

////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		Agente[] agentes = _Torneo.shuffle(new Random(), 
				new AgenteSmith(3), new AgenteRover(3), new AgenteAlmaceneroSmith(3), 
				new AgenteAleatorio(), new AgenteAleatorio(), new AgenteAlmacenero(3),
				new AgenteAleatorio(), new AgenteAleatorio());
		Torneo torneo = new TorneoEliminatoria(3, Mancala.JUEGO, agentes);
		int cantidadPartidas = torneo.completar();
		System.out.println(torneo);
		System.out.println("Total de partidas = "+ cantidadPartidas);
	}

}
