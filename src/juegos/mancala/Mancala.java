package juegos.mancala;
import juegos.Partida;
import juegos.agentes.*;
import juegos.base.*;

import java.util.*;

/** Mancala o Kalah, implementado con las interfaces del framework para IA de la FIT-UCUDAL
 *  Pardo,Vidal,Aiello
 */

/**  Existen variantes del juego. Por ejemplo utilizando en fichas iniciales
 *  3 o 4 semillas en cada pozo, ésta implementación utiliza 4 semillas
 *  en cada pozo incial (sin contar los almacenes).
 */

public class Mancala extends _Juego {
	public static Juego JUEGO = new Mancala();

   /**
    * Crea el juego (Mancala) y sus respectivos jugadores (As - Sur) (Bs - Norte).
    */
	public Mancala() {
		super("Mancala", "As", "Bs");
	}

   /**
    * Crea el juego (Mancala) y sus respectivos jugadores (As - Sur) (Bs - Norte).
    */
	@Override public Estado inicio(Jugador... jugadores) {
		return new EstadoMancala(new TableroMancala());
	}


   /**
    * Coordenadas de las casillas, para usar como nombres de movimientos.
    */
	private final String[] CASILLAS = {"A1","A2","A3","A4","A5","A6","B1","B2","B3","B4","B5","B6"};

   /** Clase del estado del juego Mancala.
    */
	public class EstadoMancala implements Estado,Cloneable{
		public final TableroMancala tablero;
                public int jugadoractual;

       /** Constructor del estado, es necesario conocer el tablero.
        */
		public EstadoMancala(TableroMancala tablero) {
			this.tablero = tablero;
                        this.jugadoractual=0;
		}
       /** Permite construir un estado jugando un número de jugador dado.
        */
		public EstadoMancala(TableroMancala tablero,int jugador) {
			this.tablero = tablero;
                        this.jugadoractual=jugador;
		}

               /** Realiza la clonación de un estado.
                */
                @Override
                public EstadoMancala clone(){
                    TableroMancala tableroclon=this.tablero.clone();
                    int jugadorclon=this.jugadoractual;
                    return new EstadoMancala(tableroclon,jugadorclon);
                }

       /** Devuelve el juego (Mancala).
        */
		@Override public Juego juego() {
			return Mancala.this;
		}

       /** Devuelve un Array con los jugadores (As,Bs).
        */
		@Override public Jugador[] jugadores() {
			return jugadores;
		}


               /** Permite realizar un cambio de jugador, en el caso de ser 2 jugadores,
                * de 0 <-> 1.
                */
                private void cambiarJugador(){
                                int jugadorsiguiente=jugadoractual+1;
                                if(jugadorsiguiente==this.jugadores().length)
                                    jugadorsiguiente=0;
                                jugadoractual=jugadorsiguiente;
                }

       /** El estado siguiente de aplicar un movimiento.
        */
		@Override public Estado siguiente(Movimiento movimiento) {
                        if (movimiento==null) return this;
                        int agujero=((MovimientoMancala)movimiento).getMov();
                        int semillas=this.tablero.getSemillas(agujero);
                        this.tablero.vaciarAgujero(agujero);
                        int agujeroactual=agujero+1;
                        for (int i=semillas;i>1;i--){
                            if(agujeroactual==agujero) {agujeroactual++;}
                            if (agujeroactual>13) {
                                agujeroactual=0;
                            }
                            this.tablero.agregarSemilla(agujeroactual);
                            agujeroactual++;
                        }
                        if (agujeroactual>13) {
                                agujeroactual=0;
                        }
                        int limiteinferior;
                        int limitesuperior;
                        if(movimiento.jugador().toString().equals("As")) {
                                limiteinferior=0;
                                limitesuperior=5;
                        }
                        else{
                                limiteinferior=7;
                                limitesuperior=12;
                        }
                        if(agujeroactual>=limiteinferior && agujeroactual<=limitesuperior && this.tablero.getSemillas(agujeroactual)==0 && this.tablero.getSemillas(this.tablero.casaOpuesta(agujeroactual))!=0) {
                            //regla de captura
                            int agujeroopuesto=tablero.casaOpuesta(agujeroactual);
                            int semillascapturadas=tablero.getSemillas(agujeroopuesto);
                            tablero.agregarSemilla(limitesuperior+1, semillascapturadas+1);
                            tablero.vaciarAgujero(agujeroopuesto);
                            cambiarJugador();
                        }else
                        {
                            tablero.agregarSemilla(agujeroactual);
                            if(!((movimiento.jugador().toString().equals("As") && agujeroactual==6) || (movimiento.jugador().toString().equals("Bs") && agujeroactual==13)))
                            {
                                //si no es doble turno, cambiamos de jugador
                                cambiarJugador();
                            }
                        }
                        return this;
                }


