/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import modelo.balota;
import modelo.capturaVideo;
import vista.Principal;
import vista.controlJuego;

/**
 *
 * @author zirex
 */
public class controlador implements ActionListener, MouseListener{
    private controlJuego control= new controlJuego();
    private Principal p;
    private capturaVideo video;
    private balota balotas;
    private JButton [][] tablero;
    private ImageIcon sinPulsar= new ImageIcon(getClass().getResource("/Images/balota-no-seleccionada-peq.png"));
    private ImageIcon pulsado= new ImageIcon(getClass().getResource("/Images/balota-seleccionada-peq.png"));
    private int cont=0;
    private JLabel [] labels= new JLabel[5];
    
    private JLabel [][] prueba;
    
    public enum Accion{
        _VER_CONTROL_JUEGO, //Abre VISTA controlJuego
    }
    
    public controlador(JFrame p){
        this.p= (Principal) p;
    }
    
    public void iniciarVista(){
        //centrar Ventana
        this.p.setLocationRelativeTo(null);
        
        //boton Principal
        this.p.btnJugar.setActionCommand("_VER_CONTROL_JUEGO");
        this.p.btnJugar.addActionListener(this);
        
        this.LlenarCarton();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        this.control.dispose();
        this.video.cleanupCapture();
        this.p.txtPremio.setText(null);
        this.LlenarCarton();
        this.p.setVisible(true);

    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch(Accion.valueOf(ae.getActionCommand())){
            case _VER_CONTROL_JUEGO:
                try{
                    int premio= Integer.parseInt(this.p.txtPremio.getText().trim());
                    if(this.cont<4){
                    JOptionPane.showMessageDialog(null, "Por favor verifique que se haya seleccionado minimo cuatro rectangulos para jugar", "Advertencia",
                                                 JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        this.p.setVisible(false);                    
                        this.control.setLocationRelativeTo(null);
                        //boton controlJuego
                        this.control.btnReiniciar.addMouseListener(this);
                        this.video= new capturaVideo(this.control.entradaVideo);
                        this.tablero = new JButton [5][15];
                        this.balotas= new balota();
                        this.ArreglarControles();
                        this.llenarTablero();
                        this.copiaCarton();
                        this.control.lbValorPremio.setText("Premio por: "+premio+" Bs.");
                        this.control.setVisible(true);
                    }
                }
                catch(NumberFormatException ne){
                    JOptionPane.showMessageDialog(this.control, "El premio debe ser numerico!", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                
                break;
        }
    }
    
    private void ArreglarControles(){
        this.control.contenedor.setLayout(new GridLayout(5,15));
        this.control.contenedor.removeAll();
        this.p.carton.removeAll();
        this.control.lbUltimaBalota.setText("");
        this.control.lbUltimaBalota.setHorizontalTextPosition(SwingConstants.CENTER);
        this.control.lbUltimaBalota.setVerticalTextPosition(SwingConstants.CENTER);
        
        this.control.lbPenBalota.setText("");
        this.control.lbPenBalota.setHorizontalTextPosition(SwingConstants.CENTER);
        this.control.lbPenBalota.setVerticalTextPosition(SwingConstants.CENTER);
        
        this.control.lbAntePenBal.setText("");
        this.control.lbAntePenBal.setHorizontalTextPosition(SwingConstants.CENTER);
        this.control.lbAntePenBal.setVerticalTextPosition(SwingConstants.CENTER);
        
        this.control.lbCuartaBal.setText("");
        this.control.lbCuartaBal.setHorizontalTextPosition(SwingConstants.CENTER);
        this.control.lbCuartaBal.setVerticalTextPosition(SwingConstants.CENTER);
        
        this.control.lbQuintaBal.setText("");
        this.control.lbQuintaBal.setHorizontalTextPosition(SwingConstants.CENTER);
        this.control.lbQuintaBal.setVerticalTextPosition(SwingConstants.CENTER);
        
        this.labels[0]= control.lbUltimaBalota;
        this.labels[1]= control.lbPenBalota;
        this.labels[2]= control.lbAntePenBal;
        this.labels[3]= control.lbCuartaBal;
        this.labels[4]= control.lbQuintaBal;
    }
    
    private void llenarTablero(){        
        int aux=1;
        for (int x=0; x < this.tablero.length; x++) {
            for (int y=0; y < this.tablero[x].length; y++) {
                JButton boton= new JButton(String.valueOf(aux++), sinPulsar);
                boton.setHorizontalTextPosition(SwingConstants.CENTER);
                boton.setVerticalTextPosition(SwingConstants.CENTER);
                boton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                boton.setBorderPainted(false);
                boton.setContentAreaFilled(false);
                boton.setActionCommand("pulsar");
                this.tablero[x][y]= boton;
                this.control.contenedor.add(this.tablero[x][y]);
                this.tablero[x][y].addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        JButton boton =(JButton) ae.getSource();
                        if(boton.getIcon().toString().equals(sinPulsar.toString())){
                            boton.setIcon(pulsado);
                            balotas.insertarIncio(boton.getText());
                            balotas.rotarBalota(labels);
                        }
                        else{
                            boton.setIcon(sinPulsar);
                        }
                    }
                });
            }
        }
    }
    
    private MouseListener ml= new MouseListener() {

                        @Override
                        public void mouseClicked(MouseEvent me) {
                            JLabel label= (JLabel) me.getSource();
                            if(label.getBackground().equals(Color.WHITE)){
                                label.setBackground(Color.red);
                                cont++;
                            }
                            else{
                                label.setBackground(Color.WHITE);
                                cont-=1;
                            }
                       }

                        @Override
                        public void mousePressed(MouseEvent me) {}

                        @Override
                        public void mouseReleased(MouseEvent me) {}

                        @Override
                        public void mouseEntered(MouseEvent me) {}

                        @Override
                        public void mouseExited(MouseEvent me) {}
                    };
    
    private void LlenarCarton(){
        this.p.carton.removeAll();
        prueba= new JLabel[5][5];
        for (int i = 0; i < prueba.length; i++) {
            for (int j = 0; j < prueba[i].length; j++) {
                JLabel label= new JLabel();
                label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                label.setOpaque(true);                
                prueba[i][j]=label;
                this.p.carton.add(prueba[i][j]);
                if(i==2 && j==2){
                    label.setBackground(Color.red);
                }
                else{
                    label.setBackground(Color.white);
                    prueba[i][j].addMouseListener(ml);
                }
            }
        }
    }
    
    private void copiaCarton(){
        this.control.AsiGana.removeAll();
        for (int i = 0; i < prueba.length; i++) {
            for (int j = 0; j < prueba[i].length; j++) {
                JLabel label= prueba[i][j];
                this.control.AsiGana.add(label);
                prueba[i][j].removeMouseListener(ml);
            }
        }
    }
}
