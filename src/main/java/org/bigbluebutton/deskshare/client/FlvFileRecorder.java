/** 
*
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
*
* Copyright (c) 2010 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 2.1 of the License, or (at your option) any later
* version.
*
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
* 
**/
package org.bigbluebutton.deskshare.client;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Date;

import java.io.IOException;
import org.bigbluebutton.deskshare.client.encoder.FlvEncodeException;
import org.bigbluebutton.deskshare.client.encoder.ScreenVideoFlvEncoder;

//import org.fluidproject.vulab.rascal.ui.ScreencastApplet;
import org.fluidproject.vulab.rascal.utils.http.HTTPStreamer;
//import org.fluidproject.vulab.rascal.core.utils.FastFrameBuffer;
import org.fluidproject.vulab.rascal.core.SessionFactory;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



import java.util.Timer;
import java.util.TimerTask;


public class FlvFileRecorder {

    //public static final String DIRNAME = System.getProperty("user.home") + File.separator + "screencast";
    public static final String DIRNAME = System.getProperty("user.home") + File.separator + "blurascal";
	private FileOutputStream fo;
	private ScreenVideoFlvEncoder svf ;
        private File dir;
        private File file;
        private String filename;
        private boolean firstTime = true;

        private final Executor recorderExec = Executors.newSingleThreadExecutor();
        private Runnable recorderRunner;
        private volatile boolean startRecording = false;


	public FlvFileRecorder(){
		svf = new ScreenVideoFlvEncoder();
	}
	
	public void init() {
    	try {
    		//fo = new FileOutputStream("D://temp/" + "ScreenVideo4.flv");
                createDir(new File(DIRNAME));

                createFile();
                /*
                Date now = new Date();
		long timestamp = now.getTime();
                filename = dir.toString() + File.separator  + timestamp + ".flv";

               // fo = new FileOutputStream("C://blueRascal/"+"ScreenVideo.flv");
                file = new File(filename);
                //fo = new FileOutputStream(filename);
                fo = new FileOutputStream(file);
			fo.write(svf.encodeHeader());
                 *
                 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
/*
        public void start() {
		startRecording = true;
		recorderRunner =  new Runnable() {
			public void run() {
				while (startRecording){
					createFile();
					pause(20000);
                                        closeFile();
				}
                                System.out.println("closing file.");

			}
		};
		recorderExec.execute(recorderRunner);
	}*/

        private void pause(int dur) {
		try{
			Thread.sleep(dur);
		} catch (Exception e){
			System.out.println("Exception sleeping.");
		}
	}

        public void createFile() {
    	try {
    		//fo = new FileOutputStream("D://temp/" + "ScreenVideo4.flv");
                //createDir(new File(DIRNAME));
                Date now = new Date();
		long timestamp = now.getTime();
                filename = dir.toString() + File.separator  + timestamp + ".flv";

               // fo = new FileOutputStream("C://blueRascal/"+"ScreenVideo.flv");
                file = new File(filename);
                //fo = new FileOutputStream(filename);
                fo = new FileOutputStream(file);
			fo.write(svf.encodeHeader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

        public void closeFile()  {
    	try {
    		System.out.println("Closing File");
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


        private void createDir(File dir){
            deleteDir(dir.getPath());
            dir.mkdir();
            this.dir = dir;
        }

        /*
        public void stopRecording()  {
    	try {
                startRecording = false;
    		System.out.println("Closing File stream");
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	public void stop()  {
    	try {
               
    		System.out.println("Closing File stream");
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void record(ByteArrayOutputStream frame) {
		saveToFile(frame);
                
	}

        public void record(ByteArrayOutputStream frame, boolean newFile) {

            if(newFile){
                if (firstTime){
                    firstTime = false;
                } else{

                    closeFile();
                    createFile();
                }
                

            }
            saveToFile(frame);

	}

	
	private void saveToFile(ByteArrayOutputStream videoData) {
		try {
			fo.write(svf.encodeFlvData(videoData));
			fo.flush();				
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlvEncodeException e) {
			e.printStackTrace();
		}
	}

         /**
         *
         * A method that streams audio file after the recording is completed.
         * The audio file is deleted from the client machine afterward.
         */
        public void streamVideoFile(){

            System.out.println("Streaming video file");
		new HTTPStreamer(file,SessionFactory.getSession());
		//System.out.println("Deleting video file");
		//file.delete();
		System.out.println("Finishing  video Streaming()");

        }
        public static boolean deleteDir(String strFile) {
		// Declare variables variables
		File fDir = new File(strFile);
		String[] strChildren = null;
		boolean bRet = false;

		// Validate directory
		if (fDir.isDirectory()) {
			// -- Get children
			strChildren = fDir.list();

			// -- Go through each
			for (int i = 0; i < strChildren.length; i++) {
				bRet = deleteDir(new File(fDir, strChildren[i])
						.getAbsolutePath());
				if (!bRet) {
					return false;
				}
			}
		}


		// The directory is now empty so delete it
		return fDir.delete();

	}

}
