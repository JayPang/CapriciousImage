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


public class SAPNA2 {
  public SAPNA2() {
  }
  
  public double getResult(double real_pp) {
    String pp = JOptionPane.showInputDialog("Set the probability(double) of pepper noise");

    real_pp = Double.parseDouble(pp);
    
    return real_pp;
  }
}
