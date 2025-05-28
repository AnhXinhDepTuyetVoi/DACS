/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mydacs.ghichu;

import javax.swing.SwingUtilities;

/**
 *
 * @author ANH
 */
public class PhanMemGhiChu {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SwingUtilities.invokeLater(() -> {
            new Giaodienchinh().setVisible(true);
        });
    }
}
