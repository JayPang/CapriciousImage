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


public class GNAction {
  public GNAction() {
  }
  
  public double getResult(double mean) {
    String m = JOptionPane.showInputDialog("Set the mean of Gaussian noise");
    mean = Double.parseDouble(m);
    
    return mean;
  }
}
