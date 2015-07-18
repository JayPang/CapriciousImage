import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


public class CNine {
  public CNine() {
  }
  
  public double getResult(double Q, int[][] filter) {
    String q = JOptionPane.showInputDialog("Set the value of Q(double)");
    Q = Double.parseDouble(q);
    
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
         filter[i][j] = 1;
      }
    }
    
    return Q;
    
  }
}
