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


public class Bw {
  public Bw() {
  }
  
  public void getResult(int[] BWArray, int[] ImageSource, int w, int h) {
    ColorModel colorModel = ColorModel.getRGBdefault();
    int r, g, b;
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            r = colorModel.getRed(ImageSource[i * w + j]);
            g = colorModel.getGreen(ImageSource[i * w + j]);
            b = colorModel.getBlue(ImageSource[i * w + j]);
            int gray = r  + g  + b;
            gray /= 3;
            if(gray > 127)
              r = g = b = 255;
            else
              r = g = b = 0;
            BWArray[i * w + j] = (255 << 24) | (r << 16) | (g << 8) | b;
        }
    }
    
  }
}
