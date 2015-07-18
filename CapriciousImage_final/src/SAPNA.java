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


public class SAPNA {
  public SAPNA() {
  }
  
  public double getResult(double real_sp) {
    String sp = JOptionPane.showInputDialog("Set the probability(double) of salt noise");
   
    real_sp = Double.parseDouble(sp);
    
    return real_sp;
  }
}
