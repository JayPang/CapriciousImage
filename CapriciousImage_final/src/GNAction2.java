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


public class GNAction2 {
  public GNAction2() {
  }
  
  public double getResult(double svariance) {
    String sv = JOptionPane.showInputDialog("Set the standard variance of Gaussian noise");
    svariance = Double.parseDouble(sv);
    
    return svariance;
  }
}
