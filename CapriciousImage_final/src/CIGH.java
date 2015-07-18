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


public class CIGH {
  public CIGH() {
  }
  
  public void getResult(int h, int w, int[] currentPixArray, int[] gbuff, int[] g) {
    ColorModel colorModel = ColorModel.getRGBdefault();
    for (int i = 0; i < h * w; i++) {
    g[i] = colorModel.getGreen(currentPixArray[i]);
    }
    for (int i = 0; i < h * w; i++) {
    gbuff[i] = (255 << 24) | (g[i] << 16) | (g[i] << 8) | g[i];
    }

  }
}
