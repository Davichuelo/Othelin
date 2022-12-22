/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.Macaquismo;

import edu.upc.epsevg.prop.othello.CellType;
import static edu.upc.epsevg.prop.othello.CellType.PLAYER1;
import static edu.upc.epsevg.prop.othello.CellType.PLAYER2;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Clase que implementa una IA con algoritmo minimax que juega a Othello
 * @author David Martínez y Daniel Mariño
 */
public class PlayerMiniMax implements IPlayer, IAuto{ //control+shift+C comenta todo
    
    private String _nombre;             //Variable global con el nombre de nuestro jugador
    private int _profundidad;           //Variable global con la profundidad del juego
    private int nodosExplorados;        //Variable global que hace de contador con los nodos que exploramos
    CellType macaquismoPlayer = CellType.EMPTY; //CellType de nuestro jugador
    CellType otroPlayer = CellType.EMPTY;       //CellType del adversario
    final static int[] filas    = {0, 1, 2, 3, 4, 5, 6, 7}; //Array de filas para cuando sea necesario recorrerlas
    final static int[] columnas = {0, 1, 2, 3, 4, 5, 6, 7}; //Array de columnas para cuando sea necesario recorrerlas
    private Move movimiento;            //Variable global con el mov que elegimos
    long[][][] tabla = null;            //Tabla que nos servirá para crear tableros auxiliares
    HashMap<Long, Integer> guardar;     //HashMap para ir guardando las heuristicas
    private int heurAhorradas = 0;      //Hace de contador de heuristicas que no volvemos a visitar porque ya las tenemos guardadas en el HashMap
    //private int contadorErrores = 0;    //Contador que utilizamos mientras programabamos zobrisk para detectar errores
    
    //Tablero de puntuacion que usamos para nuestra heuristica
    int[][] tableroPuntuacion = {{4, -3,  2,  2,  2,  2, -3,  4},
                                {-3, -4, -1, -1, -1, -1, -4, -3},
                                { 2, -1,  1,  0,  0,  1, -1,  2},
                                { 2, -1,  0,  1,  1,  0, -1,  2},
                                { 2, -1,  0,  1,  1,  0, -1,  2},
                                { 2, -1,  1,  0,  0,  1, -1,  2},
                                {-3, -4, -1, -1, -1, -1, -4, -3},
                                { 4, -3,  2,  2,  2,  2, -3,  4} };
    
    /**
     * Constructora de la clase PlayerMiniMax
     * @param nombre Nombre que se le da a nuestro jugador
     * @param prof Profundidad máxima que utiliza el algoritmo minimax
     */
    public PlayerMiniMax(String nombre, int prof) {
        this._nombre = nombre;
        this._profundidad = prof;
        guardarTablero();
        guardar = new HashMap<>();
    }
    
    @Override
    public Move move(GameStatus gs) {
        nodosExplorados = 0;
        
        macaquismoPlayer = gs.getCurrentPlayer();
        otroPlayer = CellType.opposite(macaquismoPlayer);
        
        movimiento = miniMax(gs, _profundidad); 
        
        //System.out.println("Nodos explorados " + nodosExplorados);
        //System.out.println("Nodos ahorrados " + heurAhorradas);
        //System.out.println("Errores " + contadorErrores);
   
        return movimiento;
    }

    @Override
    public void timeout() {
        
    }

    @Override
    public String getName() {
        return "Macaquismo(" + _nombre + ")";
    }
    
    /**
     * Función que calcula el valor de Hash del tablero T
     * @param T Tablero que representa la partida de Othello
     * @return El valor de Hash del tablero T
     */
    private long valorHash(GameStatus T){
        MacaquismoStatus nuevoTablero = new MacaquismoStatus(T);
        long valHash = 0;
        for (int fila : filas){
            for (int columna : columnas){
                if (nuevoTablero.devuelveOcupacion(fila, columna)){
                    valHash = valHash ^ (tabla[nuevoTablero.devuelveColor(fila,columna)][fila][columna]);
                } 
            }
        }
        return valHash;
    }
    
    /**
     * Función que crea dos tableros, uno con todas las fichas de color negro y otro con todas las fichas de color blanco
     */
    private void guardarTablero() {
        //blancos es 0 y negros es 1
        tabla = new long [2][8][8];
        Random num = new Random();
        for (int color = 0; color < 2; color++){
            for (int fila : filas){
                for (int columna : columnas){
                    tabla[color][fila][columna] = num.nextLong();
                }
            }
        }
    }
    
