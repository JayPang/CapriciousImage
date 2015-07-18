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


public class Mul {
  public Mul() {
  }
  
  public int getResult(int v[][], int x[][], int k) {
    int t = 0;
    int hh = x.length;
    int ww = x[0].length;
    for (int i = 0; i < hh; i++) {
      for (int j = 0; j < ww; j++) {
        t += v[i][j] * x[i][j];
      }
    }
    t = Math.abs(t);
    if (k == 0) {
      return t;
    }
    else {
      return (t / k);
    }
    
       
  }
}
