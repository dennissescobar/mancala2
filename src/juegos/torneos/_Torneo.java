package juegos.torneos;

import juegos.base.*;

/** Clase utilitaria para implementar rápidamente torneos.
 */
public abstract class _Torneo implements Torneo {
	protected final Juego juego;
	protected final Agente[] agentes;
	protected double[][] estadisticas;
	
	public _Torneo(Juego juego, Agente... agentes) {
		this.juego = juego;
		this.agentes = agentes;
		estadisticas = new double[agentes.length][4];
	}

	@Override public Juego juego() {
		return juego;
	}
	
	@Override public Agente[] agentes() {
		return agentes;
	}

	public abstract int completar();

	@Override public double[] estadisticas(Agente agente) {
		int i = indiceAgente(agente);
		return i < 0 ? null : estadisticas[i];
	}

	@Override public Double partidasJugadas(Agente agente) {
		double[] es = estadisticas(agente);
		return es == null ? null : es[0];
	}
	
	@Override public Double partidasGanadas(Agente agente) {
		double[] es = estadisticas(agente);
		return es == null ? null : es[1];
	}

	@Override public Double partidasPerdidas(Agente agente) {
		double[] es = estadisticas(agente);
		return es == null ? null : es[2];
	}

	@Override public Double resultadoPromedio(Agente agente) {
		double[] es = estadisticas(agente);
		return es == null ? null : es[3];
	}
	
	/** Índice de un agente en el vector de agentes. 
	 */
	protected int indiceAgente(Agente agente) {
		for (int i = 0; i < agentes.length; i++) {
			if (agente.equals(agentes[i])) {
				return i;
			}
		}
		return -1;
	}
	
	/** Acumula las estadísticas de un estado final de juego. 
	 */
	protected void acumular(Estado estadoFinal, Agente... agentes) {
		for (int i = 0; i < agentes.length; i++) {
			double[] es = estadisticas(agentes[i]);
			if (es != null) {
				double resultado = estadoFinal.resultado(agentes[i].jugador());
				es[0] += 1; // partidasJugadas
				if (resultado > 0) es[1] += 1; // partidasGanadas
				if (resultado < 0) es[2] += 1; // partidasPerdidas
				es[3] = (es[3] * (es[0] - 1) + resultado) / es[0]; // resultadoPromedio   
			}
		}
	}
}
