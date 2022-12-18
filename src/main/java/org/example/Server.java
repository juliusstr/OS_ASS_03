package org.example;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.Semaphore;



public class Server
{

    public static void main(String[] args) throws IOException
    {
        // server is listening on port 6666
        ServerSocket ss = new ServerSocket(6666);
        // semaphore object created to pass around
        Semaphore semaphore = new Semaphore(1);
        //phonebook created also to pass around
        Phonebook phonebook = new Phonebook();


        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;//temp socket to pass to clientHandler thread

            try
            {
                // receive incoming client requests on serversocket(ss). is bloked until connection is made
                s = ss.accept();

                System.out.println("A new client is connected : " + s);//debug/info

                // obtaining input and out streams to pass to clientHandler thread
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object and passes variabels
                Thread t = new ClientHandler(s, dis, dos, semaphore, phonebook);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){//you know
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {

    static final long DELAY_MS = 2000;// deley to simulate extensive CPU usages
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    Semaphore semaphore;

    Phonebook phonebooklive;
    Phonebook phonebook;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Semaphore semaphore, Phonebook phonebook)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.semaphore = semaphore;
        semaphore.acquireUninterruptibly();//fix
        this.phonebook = phonebook.clone();
        semaphore.release();//fix
        this.phonebooklive = phonebook;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {

                // Ask user what he wants
                dos.writeUTF("HI");

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                String command[] = received.split(";");
                String k = "";
                String v = "";
                switch (command[0]) {
                    case "Put" :
                        k = command[1];
                        v = command[2];
                        System.out.println("k: " + k + "   v: " + v);
                        semaphore.acquireUninterruptibly();
                        phonebooklive.put(k,v);
                        Thread.sleep(DELAY_MS);
                        semaphore.release();
                        toreturn = phonebook.put(k,v);
                        if (toreturn == null)
                            toreturn = "N/A";
                        dos.writeUTF(toreturn);
                        break;

                    case "Get" :
                        k = command[1];
                        System.out.println("k: " + k);
                        toreturn = phonebook.get(k);
                        Thread.sleep(DELAY_MS);
                        if (toreturn == null)
                            toreturn = "N/A";
                        dos.writeUTF(toreturn);
                        break;

                    case "Remove" :
                        k = command[1];
                        System.out.println("k: " + k);
                        semaphore.acquireUninterruptibly();
                        phonebooklive.remove(k);
                        Thread.sleep(DELAY_MS);
                        semaphore.release();
                        toreturn = phonebook.remove(k);
                        if (toreturn == null)
                            toreturn = "N/A";
                        dos.writeUTF(toreturn);
                        break;

                    case "Size" :
                        int i;
                        i = phonebook.size();
                        Thread.sleep(DELAY_MS);
                        dos.writeUTF("" + i);
                        break;
                    case "Update" :
                        semaphore.acquireUninterruptibly();
                        phonebook = phonebooklive.clone();
                        Thread.sleep(DELAY_MS);
                        semaphore.release();
                        dos.writeUTF("Update done!");
                        break;
                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("error");
                break;
            } catch (ArrayIndexOutOfBoundsException e){
                try {
                    dos.writeUTF("missing info in command");
                } catch (IOException ex) {
                    e.printStackTrace();
                    System.out.println("error");
                    break;
                }
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
