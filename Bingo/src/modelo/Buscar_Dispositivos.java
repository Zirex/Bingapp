package modelo;

import javax.media.*;
import javax.media.cdm.CaptureDeviceManager;

/**
 * @web http://jc-mouse.blogspot.com/
 * @author Mouse
 */
public class Buscar_Dispositivos {    

    public Buscar_Dispositivos(){}

    public void Escaner(){
        //se recorre la cantidad de Dispositivos que encuentra disponibles
        for(int i=0; i<CaptureDeviceManager.getDeviceList().size();i++){
            //se muestra uno por uno en pantalla
           System.out.println( ( (CaptureDeviceInfo) CaptureDeviceManager.getDeviceList().get(i) ).getName() );
        }
    }
}
