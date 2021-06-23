/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eieruhr.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author schel
 */
public class MyView extends JLabel
{
    public MyView()
    {   
        this.setLayout(new BorderLayout());
        this.setFont(new Font("Helvetica",0,120));
        this.setText("X");
        this.setSize(100,100);
        this.setBackground(Color.WHITE);
        
        this.setOpaque(true);

        this.setVisible(true);
    }
    
    
}
