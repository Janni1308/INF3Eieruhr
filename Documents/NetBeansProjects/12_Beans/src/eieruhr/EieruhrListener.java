/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eieruhr;

import java.util.EventListener;

/**
 *
 * @author schel
 */
public interface EieruhrListener extends EventListener
{
    public void eierUhrReachedZero(EieruhrcountEvent evt);
}
