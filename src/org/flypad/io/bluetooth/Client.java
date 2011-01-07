/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.bluetooth;

import org.flypad.util.SimpleThread;
import java.io.IOException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
public class Client extends ManagedConnection {

    private ServerDiscoverer serverDiscoverer;
    
    public Client(
            final BluetoothListener bluetoothListener)
            throws IOException {

        super(bluetoothListener);
        /*
         * Retrieve the local device to get to the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Clients retrieve the discovery agent
         */
        discoveryAgent = localDevice.getDiscoveryAgent();

        bluetoothListener.infoMessage("Client created.");

        connect();
    }

    public void terminated() {
        super.terminated();
        connect();
    }

    private synchronized void connect() {
        if (
                serverDiscoverer != null
                && serverDiscoverer.isWorking()) {
            infoMessage("already searching");
            return;
        }

        serverDiscoverer = new ServerDiscoverer(this);
        serverDiscoverer.start();
    }

    class ServerDiscoverer extends SimpleThread {
        private final Client client;

        public ServerDiscoverer(final Client client) {
            this.client = client;
        }
        
        public void run() {
            String url = null;
            
            try {
                while (url == null) {
                    infoMessage("Searching for host...");
                    url = discoveryAgent.selectService(
                            serviceUUID,
                            ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
                            false);

                    if (url == null) {
                        infoMessage("Couldn't find host");
                    }
                }

                infoMessage("Host found. Connecting...");
                infoMessage(url);

                connect(client, (StreamConnection) Connector.open(url));
            } catch (IOException e) {
                errorMessage(e.toString());
            }
        }
    }
}
