/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eieruhr.led;


import eieruhr.EieruhrListener;
import eieruhr.EieruhrcountEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;

/**
 *
 * @author schel
 */
public class Led extends JComponent implements EieruhrListener
{

    private Ellipse2D.Float kreisObj;
    private BasicStroke pinsel;
    private volatile float radius;
    private static final float DICKE = 20f;
    private boolean eingeschaltet;
    private Color farbe;
    // Events sind asynchron deshalb muss etwas threadsicheres gewählt werden
    // Threadsichere liste
    private CopyOnWriteArrayList<LedListener> listenerListe;
    private EieruhrListener listener;
    public Led()    
    {
        kreisObj = new Ellipse2D.Float();
        pinsel = new BasicStroke(DICKE);
        radius =100;// 1/2 * min(wWidth,wHeight)
        farbe = Color.RED;
        eingeschaltet = true;
        this.listenerListe = new CopyOnWriteArrayList<>();
        listener = this;
    }
        

    public void addLedListener(LedListener listener)
    {
        this.listenerListe.add(listener);
    }
    public void removeLedListener(LedListener listener)
    {
        this.listenerListe.remove(listener);
    }
    public void fireLedEvent(LedEvent evt)
    {
        this.listenerListe.forEach(listener-> listener.ledStateChanged(evt));
    }
    public void hinUndHerschalten()
    {
        this.setEingeschaltet(!eingeschaltet);
    }
    public boolean isEingeschaltet()
    {
        return eingeschaltet;
    }
    public void setEingeschaltet(boolean eingeschaltet)
    {
        boolean alt = this.eingeschaltet;
        boolean neu = eingeschaltet;
        this.eingeschaltet = eingeschaltet;
        // muss setEingeschaltet heißen und der Parameter muss "eingeschaltet" sein !!!
        this.firePropertyChange("eingeschaltet", alt, neu);
        this.repaint();
    }
    public Color getFarbe()
    {
        return farbe;
    }
    public void setFarbe(Color farbe)
    {
        this.farbe = farbe;
        this.repaint();
        this.fireLedEvent(new LedEvent(this));
    }
    //Wann wird gezeichnet:
    // Fenster erscheint
    // Fenstergröße ändert siche
    // Wenn der Fokus des Fensters verändert wird
    @Override
    public void paintComponent(Graphics g)
    {
        //für refresh bei älteren Betriebssys. verwenden
        super.paintComponent(g);
        //cast um neue Bibliothek zu beutzen
        Graphics2D g2 =(Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int breite = this.getWidth()-1;//-1 wegen Fehlertoleranzen
        int hoehe = this.getHeight()-1;

        radius = -DICKE/2+Math.min(breite,hoehe)/2;

        float x = breite/2f -radius;
        float y = hoehe/2f -radius;
        
        kreisObj.setFrame(x,y,2*radius,2*radius);
        
        g2.setStroke(pinsel);
        
        if(eingeschaltet)
        {
            g2.setPaint(farbe);
        }
        else
        {
            g2.setPaint(this.getParent().getBackground());
        }
        
        g2.fill(kreisObj);
        
        g2.setPaint(Color.BLACK);
        g2.draw(kreisObj);
    }


    @Override
    public void eierUhrReachedZero(EieruhrcountEvent evt)
    {
        this.setFarbe(Color.GREEN);
    }

}
