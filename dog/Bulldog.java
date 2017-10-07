package dog;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;

class Bulldog {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
    
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    
    public static void main(String[] args){
        Bulldog server = new Bulldog();
        Bulldog.await();
    }
    public static void await(){
        int portno = 8080;
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(portno, 1, InetAddress.getByName("127.0.0.1")); //localhost
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        //wait for requests
        while(true){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try{
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();

                if(request.getURL().equals(SHUTDOWN_COMMAND)){
                    break;
                }
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                socket.close();
            
                } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}