package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class PeterClient
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
                        tosend = "Put;Peter;31105068";
                        break;
                    case 1:
                        tosend = "Put;Meatgrinder;42";
                        break;
                    case 2:
                        tosend = "Put;Disconnect;10";
                        break;
                    case 3:
                        tosend = "Put;Bhupjit;66";
                        break;
                    case 4:
                        tosend = "Get;Peter";
                        break;
                    case 5:
                        tosend = "Remove;Meatgrinder";
                        break;
                    case 6:
                        tosend = "Put;Juliusminven;56";
                        break;
                    case 7:
                        tosend = "Remove;Juliusminven";
                        break;
                    case 8:
                        tosend = "Put;IAmGonnaCommitAFelony;12";
                        break;
                    case 9:
                        tosend = "Update";
                        break;
                    case 10:
                        tosend = "Get;IAmGonnaCommitAFelony";
                        break;
                    case 11:
                        tosend = "Remove;IAmGonnaCommitAFelony";
                        break;
                    case 12:
                        tosend = "Size";
                        break;
                    default:
                        tosend = "Exit";
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
