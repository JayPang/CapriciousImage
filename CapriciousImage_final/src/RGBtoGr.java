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


public class RGBtoGr {
  public RGBtoGr() {
  }
  
  public void getResult(int[] ImageSource, int[] grayArray, int w, int h) {
    ColorModel colorModel = ColorModel.getRGBdefault();
    int i, j, k, r, g, b;
    for (i = 0; i < h; i++) {
        for (j = 0; j < w; j++) {
            k = i * w + j;
            r = colorModel.getRed(ImageSource[k]);
            g = colorModel.getGreen(ImageSource[k]);
            b = colorModel.getBlue(ImageSource[k]);
            int gray = (int) (r * 0.299 + g * 0.587 + b * 0.114);
            r = g = b = gray;
            grayArray[i * w + j] = (255 << 24) | (r << 16) | (g << 8) | b;
        }
    }
    
    
    
    
  }
}
