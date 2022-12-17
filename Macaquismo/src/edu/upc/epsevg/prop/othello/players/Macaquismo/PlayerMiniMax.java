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

/**
 * Jugador Macaquismo
 * @author David
 */
public class PlayerMiniMax implements IPlayer, IAuto{
    
    private String _nombre;             //Variable global con el nombre de nuestro jugador
    private int _profundidad;           //Variable global con la profundidad del juego
    private int nodosExplorados;        //Variable global que hace de contador con los nodos que exploramos
    final static int[] filas    = {0, 1, 2, 3, 4, 5, 6, 7};
    final static int[] columnas = {0, 1, 2, 3, 4, 5, 6, 7};
    private Move movimiento;            //Variable global con el mov que elegimos
    
    int[][] tableroPuntuacion = {
                {10, 2, 7, 7, 7, 7, 2, 10},
                {2, -4, 1, 1, 1, 1, -4, 2},
                {7, 1, 1, 1, 1, 1, 1, 7},
                {7, 1, 1, 1, 1, 1, 1, 7},
                {7, 1, 1, 1, 1, 1, 1, 7},
                {7, 1, 1, 1, 1, 1, 1, 7},
                {2, -4, 1, 1, 1, 1, -4, 2},
                {10, 2, 7, 7, 7, 7, 2, 10}};
    
    public PlayerMiniMax(String nombre, int prof) {
        this._nombre = nombre;
        this._profundidad = prof;
    }
    
    @Override
    public Move move(GameStatus gs) {
        nodosExplorados = 0;
        
        movimiento = miniMax(gs, _profundidad); //se deberia igualar a algo pero nse a que 😃
        
        System.out.println("Nodos explorados " + nodosExplorados);
        
        return movimiento;
    }

    @Override
    public void timeout() {
        
    }

    @Override
    public String getName() {
        return "Macaquismo(" + _nombre + ")";
    }
    
    private Move miniMax(GameStatus T, int prof){
        int valorActual = Integer.MIN_VALUE;
        Point aux = new Point(0, 0);
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        
        for (int fila : filas){
            for (int columna : columnas){
                Point pos = new Point(fila,columna); //xd
                if (T.isHighlighted(pos)){ // CAN MOVE MIERDON, HIGHLIGHTED ENJOYER
                    GameStatus auxT = new GameStatus(T);
                    auxT.movePiece(pos); //se supone que siempre es player1 :)
                    int nouValor = Min(auxT, prof-1, alpha, beta);
                    if (nouValor > valorActual){
                        valorActual = nouValor;
                        // si falla fuera del puntin Point mov = new Point(fila,columna);
                        //Move resultado = new Move (pos, nodosExplorados, _profundidad, SearchType.MINIMAX);
                        aux = new Point(fila,columna);
                    }   
                }
            }
        }
        Move resultado = new Move (aux, nodosExplorados, _profundidad, SearchType.MINIMAX);
        System.out.println("aux:" + aux);
        return resultado;
    }
    
    private int Max(GameStatus T, int _profundidad, int alpha, int beta){
        
        int valorActual = Integer.MIN_VALUE;
        
        //falta if para mirar si es final de partida nse que 
        
        //if depth = 0 o no se puede mover
        
        if (_profundidad == 0 || T.getEmptyCellsCount() == 0) //arreglar que no se pueda mover a ningun lafdo
            valorActual = Heuristica(T);
        
        else{
            for (int fila : filas){
                for (int columna : columnas){
                    GameStatus auxT = new GameStatus(T); 
                    Point pos = new Point(fila,columna); //xd
                    if (auxT.isHighlighted(pos)){  // CAN MOVE MIERDON, HIGHLIGHTED ENJOYER
                        //_macaquismoColor = auxT.getCurrentPlayer();
                        auxT.movePiece(pos);
                        int fHMIN = Min(auxT, _profundidad-1, alpha, beta);
                        valorActual = Math.max(valorActual, fHMIN); 
                        alpha = Math.max(valorActual, alpha);
                        if(alpha >= beta) break;
                    }
                }
            }
        }
        
        return valorActual;
    }
    
    private int Min(GameStatus T, int _profundidad, int alpha, int beta){
        
        int valorActual = Integer.MAX_VALUE;
        
        //falta if para mirar si es final de partida nse que 
        
        //if depth = 0 o no se puede mover
        if (_profundidad == 0 || T.getEmptyCellsCount() == 0) //arreglar que no se pueda mover a ningun lafdo
            valorActual = Heuristica(T);
        
        else{
            for (int fila : filas){
                for (int columna : columnas){
                    GameStatus auxT = new GameStatus(T); 
                    Point pos = new Point(fila,columna); //xd
                    if (auxT.isHighlighted(pos)){  // CAN MOVE MIERDON, HIGHLIGHTED ENJOYER
                        //_rivalColor = auxT.getCurrentPlayer();
                        auxT.movePiece(pos);
                        int fHMAX = Max(auxT, _profundidad-1, alpha, beta);
                        valorActual = Math.min(valorActual, fHMAX); 
                        beta = Math.min(valorActual, beta);
                        if(alpha >= beta) break;
                    }
                }
            }
        }
        
        return valorActual;
    }
    
    private int Heuristica(GameStatus T){
        int h = 0;
        for(int fila : filas){
            for(int columna : columnas){
                if (T.getPos(fila, columna) == PLAYER1) //si trolea player 1
                    h += tableroPuntuacion[fila][columna];
                else if (T.getPos(fila, columna) == PLAYER2) //si trolea player2
                    h -= tableroPuntuacion[fila][columna];
            }
        }  

        nodosExplorados = nodosExplorados +1;
        return h;
    }
}

/*
InfoNode{
    byte indexM
    long num1, num2
    no hacer GameStatus
}
*/
