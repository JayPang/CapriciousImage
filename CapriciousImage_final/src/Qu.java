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


public class Qu {
  public Qu() {
  }
  
  public void getResult(int[][] temp, int[] ImageSource, int graylevel, int w, int h, int m, int n) {
    ColorModel colorModel = ColorModel.getRGBdefault();
    temp = new int[h][w];
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
        temp[i][j] = colorModel.getRed(ImageSource[i * w + j]);
        }
    }
    for (int i = 0; i < h; i ++) {
      for (int j = 0; j < w; j++) {
      for (int l = 1; l <= graylevel; l++) {
        if (temp[i][j] < l * m) {
        temp[i][j] = (l - 1) * n;
        break;
        }
      }
      }
    }
    
    
  }
}
