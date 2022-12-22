/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.Macaquismo;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import static edu.upc.epsevg.prop.othello.players.Macaquismo.PlayerMiniMax.columnas;
import static edu.upc.epsevg.prop.othello.players.Macaquismo.PlayerMiniMax.filas;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author David Martínez y Daniel Mariño
 */
public class PlayerID implements IPlayer, IAuto {

    boolean tiempo = false;

    private int nodosExplorados;
    private String _nombre;             //Variable global con el nombre de nuestro jugador
    private int _profundidad;           //Variable global con la profundidad del juego
    CellType macaquismoPlayer = CellType.EMPTY;
    CellType otroPlayer = CellType.EMPTY;
    final static int[] filas    = {0, 1, 2, 3, 4, 5, 6, 7};
    final static int[] columnas = {0, 1, 2, 3, 4, 5, 6, 7};
    private Move movimiento;            //Variable global con el mov que elegimos
    long[][][] tabla = null;
    HashMap<Long, Integer> guardar; // para ir guardando las heuristicas
    private int heurAhorradas = 0;
    private int contadorErrores = 0;
    
    int[][] tableroPuntuacion = {{4, -3,  2,  2,  2,  2, -3,  4},
                            {-3, -4, -1, -1, -1, -1, -4, -3},
                            { 2, -1,  1,  0,  0,  1, -1,  2},
                            { 2, -1,  0,  1,  1,  0, -1,  2},
                            { 2, -1,  0,  1,  1,  0, -1,  2},
                            { 2, -1,  1,  0,  0,  1, -1,  2},
                            {-3, -4, -1, -1, -1, -1, -4, -3},
                            { 4, -3,  2,  2,  2,  2, -3,  4} };
    
    
    //Constructor copiado de minimax
    public PlayerID(String nombre) { //solo el nombre en Game
        this._nombre = nombre;
        this._profundidad = 0;
        guardarTablero();
        guardar = new HashMap<>();
    }
    
    
    @Override
    public Move move(GameStatus gs) {
        tiempo = false;
        nodosExplorados = 0;
        
        macaquismoPlayer = gs.getCurrentPlayer();
        otroPlayer = CellType.opposite(macaquismoPlayer);
        
        _profundidad = 0;
        
        movimiento = miniMax(gs); //no se le mete la profundidad
        
        return movimiento;
    }

    @Override
    public void timeout() {
        tiempo = true;
    }

    @Override
    public String getName() {
        return "Macaquismo(" + _nombre + ")";
    }
    
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
    
    private void guardarTablero() {
        //blancos es 0 y negros es 1
        tabla = new long [2][8][8];
        //random
        Random num = new Random();
        for (int color = 0; color < 2; color++){
            for (int fila : filas){
                for (int columna : columnas){
                    tabla[color][fila][columna] = num.nextLong();
                }
            }
        }
    }
    
    private Move miniMax(GameStatus T){
        int prof = 0;
        Move resultado = null;
        Move anterior = null;
        while (!tiempo){
            _profundidad++;
            int valorActual = Integer.MIN_VALUE;
            Point aux = new Point(0, 0);
            int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
            ArrayList<Point> movPosibles = T.getMoves();
            prof++; //va sumando cada llamada un nivel
            
            for (int i = 0; i < movPosibles.size(); ++i){
                Point pos = movPosibles.get(i);
                GameStatus auxT = new GameStatus(T);
                auxT.movePiece(pos); //se supone que siempre es player1 :)
                Integer nouValor = Min(auxT, prof-1, alpha, beta);
                if (nouValor == null) return anterior; //movimiento anterior
                if (nouValor > valorActual){
                    valorActual = nouValor;
                    // si falla fuera del puntin Point mov = new Point(fila,columna);
                    //Move resultado = new Move (pos, nodosExplorados, _profundidad, SearchType.MINIMAX);
                    aux = new Point(pos);
                }
            }
            anterior = resultado;
            resultado = new Move (aux, nodosExplorados, _profundidad, SearchType.MINIMAX_IDS);
            
        }
        
        return resultado;
    }
    
    private Integer Max(GameStatus T, int _profundidad, int alpha, int beta){
        
        Integer valorActual = Integer.MIN_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        //mirar con el getMoves si es empty 
        
        if (tiempo) return null;
        
        else if (_profundidad == 0 || T.getEmptyCellsCount() == 0){ //arreglar que no se pueda mover a ningun lafdo
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
                Point pos = movPosibles.get(i); //xd
                GameStatus auxT = new GameStatus(T); 
                //_macaquismoColor = auxT.getCurrentPlayer();
                auxT.movePiece(pos);
                Integer fHMIN = Min(auxT, _profundidad-1, alpha, beta);
                if (fHMIN == null) return null;
                valorActual = Math.max(valorActual, fHMIN);
                alpha = Math.max(valorActual, alpha);
                if(alpha >= beta) break;

            }
        }
        
        return valorActual;
    }
    
    private Integer Min(GameStatus T, int _profundidad, int alpha, int beta){
        
        Integer valorActual = Integer.MAX_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        //mirar con el getMoves si es empty 
        if (tiempo) return null;
        
        else if (_profundidad == 0 || T.getEmptyCellsCount() == 0){ //arreglar que no se pueda mover a ningun lafdo
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
                Point pos = movPosibles.get(i); //xd
                GameStatus auxT = new GameStatus(T); 
                auxT.movePiece(pos);
                Integer fHMAX = Max(auxT, _profundidad-1, alpha, beta);
                if (fHMAX == null) return null;
                valorActual = Math.min(valorActual, fHMAX); 
                beta = Math.min(valorActual, beta);
                if(alpha >= beta) break;
            }
        }

        return valorActual;
    }
    
    private int Heuristica(GameStatus T){
        int h = 0;
        for(int fila : filas){
            for(int columna : columnas){
                if (T.getPos(fila, columna) == macaquismoPlayer) //si trolea player 1
                    h += tableroPuntuacion[fila][columna];
                else if (T.getPos(fila, columna) == otroPlayer) //si trolea player2
                    h -= tableroPuntuacion[fila][columna];
            }
        }  
        
        nodosExplorados = nodosExplorados +1;
        
        return h;
    }
    
}
