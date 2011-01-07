/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.bluetooth;

/**
 *
 * @author albus
 */
public interface BluetoothListener {
    public void receive(byte[] data);
    public void connected();
    public void lostConnection();
    public void infoMessage(String message);
    public void errorMessage(String message);
}
