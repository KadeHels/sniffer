package com.example.batmobile.sniffer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;

public class SnifferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sniffer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    String reachableHosts = printReachableHosts(getWLANipAddress("IPv4"));
                    Snackbar.make(view, reachableHosts, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                catch(SocketException e){
                    Snackbar.make(view, "Error: " + e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sniffer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    public class InternetUtils {
//        public static void main(String[] args) throws SocketException {
//            Scanner consoleIn = new Scanner(System.in);
//            System.out.print("1)Print your IPv4 address\n2)Print your IPv6 adress\n3)Print reachable IPv4 hosts: ");
//            int choice = consoleIn.nextInt();
//            consoleIn.close();
//            if (choice == 1 || choice == 2) {
//                String protocolVersion = choice == 1 ? "IPv4" : "IPv6";
//                InetAddress address = getWLANipAddress(protocolVersion);
//                System.out.println(address != null ? address : protocolVersion
//                        + " address not found. Is your internet down?");
//            } else if (choice == 3) {
//                InetAddress address = getWLANipAddress("IPv4");
//                if (address != null) {
//                    printReachableHosts(address);
//                } else {
//                    System.out.println("IPv4 Address not found. Is your internet down?");
//                }
//            } else {
//                System.out.println("Unknown choice.");
//            }
//        }

        public static InetAddress getWLANipAddress(String protocolVersion) throws SocketException {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                if (netint.isUp() && !netint.isLoopback() && !netint.isVirtual()) {
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        if (protocolVersion.equals("IPv4")) {
                            if (inetAddress instanceof Inet4Address) {
                                return inetAddress;
                            }
                        } else {
                            if (inetAddress instanceof Inet6Address) {
                                return inetAddress;
                            }
                        }
                    }
                }
            }
            return null;
        }

        public static String printReachableHosts(InetAddress inetAddress) throws SocketException {
            String ipAddress = inetAddress.toString();
            ipAddress = ipAddress.substring(1, ipAddress.lastIndexOf('.')) + ".";
            String otherAddress = null;
            for (int i = 0; i < 256; i++) {
                otherAddress = ipAddress + String.valueOf(i);
                try {
                    if (InetAddress.getByName(otherAddress.toString()).isReachable(50)) {
                        System.out.println(otherAddress);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return otherAddress;
        }
}
