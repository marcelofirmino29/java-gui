package br.com.firmino.gestaovenda.modelo.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class AbstractKeyListener implements KeyListener{
    
    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {}

    @Override
    public void keyReleased(KeyEvent ke) {}
    
}
