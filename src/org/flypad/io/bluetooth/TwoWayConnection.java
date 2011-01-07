/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.bluetooth;

import javax.microedition.io.StreamConnection;

/**
 *
 * @author albus
 */
class TwoWayConnection
        implements Connection, TerminationListener {
    private final ManagedConnection managedConnection;
    private final Reception reception;
    private final Transmission transmission;

    public TwoWayConnection(
            final ManagedConnection managedConnection,
            final StreamConnection connection) {

        this.managedConnection = managedConnection;

        reception = new Reception(this, connection, this);
        reception.start();

        transmission = new Transmission(this, connection);
        transmission.start();
    }

    public final void send(byte[] data) {
        transmission.send(data);
    }

    public final void receive(byte[] data) {
        managedConnection.receive(data);
    }

    public void connected() {
        managedConnection.connected();
    }

    public void lostConnection() {
        managedConnection.lostConnection();
    }

    public void infoMessage(String message) {
        managedConnection.infoMessage(message);
    }

    public void errorMessage(String message) {
        managedConnection.errorMessage(message);
    }

    public final void terminated() {
        reception.kill();
        transmission.kill();
        managedConnection.terminated();
    }

    public final void close() {
        reception.kill();
        transmission.kill();
    }
}
