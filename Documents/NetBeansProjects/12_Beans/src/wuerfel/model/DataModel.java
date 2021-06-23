/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuerfel.model;

import java.lang.System.Logger.Level;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;





/**
 *
 * @author schel
 */
public class DataModel implements Runnable // or Callable( for one value with one value as result)
{

    private int value;
    private int randomVal; // Alternativ bei einfachen Datentypen 
    
    /**
     * Zustandsvariable -> bei Nebenläufigkeit immer nötig
     * boolean für 2 Werte - bei mehreren Werten: enum
     */
    private boolean isRunning;
    private SubmissionPublisher<Integer> iPublisher;
    // private Thread thd;or
    private ExecutorService eService;

    public DataModel()
    {
        value = 10;
        isRunning = false;
        iPublisher = new SubmissionPublisher<Integer>();
        eService = Executors.newSingleThreadExecutor();
        //eService.execute(this);
    }
    public void addValueSubscription(Subscriber<Integer> subscriber)
    {
        this.iPublisher.subscribe(subscriber);
    }
    public void setStartValue(int startValue)
    {
        this.value = startValue;
    }
    public void start()//Tread A
    {
        if(isRunning == false)
        {
            synchronized(this)
            {
                isRunning = true;
                this.notify();//Thread B notifys extern threads(=excluded the synchronized method)
                eService.execute(this);
            }
        }
    }
    public void stop()//Thread A
    {
        isRunning = false;
    }
    @Override
    public void run()//Thread B - each synchronized must run its own thread
    {
        // Umschaltung auf Maschienencodeebene - zwischen Threads
        while(isRunning)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex)
            {
                System.err.println(ex);
            }

            // Subscriber benachrichtigen und Wert mitsenden (via Event)
            System.out.println(value);

            if(--value == 0)isRunning = false;
            
            iPublisher.submit(value); //Eventqueue wird abgearbeitet (Nebenläufig) => irgendwann onNext(int)

        }
        try
        {
            this.wait();//thread A waits
        } 
        catch (InterruptedException ex)
        {
            Logger.getLogger(DataModel.class.getName()).severe(ex.toString());
        }
    }
    public synchronized int getRandomVal()
    {
        return randomVal;
    }
}




///**
// *V2
// * @author schel
// */
//public class DataModel implements Runnable // or Callable( for one value with one value as result)
//{
//
//    private int value;
//    
//    /**
//     * Zustandsvariable -> bei Nebenläufigkeit immer nötig
//     * boolean für 2 Werte - bei mehreren Werten: enum
//     */
//    private boolean isRunning;
//    private SubmissionPublisher<Integer> iPublisher;
//    // private Thread thd;or
//    private ExecutorService eService;
//
//    public DataModel()
//    {
//        value = 1;
//        isRunning = true;
//        iPublisher = new SubmissionPublisher<Integer>();
//        eService = Executors.newSingleThreadExecutor();
//        
//    }
//    public void addValueSubscription(Subscriber<Integer> subscriber)
//    {
//        this.iPublisher.subscribe(subscriber);
//    }
//    public void start()
//    {
//        isRunning = true;
//        eService = Executors.newSingleThreadExecutor();
//        eService.execute(this);// or submit(this) returns Future Object
//    }
//    public void stop()
//    {
//        isRunning = false;
//        eService = null;
//    }
//    @Override
//    public void run()
//    {
//        while(isRunning)
//        {
//            try
//            {
//                Thread.sleep(300);
//            }
//            catch(InterruptedException ex)
//            {
//                System.err.println(ex);
//            }
//            value++;// = (int)(1+6*Math.random());
//            if(value == 7)value = 1;
//            // Subscriber benachrichtigen und Wert mitsenden (via Event)
//
//            iPublisher.submit(value);
//            
//            System.out.println(value);
//        }
//    }
//}


///**
// *V1
// * @author schel
// */
//public class DataModel implements Runnable // or Callable( for one value with one value as result)
//{
//
//    private int value;
//    
//    /**
//     * Zustandsvariable -> bei Nebenläufigkeit immer nötig
//     * boolean für 2 Werte - bei mehreren Werten: enum
//     */
//    private boolean isRunning;
//    private boolean isSending;
//    private SubmissionPublisher<Integer> iPublisher;
//    // private Thread thd;or
//    private ExecutorService eService;
//
//    public DataModel()
//    {
//        value = 1;
//        isRunning = true;
//        isSending = false;
//        iPublisher = new SubmissionPublisher<Integer>();
//        eService = Executors.newSingleThreadExecutor();
//        
//    }
//    public void addValueSubscription(Subscriber<Integer> subscriber)
//    {
//        this.iPublisher.subscribe(subscriber);
//    }
//    public void start()
//    {
//        isRunning = true;
//        isSending = true;
//        eService.execute(this);// or submit(this) returns Future Object
//    }
//    public void stop()
//    {
//        //isRunning = false;
//        isSending = false;
//    }
//    @Override
//    public void run()
//    {
//        while(isRunning)
//        {
//            try
//            {
//                Thread.sleep(300);
//            }
//            catch(InterruptedException ex)
//            {
//                System.err.println(ex);
//            }
//            value++;// = (int)(1+6*Math.random());
//            if(value == 7)value = 1;
//            // Subscriber benachrichtigen und Wert mitsenden (via Event)
//            if(isSending)
//            {
//                iPublisher.submit(value);
//            }
//            System.out.println(value);
//        }
//    }
//    
//}
