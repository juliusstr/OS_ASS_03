package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DicteClient
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 6666
            Socket s = new Socket(ip, 6666);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler

            int callNr = 0;
            String tosend;
            while (true)
            {
                System.out.println(dis.readUTF());
                switch (callNr++){
                    case 0:
                        tosend = "Put;dicte;2567";
                        break;
                    case 1:
                        tosend = "Get;dicte";
                        break;
                    case 2:
                        break;

                    catosend
                    default:
                        tosend = "EXIT";
                        break;
                }

                //String tosend = scn.nextLine();
                dos.writeUTF(tosend);
                System.out.println("sendt: " + tosend);

                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing date or time as requested by client
                String received = dis.readUTF();
                System.out.println(received);
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
