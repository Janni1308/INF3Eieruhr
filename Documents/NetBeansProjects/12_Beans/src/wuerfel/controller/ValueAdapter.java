/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wuerfel.controller;

import eieruhr.view.MyView;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import wuerfel.model.DataModel;
import wuerfel.util.OhmLogger;


/**
 *
 * @author schel
 */
public class ValueAdapter implements Subscriber<Integer>
{
    //SingleTone (=exact ein Objekt):Zeile kann kopiert werden und es ist immer das gleiche Objekt
    private static java.util.logging.Logger lg = OhmLogger.getLogger();

    private SubmissionPublisher<Integer> publisher;
    private MyView view;
    private DataModel model;
    private Subscription subscription;
    
    public ValueAdapter(MyView view,DataModel model)
    {
        this.view = view;
        this.model = model;
        this.publisher = new SubmissionPublisher<>();
    }
    public void addValueSubscription(Subscriber<Integer> subscription)
    {
        publisher.subscribe(subscription);
    }
    public void doSubscribe()
    {
        model.addValueSubscription(this);
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription)
    {
        //lg.info("Subscribe");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Integer item)
    {      
        String strValue = String.valueOf(item);
        this.view.setText(strValue);
        this.publisher.submit(item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable thrwbl)
    {
        
    }

    @Override
    public void onComplete()
    {
    }
}
