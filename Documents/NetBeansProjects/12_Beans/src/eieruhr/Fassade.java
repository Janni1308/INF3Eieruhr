/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eieruhr;

import eieruhr.view.MyView;
import java.awt.Font;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import javax.swing.JComponent;
import javax.swing.JPanel;
import wuerfel.controller.ValueAdapter;
import wuerfel.model.DataModel;

/**
 *
 * @author schel
 */
public class Fassade extends JComponent implements Flow.Subscriber<Integer>
{
    // fassade in Testklasse
    // view adapter
    
    private MyView view;
    private ValueAdapter adapter;
    private DataModel model;
    private int wert = 10;
    private boolean running;
    private Flow.Subscription subscription;
    
    // layoutmanager 
    // view in layoutmanger aufnehmen
    private CopyOnWriteArrayList<EieruhrListener> listenerZeroEventList;
    private CopyOnWriteArrayList<EieruhrListener> listenerCountChangedList;
    //zustand, werte
    public Fassade()
    {
        this.model = new DataModel();
        this.view = new MyView();
        this.add(view);
        //LayoutManager m = new ;
        //this.setLayout(mgr);
        this.adapter = new ValueAdapter(this.view,model);
        this.running = false;
        this.listenerZeroEventList = new CopyOnWriteArrayList();
        this.setVisible(true);
    }
    // Schnittstelle für Benutzer bekommt events - bidirektional
    // Erstellt alle Instanzen

    public void doSubscribe()
    {
        this.adapter.doSubscribe();
        this.adapter.addValueSubscription(this);    
    }
    public void setStartValue(int wert)
    {
        this.wert = wert;
        this.model.setStartValue(wert);
    }
    
    public void start()
    {
        this.running = true;
        this.model.start();
    }
    public void stop()
    {
        this.running = false;
        this.model.stop();
    }
    public void addEieruhrListener(EieruhrListener listener)
    {
        this.listenerZeroEventList.add(listener);
    }
    public void removeEieruhrListener(EieruhrListener listener)
    {
        this.listenerZeroEventList.remove(listener);
    }
    public void fireEieruhrEvent(EieruhrcountEvent evt)
    {
        this.listenerZeroEventList.forEach(x->x.eierUhrReachedZero(evt));
    }
    
    @Override
    public void onSubscribe(Flow.Subscription subscription)
    {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item)
    {
        this.setWert(item);
        
        this.repaint();
        if(item == 0)
        {
            this.fireEieruhrEvent(new EieruhrcountEvent(this));
            this.stop();
        }
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable thrwbl)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onComplete()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setWert(int wert)
    {
        int alt = this.wert;
        int neu = wert;
        this.wert = wert;
        // muss setEingeschaltet heißen und der Parameter muss "eingeschaltet" sein !!!
        this.firePropertyChange("wert", alt, neu);

    }
    public int getWert()
    {
        return this.wert;
    }
    public boolean isRunning()
    {
        return this.running;
    }
}
    