    /**
     * Función que implementa el algoritmo Minimax
     * @param T Tablero que representa la partida de Othello
     * @param prof Profundidad máxima que utilizará el algoritmo
     * @return El mejor movimiento para el estado actual del tablero
     */
    private Move miniMax(GameStatus T, int prof){
        int valorActual = Integer.MIN_VALUE;
        Point aux = T.getMoves().get(0);
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        for (int i = 0; i < movPosibles.size(); ++i){
            Point pos = movPosibles.get(i);
            GameStatus auxT = new GameStatus(T);
            auxT.movePiece(pos); 
            int nouValor = Min(auxT, prof-1, alpha, beta);
            if (nouValor > valorActual){
                valorActual = nouValor;
                aux = new Point(pos);
            }
        }
        Move resultado = new Move (aux, nodosExplorados, _profundidad, SearchType.MINIMAX);
        //System.out.println("aux: " + aux);
        return resultado;
    }
    
    /**
     * Función que devuelve la heurística máxima de todos los siguientes estados posibles al actual (Tablero T)
     * @param T Tablero que representa la partida de Othello
     * @param _profundidad Profundidad de nodos que le queda por explorar al algoritmo minimax. Valor entre 0 y la profundidad escogida para la partida
     * @param alpha Parámetro alfa que se utiliza en la poda alfa-beta
     * @param beta Parámetro beta que se utiliza en la poda alfa-beta
     * @return Heurística máxima de todos los siguientes estados posibles al actual (Tablero T)
     */
    private int Max(GameStatus T, int _profundidad, int alpha, int beta){
        
        int valorActual = Integer.MIN_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        
        if (_profundidad == 0 || T.getEmptyCellsCount() == 0){ 
            long valorHash = valorHash(T);
            if (guardar.containsKey(valorHash)){
                valorActual = guardar.get(valorHash);
                //if (valorActual != Heuristica(T)) contadorErrores++;
                heurAhorradas++;
            }    
            else {
                valorActual = Heuristica(T);
                guardar.put(valorHash, valorActual); //como llave el valor de Hash
            } 
        }
  
        else{
            for (int i = 0; i < movPosibles.size(); ++i){
                Point pos = movPosibles.get(i); 
                GameStatus auxT = new GameStatus(T); 
                auxT.movePiece(pos);
                int fHMIN = Min(auxT, _profundidad-1, alpha, beta);
                valorActual = Math.max(valorActual, fHMIN); 
                alpha = Math.max(valorActual, alpha);
                if(alpha >= beta) break;
            }
        }
        
        return valorActual;
    }
    
    /**
     * Función que devuelve la heurística mínima de todos los siguientes estados posibles al actual (Tablero T)
     * @param T Tablero que representa la partida de Othello
     * @param _profundidad Profundidad de nodos que le queda por explorar al algoritmo minimax. Valor entre 0 y la profundidad escogida para la partida
     * @param alpha Parámetro alfa que se utiliza en la poda alfa-beta
     * @param beta Parámetro beta que se utiliza en la poda alfa-beta
     * @return Heurística mínima de todos los siguientes estados posibles al actual (Tablero T) 
     */
    private int Min(GameStatus T, int _profundidad, int alpha, int beta){
        
        int valorActual = Integer.MAX_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        if (_profundidad == 0 || T.getEmptyCellsCount() == 0){ 
            long valorHash = valorHash(T);
            if (guardar.containsKey(valorHash)){
                valorActual = guardar.get(valorHash);
                //if (valorActual != Heuristica(T)) contadorErrores++;
                heurAhorradas++;
            }    
            else {
                valorActual = Heuristica(T);
                guardar.put(valorHash, valorActual); 
            } 
        }
        
        else{
            for (int i = 0; i < movPosibles.size(); ++i){
                Point pos = movPosibles.get(i); 
                GameStatus auxT = new GameStatus(T); 
                auxT.movePiece(pos);
                int fHMAX = Max(auxT, _profundidad-1, alpha, beta);
                valorActual = Math.min(valorActual, fHMAX); 
                beta = Math.min(valorActual, beta);
                if(alpha >= beta) break;
            }
        }

        return valorActual;
    }
    
    /**
     * Función que devuelve el valor heurístico del tablero T
     * @param T Tablero que representa la partida de Othello
     * @return Devuelve el valor heurístico del tablero T, positivo en caso de ir ganando y negativo en caso de ir perdiendo
     */
    private int Heuristica(GameStatus T){
        int h = 0;
        for(int fila : filas){
            for(int columna : columnas){
                if (T.getPos(fila, columna) == macaquismoPlayer) 
                    h += tableroPuntuacion[fila][columna];
                else if (T.getPos(fila, columna) == otroPlayer) 
                    h -= tableroPuntuacion[fila][columna];
            }
        }  

        nodosExplorados = nodosExplorados +1;
        return h;
    }
}
