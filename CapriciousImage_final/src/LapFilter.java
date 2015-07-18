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


public class LapFilter {
  public LapFilter() {
  }
  
  public void getResult(int filter[][]) {
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        filter[i][j] = 0;
      }
    }
    filter[0][1] = 1;
    filter[1][0] = 1;
    filter[1][1] = -4;
    filter[1][2] = 1;
    filter[2][1] = 1;
  }
}
