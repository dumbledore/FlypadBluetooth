/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flypad.io.bluetooth;

/**
 *
 * @author albus
 */
public interface DataListener {
    public void receive(byte[] data);
}
