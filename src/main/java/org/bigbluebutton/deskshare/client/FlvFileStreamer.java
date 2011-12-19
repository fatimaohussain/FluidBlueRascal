/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bigbluebutton.deskshare.client;

/**
 *
 * @author fatima
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.io.File;


import org.fluidproject.vulab.rascal.utils.http.HTTPStreamer;
//import org.fluidproject.vulab.rascal.core.utils.FastFrameBuffer;
import org.fluidproject.vulab.rascal.core.SessionFactory;


public class FlvFileStreamer {

    private BlockingQueue<String> queue;
    private static final int BUFFER_SIZE = 15;
    private volatile boolean sendFiles = false;

    private final Executor exec = Executors.newSingleThreadExecutor();
	private Runnable fileStreamer;
        private File dir;

    public FlvFileStreamer(File dir) {

        queue = new ArrayBlockingQueue<String>(BUFFER_SIZE,true);
        this.dir = dir;
    }

    public void initialize() {

        sendFiles = true;
		fileStreamer = new Runnable() {
			public void run() {
				while (sendFiles) {
					streamFlvFile();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		exec.execute(fileStreamer);

    }


    public void streamFlvFile(){

        File[] files = dir.listFiles();


		if (files != null) {
                    String fileToStream = getFileName();
                    File file = getFile(files, fileToStream);
                    if(file != null){
                        stream(file);
                    }
                }

    }


    public void finalizeStreaming(){

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {

            if(files[i].exists() ) {

                stream(files[i]);

            }
        }


    }

    private void stream(File file){
         new HTTPStreamer(file,SessionFactory.getSession());
                        //System.out.println("Deleting video file");
                        //file.delete();
                        System.out.println("Finishing  video Streaming()");
    }

    public File getFile(File[] files, String filename){

        for (int i = 0; i < files.length; i++) {

            if(files[i].exists() ) {
                String temp = files[i].getName();
                if(temp.compareTo(filename)== 0)
                {
                    return files[i];
                }
            }
        }
        return null;
    }

    public String getFileName(){
		try {

                        //String top = (queue.isEmpty())? "EMPTY" : queue.peek().toString();
			//logger.info("dequeuing "+top);
			//logger.info("Queue size is "+ queue.size());
			//if(! queue.isEmpty()) {
                            return queue.take();
                        //} else {
                          //  return null;
                        //}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public void putFileName(String filename){
		try {


                           queue.put(filename);
                        

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

    public void stopStreaming(){
        sendFiles = false;
        
        finalizeStreaming();

    }

}
