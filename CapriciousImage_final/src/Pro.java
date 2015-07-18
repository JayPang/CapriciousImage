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


public class Pro {
  public Pro() {
  }
  
  public int getResult(int i, int j) {
    int product = 1;
    for (int a = 0; a < i + j; a++) {
    product *= -1;
    }
    return product;
       
  }
}
