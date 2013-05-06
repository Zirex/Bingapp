/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javax.swing.JLabel;

/**
 *
 * @author zirex
 */
public class balota {
    private String info;
    private balota sig;
    private JLabel [] balotas;
    
    public balota(){
        this.sig=null;
    }
    
    private balota(String x){
        this.info= x;
        this.sig= null;
    }
    
    public void insertarIncio(String x){
        balota n= new balota(x);
        n.sig= this.sig;
        this.sig=n;
    }
    
    public String getInfo(){
        return this.info;
    }
    
    public void rotarBalota(JLabel [] label){
        this.balotas= label;
        int con=0;
        balota aux= this.sig;
        while(aux!=null){
            JLabel b= this.balotas[con];
            b.setText(aux.info);
            aux=aux.sig;
            if(con==4){
                break;
            }
            con++;
        }
    }
}
