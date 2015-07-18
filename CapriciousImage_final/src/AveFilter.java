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


public class AveFilter {
  public AveFilter() {
  }
  
  public void getResult(int boxint, int filter[][]) {
    String box = JOptionPane.showInputDialog("Set the size of box averaging filter");
    boxint = Integer.parseInt(box);

    for (int i = 0; i < boxint; i++) {
      for (int j = 0; j < boxint; j++) {
        filter[i][j] = 1;
      }
    }
  }
}
