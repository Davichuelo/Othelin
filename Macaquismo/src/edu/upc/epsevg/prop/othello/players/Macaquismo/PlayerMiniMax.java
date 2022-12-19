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

/**
 * Jugador Macaquismo
 * @author David
 */
public class PlayerMiniMax implements IPlayer, IAuto{
    
    private String _nombre;             //Variable global con el nombre de nuestro jugador
    private int _profundidad;           //Variable global con la profundidad del juego
    private int nodosExplorados;        //Variable global que hace de contador con los nodos que exploramos
    CellType macaquismoPlayer = CellType.EMPTY;
    CellType otroPlayer = CellType.EMPTY;
    final static int[] filas    = {0, 1, 2, 3, 4, 5, 6, 7};
    final static int[] columnas = {0, 1, 2, 3, 4, 5, 6, 7};
    private Move movimiento;            //Variable global con el mov que elegimos
    
    int[][] tableroPuntuacion = {{4, -3,  2,  2,  2,  2, -3,  4},
                                {-3, -4, -1, -1, -1, -1, -4, -3},
                                { 2, -1,  1,  0,  0,  1, -1,  2},
                                { 2, -1,  0,  1,  1,  0, -1,  2},
                                { 2, -1,  0,  1,  1,  0, -1,  2},
                                { 2, -1,  1,  0,  0,  1, -1,  2},
                                {-3, -4, -1, -1, -1, -1, -4, -3},
                                { 4, -3,  2,  2,  2,  2, -3,  4} };
    
    public PlayerMiniMax(String nombre, int prof) {
        this._nombre = nombre;
        this._profundidad = prof;
    }
    
    @Override
    public Move move(GameStatus gs) {
        nodosExplorados = 0;
        
        macaquismoPlayer = gs.getCurrentPlayer();
        otroPlayer = CellType.opposite(macaquismoPlayer);
        
        movimiento = miniMax(gs, _profundidad); 
        
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
        ArrayList<Point> movPosibles = T.getMoves();
        
        for (int i = 0; i < movPosibles.size(); ++i){
            Point pos = movPosibles.get(i);
            GameStatus auxT = new GameStatus(T);
            auxT.movePiece(pos); //se supone que siempre es player1 :)
            int nouValor = Min(auxT, prof-1, alpha, beta);
            if (nouValor > valorActual){
                valorActual = nouValor;
                // si falla fuera del puntin Point mov = new Point(fila,columna);
                //Move resultado = new Move (pos, nodosExplorados, _profundidad, SearchType.MINIMAX);
                aux = new Point(pos);
            }
        }
        Move resultado = new Move (aux, nodosExplorados, _profundidad, SearchType.MINIMAX);
        //System.out.println("aux:" + aux);
        return resultado;
    }
    
    private int Max(GameStatus T, int _profundidad, int alpha, int beta){
        
        int valorActual = Integer.MIN_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        //mirar con el getMoves si es empty 
        
        if (_profundidad == 0 || T.getEmptyCellsCount() == 0) //arreglar que no se pueda mover a ningun lafdo
            valorActual = Heuristica(T);
  
        else{
            for (int i = 0; i < movPosibles.size(); ++i){
                Point pos = movPosibles.get(i); //xd
                GameStatus auxT = new GameStatus(T); 
                //_macaquismoColor = auxT.getCurrentPlayer();
                auxT.movePiece(pos);
                int fHMIN = Min(auxT, _profundidad-1, alpha, beta);
                valorActual = Math.max(valorActual, fHMIN); 
                alpha = Math.max(valorActual, alpha);
                if(alpha >= beta) break;

            }
        }
        
        return valorActual;
    }
    
    private int Min(GameStatus T, int _profundidad, int alpha, int beta){
        
        int valorActual = Integer.MAX_VALUE;
        ArrayList<Point> movPosibles = T.getMoves();
        
        //mirar con el getMoves si es empty 
        
        if (_profundidad == 0 || T.getEmptyCellsCount() == 0) //arreglar que no se pueda mover a ningun lafdo
            valorActual = Heuristica(T);
        
        else{
            for (int i = 0; i < movPosibles.size(); ++i){
                Point pos = movPosibles.get(i); //xd
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

/*
InfoNode{
    byte indexM
    long num1, num2
    no hacer GameStatus
}
*/
