/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.bluetooth;

import org.flypad.util.SimpleThread;
import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author albus
 */
public class Server extends ManagedConnection {
    /**
     * Define the server connection URL
     */
    private final String connURL =
            "btspp://localhost:" + serviceUUID.toString() + ";"
            + "name=" + serviceName;

    private final StreamConnectionNotifier connectionNotifier;
    private final ClientDiscoverer discoverer = new ClientDiscoverer(this);

    public Server(final BluetoothListener dataListener)
            throws IOException {

        super(dataListener);

        infoMessage("Creating server...");
        /*
         * Retrieve the local device to get the Bluetooth Manager
         */
        localDevice = LocalDevice.getLocalDevice();

        /*
         * Servers set the discoverable mode to GIAC
         */
        infoMessage("Setting GIAC...");
        localDevice.setDiscoverable(DiscoveryAgent.GIAC);

        /*
         * Create a server connection (a notifier)
         */
        connectionNotifier = (StreamConnectionNotifier) Connector.open(connURL);
        infoMessage("Server is running...");
        discoverer.start();
    }

    public final void close() {
        discoverer.kill();
        super.close();
    }

    class ClientDiscoverer extends SimpleThread {
        private final Server server;

        ClientDiscoverer(final Server server) {
            this.server = server;
        }

        public void run() {
            while (isWorking()) {
                try {
                    /*
                     * Accept a new client connection
                     */
                    infoMessage("Awaiting client connection...");
                    StreamConnection client =
                            connectionNotifier.acceptAndOpen();

                    /*
                     * Get a handle on the connection
                     */
                    RemoteDevice remote = RemoteDevice.getRemoteDevice(client);
                    infoMessage("New client connection to "
                            + remote.getFriendlyName(false));

                    connect(server, client);
                } catch (IOException e) {
                    errorMessage(e.toString());
                }
            }
        }
    }
}
