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
    
    /**
     * Constructora de la clase MacaquismoStatus
     * @param gs Tablero que representa la partida de Othello
     */
    public MacaquismoStatus(GameStatus gs) {
        super(gs);
    }
    
    /**
     * Funcion que devuelve el color de una ficha colocada en el tablero de Othello
     * @param fila Fila del tablero que representa la partida de Othello
     * @param columna Columna del tablero que representa la partida de Othello
     * @return Devuelve el color de una ficha colocada en el tablero de Othello, 0 en el caso de que sea blanca y 1 en el caso en el que es negra
     */
    public int devuelveColor(int fila, int columna){ 
        if (board_color.get(fila+columna*8))
            return 1;
        else return 0;
    }
    
    /**
     * Funcion que mira si una posicion del tablero esta ocupada o esta vacía
     * @param fila Fila del tablero que representa la partida de Othello
     * @param columna Columna del tablero que representa la partida de Othello
     * @return Devuelve true si hay una ficha en la posicion que mira, false en caso contrario
     */
    public boolean devuelveOcupacion(int fila, int columna){ 
        return (board_occupied.get(fila+columna*8));            
    }
}
