package juegos.mancala;

/**
 * Representa un tablero del juego Mancala.
 * Las posiciones 6 y 13 representan los almacenes.
 * Los números en cada pozo representan
 * la cantidad de semillas en dicho pozo.
 */
/** Estructura interna: [0 1 2 3 4 5 {6} 7 8 9 10 11 12 {13}]
 * Estructura externa:
 *
            B6     B5     B4     B3     B2     B1
          ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )
    { 0 }                                          { 0 }
          ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )  ( 4 )
            A1     A2     A3     A4     A5     A6
 */

public class TableroMancala implements Cloneable{
    private int[] agujeros;

    /**
    * Constructor del tablero de Mancala.
    * Inicializa el tablero con 4 semillas en cada pozo y 0 semillas en
    * los almacenes.
    */
    public TableroMancala(){
        this.agujeros=new int[14];
        for (int i=0;i<14;i++){
            if ((i==6)||(i== 13)){//almacenes
                agujeros[i]=0;
            }else{
                agujeros[i]=4;
            }
        }
    }

    /**
    * Constructor utilizado para realizar copias del tablero en
    * determinado estado.
    */
    private TableroMancala(int[] agujeros){
        int tamanoarray=agujeros.length;
        int[] copia=new int[tamanoarray];
        
        for (int i=0;i<tamanoarray;i++){
            copia[i]=agujeros[i];
        }
        this.agujeros=copia;
    }

    /**
    * Clona el tablero actual.
    */
    @Override
    public TableroMancala clone(){
        return new TableroMancala(this.agujeros);
    }


    /*
     * Devuelve las semillas de un pozo.
     */
    public int getSemillas(int agujero){
        return this.agujeros[agujero];
    }

    /*
     * Quita las semillas de un pozo.
     */
    public void vaciarAgujero(int agujero){
        agujeros[agujero]=0;
    }

    /*
     * Agrega a un determinado pozo una semilla.
     */
    public void agregarSemilla(int agujero){
        agujeros[agujero]++;
    }

    /*
     * Agrega a un determinado pozo una cantidad de semillas especificada.
     */
    public void agregarSemilla(int agujero, int cantidad)
    {
        agujeros[agujero]=agujeros[agujero]+cantidad;
    }

    /*
     * Devuelve el pozo opuesto de determinado pozo.
     * (Util para captura de piezas del pozo o casa opuesta).
     */
    public int casaOpuesta(int agujero){
        return 12-agujero;
    }

    /*
     * Indica si un tablero es final (norte o sur no tiene semillas).
     */
    public boolean esFinal(){
        boolean vacias=true;
        int i=0;
        while ((i<6)&&vacias){
            if (getSemillas(i)!=0){
                vacias=false;
            }
            i++;
        }
        if (vacias){return true;}
        vacias=true;
        i=7;
        while ((i<13)&&vacias){
            if (getSemillas(i)!=0){
                vacias=false;
            }
            i++;
        }
        if (vacias){return true;}
        return false;
    }
}