       /** Los movimientos posibles que tiene un jugador.
        */
		@Override public Movimiento[] movimientos(Jugador jugador) {
                        List<Integer> movimientos=new ArrayList<Integer>();
                        int limiteinferior;
                        int limitesuperior;
                        int cantidadsemillas;
                        if (jugador.toString().equals("As")){
                            limiteinferior=0;
                            limitesuperior=5;
                        } else {
                            limiteinferior=7;
                            limitesuperior=12;
                        }
                        for (int i=limiteinferior;i<=limitesuperior;i++){
                            cantidadsemillas=tablero.getSemillas(i);
                            if (cantidadsemillas!=0){
                                movimientos.add(i);
                            }
                        }
                        int cantidadmovs=movimientos.size();
                        Movimiento movim[]=new MovimientoMancala[cantidadmovs];
                        for (int i=0;i<cantidadmovs;i++){
                            movim[i]=new MovimientoMancala(movimientos.get(i));
                        }
                        if(this.jugadores()[jugadoractual]==jugador)
                            return movim;
                        else
                            return null;
		}

       /** El resultado del jugador si es un tablero final (de lo contrario retorna null).
        */
		@Override public Double resultado(Jugador jugador) {
                    if (!this.tablero.esFinal()){
                        return null;
                    }
                    int puntaje0=0;
                    for (int i=0;i<7;i++){
                     puntaje0=puntaje0+tablero.getSemillas(i);
                    }
                    
                    
                    Double canta=puntaje0+0.0;
                	Double cantb=48.0-puntaje0;
                	
                    if (jugador.toString().equals("As")){
                        return canta-cantb;//para que retorne valores negativos si a pierde
                    }else{
                        return cantb-canta;//para que retorne valores negativos si b pierde
                    }
		}


       /** Representa un movimiento del Mancala.
        */
		public class MovimientoMancala implements Movimiento {
                        public final int movimiento;

           /** Constructor del movimiento.
            *  Un movimiento puede ser representado por un int,
            *  cada jugador como maximo puede tener 6 movimientos.
            *  (0..5).
            */
			public MovimientoMancala(int movimiento) {
				this.movimiento = movimiento;
			}

           /** Estado en el movimiento.
            */
			@Override public Estado estado() {
				return EstadoMancala.this;
			}

           /** Jugador que mueve.
            */
			@Override public Jugador jugador() {
                            if (((this.movimiento)>=0)&&((this.movimiento)<=5)){
                                return jugadores[0];
                            }else{
                                return jugadores[1];
                            }
			}

                       /** Retorna el valor del movimiento.
                        */
                        public int getMov(){
                            return movimiento;
                        }

			@Override public String toString() {
                            if (movimiento==0){return CASILLAS[0];}
                            if (movimiento==1){return CASILLAS[1];}
                            if (movimiento==2){return CASILLAS[2];}
                            if (movimiento==3){return CASILLAS[3];}
                            if (movimiento==4){return CASILLAS[4];}
                            if (movimiento==5){return CASILLAS[5];}
                            if (movimiento==7){return CASILLAS[6];}
                            if (movimiento==8){return CASILLAS[7];}
                            if (movimiento==9){return CASILLAS[8];}
                            if (movimiento==10){return CASILLAS[9];}
                            if (movimiento==11){return CASILLAS[10];}
                            if (movimiento==12){return CASILLAS[11];}
                            return "NAN";

			}
		}

       /** Convierte a string el movimiento mostrando el tablero.
        */
		@Override public String toString() {

                    /** Estructura interna: [0 1 2 3 4 5 {6} 7 8 9 10 11 12 {13}]
                     * Estructura externa:
                     *
                                B6     B5     B4     B3     B2     B1
                              ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )
                        { 0 }                                          { 0 }
                              ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )
                                A1     A2     A3     A4     A5     A6
                     */
			StringBuilder buffer = new StringBuilder();

                        buffer.append("\n        B6     B5     B4     B3     B2     B1\n      ( ")
                                .append(tablero.getSemillas(12)).append(" )  ( ")
                                .append(tablero.getSemillas(11)).append(" )  ( ")
                                .append(tablero.getSemillas(10)).append(" )  ( ")
                                .append(tablero.getSemillas(9)).append(" )  ( ")
                                .append(tablero.getSemillas(8)).append(" )  ( ")
                                .append(tablero.getSemillas(7)).append(" )\n{ ")
                                .append(tablero.getSemillas(13)).append(" }                                          { ")
                                .append(tablero.getSemillas(6)).append(" }\n      ( ")
                                .append(tablero.getSemillas(0)).append(" )  ( ")
                                .append(tablero.getSemillas(1)).append(" )  ( ")
                                .append(tablero.getSemillas(2)).append(" )  ( ")
                                .append(tablero.getSemillas(3)).append(" )  ( ")
                                .append(tablero.getSemillas(4)).append(" )  ( ")
                                .append(tablero.getSemillas(5)).append(" )")
                                .append("\n        A1     A2     A3     A4     A5     A6\n");
			return buffer.toString();
		}
	}


   /** Metodo principal del Juego Mancala.
    */
	public static void main(String[] args) throws Exception {
            for (int i=0;i<100;i++){
                	System.out.println(Partida.completa(Mancala.JUEGO,
				new AgenteConsola(), new AgenteAlmaceneroSmith(7)
			).toString());
            }
 	
	}
}

