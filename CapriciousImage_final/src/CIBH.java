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


public class CIBH {
  public CIBH() {
  }
  
  public void getResult(int h, int w, int[] currentPixArray, int[] bbuff, int[] b) {
    ColorModel colorModel = ColorModel.getRGBdefault();
    for (int i = 0; i < h * w; i++) {
    b[i] = colorModel.getBlue(currentPixArray[i]);
    }
    for (int i = 0; i < h * w; i++) {
    bbuff[i] = (255 << 24) | (b[i] << 16) | (b[i] << 8) | b[i];
    }

  }
}
