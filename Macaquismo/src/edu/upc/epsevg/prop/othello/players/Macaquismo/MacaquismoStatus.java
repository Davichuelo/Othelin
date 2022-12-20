/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.Macaquismo;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 *
 * @author David Martínez y Daniel Mariño
 * 
 */
public class MacaquismoStatus extends GameStatus {
    public MacaquismoStatus(GameStatus gs) {
        super(gs);
    }
    
    public int devuelveColor(int fila, int columna){ //blancos es 0 y negros es 1
        if (board_color.get(fila+columna*8))
            return 1;
        else return 0;
    }
    
    public boolean devuelveOcupacion(int fila, int columna){ //blancos es 0 y negros es 1
        return (board_occupied.get(fila+columna*8));            
    }
}
