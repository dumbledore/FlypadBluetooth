/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.bluetooth;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
 abstract class ManagedConnection
        implements Connection, TerminationListener {
    /**
     * Bluetooth Service name
     */
    protected static final String serviceName = "Flypad";

    /**
     * Bluetooth Service UUID of interest
     */
    protected static final String SERVICE_UUID =
            "8b58b6b2c6c749a6b154bf81033aa702";

    protected UUID serviceUUID = new UUID(SERVICE_UUID, false);

    protected UUID[] uuids = {serviceUUID};

    /**
     * Local Bluetooth Manager
     */
    protected LocalDevice localDevice;

    /**
     * Disovery Agent
     */
    protected DiscoveryAgent discoveryAgent;

    private final BluetoothListener bluetoothListener;
    private TwoWayConnection connection;

    public ManagedConnection(final BluetoothListener bluetoothListener) {
        this.bluetoothListener = bluetoothListener;
    }

    public void send(byte[] data) {
        if (connection != null) {
            connection.send(data);
        }
    }

    public void receive(byte[] data) {
        bluetoothListener.receive(data);
    }

    public void infoMessage(String message) {
        bluetoothListener.infoMessage(message);
    }

    public void errorMessage(String message) {
        bluetoothListener.errorMessage(message);
    }

    public void connected() {
        bluetoothListener.connected();
    }

    public void lostConnection() {
        bluetoothListener.lostConnection();
    }

    protected void connect(final ManagedConnection mc,
            final StreamConnection sc) {
        connection = new TwoWayConnection(mc, sc);
        connected();
    }
    public void close() {
        if (connection != null) {
            connection.close();
        }
    }

    public void terminated() {
        bluetoothListener.lostConnection();
    }
}
