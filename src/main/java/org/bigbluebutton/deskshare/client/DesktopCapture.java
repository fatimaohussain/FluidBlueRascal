/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bigbluebutton.deskshare.client;

import javax.swing.JApplet;
import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;

import java.awt.Image;
/**
 *
 * @author fatima
 */
public class DesktopCapture implements ClientListener {
	public static final String NAME = "DESKTOPCAPTURE: ";

	private static final long serialVersionUID = 1L;

	
    private Integer cWidthValue ;
    private Integer cHeightValue ;
    private Integer sWidthValue ;
    private Integer sHeightValue ;
    private Boolean qualityValue ;
    private Boolean aspectRatioValue;
    private Integer xValue;
    private Integer yValue;
    //Boolean tunnelValue = true;
    Boolean fullScreenValue;
    DeskshareClient client;
    Image icon;

    public boolean isSharing;

    public DesktopCapture(Image i){

       cWidthValue = new Integer(800);
        cHeightValue = new Integer(600);
        sWidthValue = new Integer(800);
        sHeightValue = new Integer(600);
        qualityValue = false;
        aspectRatioValue = false;

        isSharing = false;
         xValue = new Integer(0);
        yValue = new Integer(0);

        fullScreenValue = true;
        icon = i;
/*
        //Image image = null;
        try {
        // Read from a file
        File file = new File("bbb.gif");
        icon = ImageIO.read(file);

        } catch (Exception e) {
         System.err.println("ImageFileException: "
                        + e.getMessage());

        }*/



        
    }

	
	public void start() {
		System.out.println("Desktop Capture Starting");
		//super.start();
		/*client = new DeskshareClient.NewBuilder().host(hostValue).port(portValue)
					.room(roomValue).captureWidth(cWidthValue)
					.captureHeight(cHeightValue).scaleWidth(sWidthValue).scaleHeight(sHeightValue)
					.quality(qualityValue).aspectRatio(aspectRatioValue)
					.x(xValue).y(yValue).fullScreen(fullScreenValue)
					.httpTunnel(tunnelValue).trayIcon(icon).enableTrayIconActions(false).build();

                 */

                client = new DeskshareClient.NewBuilder().captureWidth(cWidthValue)
					.captureHeight(cHeightValue).scaleWidth(sWidthValue).scaleHeight(sHeightValue)
					.quality(qualityValue).aspectRatio(aspectRatioValue)
					.x(xValue).y(yValue).fullScreen(fullScreenValue)
					.trayIcon(icon).enableTrayIconActions(false).build();

                 client.addClientListener(this);
		client.start();
	}

	
	public void destroy() {
		System.out.println("Desktop Capture Destroy");
		client.stop();
		//super.destroy();
	}

	//@Override
	public void stop() {
		System.out.println("Desktop Capture Stopping");
		client.stop();
	//	super.stop();
	}

	public void onClientStop(ExitCode reason) {
		// determine if client is disconnected _PTS_272_
		if ( ExitCode.CONNECTION_TO_DESKSHARE_SERVER_DROPPED == reason ){
			JFrame pframe = new JFrame("Desktop Sharing Disconneted");
			if ( null != pframe ){
				client.disconnected();
				JOptionPane.showMessageDialog(pframe,
					"Disconnected. Reason: Lost connection to the server." + reason ,
					"Disconnected" ,JOptionPane.ERROR_MESSAGE );
			}else{
				System.out.println("Desktop sharing allocate memory failed.");
			}
		}else{
			client.stop();
		}
	}

}
