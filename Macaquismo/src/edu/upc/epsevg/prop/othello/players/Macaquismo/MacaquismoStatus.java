/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.Macaquismo;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 * Clase que representa un tablero de Othello
 * @author David Martínez y Daniel Mariño
 * 
 */
public class MacaquismoStatus extends GameStatus {
    public MacaquismoStatus(GameStatus gs) {
        super(gs);
    }
    
    public int devuelveColor(int fila, int columna){ 
        if (board_color.get(fila+columna*8))
            return 1;
        else return 0;
    }
    
    public boolean devuelveOcupacion(int fila, int columna){ 
        return (board_occupied.get(fila+columna*8));            
    }
}
