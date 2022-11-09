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
        Semaphore semaphore = new Semaphore(1);
        Phonebook phonebook = new Phonebook();


        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos, semaphore, phonebook);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {

    static final long DELAY_MS = 10000;
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    Semaphore semaphore;
    Phonebook phonebook;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Semaphore semaphore, Phonebook phonebook)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.semaphore = semaphore;
        this.phonebook = phonebook;
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

                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Put" :
                        k = command[1];
                        v = command[2];
                        System.out.println("k: " + k + "   v: " + v);
                        semaphore.acquireUninterruptibly();
                        toreturn = phonebook.put(k,v);
                        semaphore.release();
                        if (toreturn == null)
                            toreturn = "N/A";
                        dos.writeUTF(toreturn);
                        break;

                    case "Get" :
                        k = command[1];
                        System.out.println("k: " + k);
                        semaphore.acquireUninterruptibly();
                        toreturn = phonebook.get(k);
                        semaphore.release();
                        if (toreturn == null)
                            toreturn = "N/A";
                        dos.writeUTF(toreturn);
                        break;

                    case "Remove" :
                        k = command[1];
                        System.out.println("k: " + k);
                        semaphore.acquireUninterruptibly();
                        toreturn = phonebook.remove(k);
                        Thread.sleep(DELAY_MS);
                        semaphore.release();
                        if (toreturn == null)
                            toreturn = "N/A";
                        dos.writeUTF(toreturn);
                        break;

                    case "Size" :
                        int i;
                        semaphore.acquireUninterruptibly();
                        i = phonebook.size();
                        Thread.sleep(DELAY_MS);
                        semaphore.release();
                        dos.writeUTF("" + i);
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
