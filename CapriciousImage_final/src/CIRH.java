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


public class CIRH {
  public CIRH() {
  }
  
  public void getResult(int h, int w, int[] currentPixArray, int[] rbuff, int[] r) {
    ColorModel colorModel = ColorModel.getRGBdefault();
    for (int i = 0; i < h * w; i++) {
    r[i] = colorModel.getRed(currentPixArray[i]);
    }
    for (int i = 0; i < h * w; i++) {
    rbuff[i] = (255 << 24) | (r[i] << 16) | (r[i] << 8) | r[i];
    }

  }
}
