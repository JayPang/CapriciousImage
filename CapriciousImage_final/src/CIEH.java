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


public class CIEH {
  public CIEH() {
  }
  
  public void getResult(int h, int w, int[] currentPixArray, int[] rbuff, int[] gbuff, int[] bbuff, int[] r, int[] g, int[] b) {
    ColorModel colorModel = ColorModel.getRGBdefault();

    for (int i = 0; i < h * w; i++) {
    r[i] = colorModel.getRed(currentPixArray[i]);
    g[i] = colorModel.getGreen(currentPixArray[i]);
    b[i] = colorModel.getBlue(currentPixArray[i]);
    }

    for (int i = 0; i < h * w; i++) {
    rbuff[i] = (255 << 24) | (r[i] << 16) | (r[i] << 8) | r[i];
    gbuff[i] = (255 << 24) | (g[i] << 16) | (g[i] << 8) | g[i];
    bbuff[i] = (255 << 24) | (b[i] << 16) | (b[i] << 8) | b[i];
    }
 
    }
  }
