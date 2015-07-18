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


public class Maxx {
  public Maxx() {
  }
  
  public int getResult(int[] arr, int max_) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] >= max_) {
        max_ = arr[i];
      }
    }
    
    return max_;
  }
}
