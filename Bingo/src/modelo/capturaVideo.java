/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import javax.swing.JLabel;

/**
 *
 * @author zirex
 */
public class capturaVideo implements CaptureCallback{
    private static int width, height, std, channel;
    private static String device;
    
    private VideoDevice videoDevice;
    private FrameGrabber    frameGrabber;
    
    private JLabel label;
    
    public capturaVideo(JLabel label1){
        device = (System.getProperty("test.device") != null) ? System.getProperty("test.device") : "/dev/video0";
        width = (System.getProperty("test.width")!=null) ? Integer.parseInt(System.getProperty("test.width")) : 260;
        height = (System.getProperty("test.height")!=null) ? Integer.parseInt(System.getProperty("test.height")) : 260;
        std = (System.getProperty("test.standard")!=null) ? Integer.parseInt(System.getProperty("test.standard")) : V4L4JConstants.STANDARD_WEBCAM;
        channel = (System.getProperty("test.channel")!=null) ? Integer.parseInt(System.getProperty("test.channel")) : 0;
        label= label1;
        
        // Initialise video device and frame grabber
                try {
                        initFrameGrabber();
                } catch (V4L4JException e1) {
                        System.err.println("Error setting up capture");
                        e1.printStackTrace();
                       
                        // cleanup and exit
                        cleanupCapture();
                        return;
                }
               
                // create and initialise UI
                //  initGUI();
               
                // start capture
                try {
                        frameGrabber.startCapture();
                } catch (V4L4JException e){
                        System.err.println("Error starting the capture");
                        e.printStackTrace();
                }
    }

    @Override
    public void nextFrame(VideoFrame vf) {
        // This method is called when a new frame is ready.
                // Don't forget to recycle it when done dealing with the frame.
               
                // draw the new frame onto the JLabel
                label.getGraphics().drawImage(vf.getBufferedImage(), 0, 0, width, height, null);
               
                // recycle the frame
                vf.recycle();
    }

    @Override
    public void exceptionReceived(V4L4JException vlje) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * this method stops the capture and releases the frame grabber and video device
     */
    public void cleanupCapture() {
            try {
                    frameGrabber.stopCapture();
            } catch (StateException ex) {
                    // the frame grabber may be already stopped, so we just ignore
                    // any exception and simply continue.
            }

            // release the frame grabber and video device
            videoDevice.releaseFrameGrabber();
            videoDevice.release();
    }
    
    private void initFrameGrabber() throws V4L4JException{
            videoDevice = new VideoDevice(device);
            frameGrabber = videoDevice.getJPEGFrameGrabber(width, height, channel, std, 80);
            frameGrabber.setCaptureCallback(this);
            width = frameGrabber.getWidth();
            height = frameGrabber.getHeight();
            System.out.println("Starting capture at "+width+"x"+height);
    }
}
