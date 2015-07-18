import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 * this class is used to store the size of an image
 */
class size {
  public int _w;
  public int _h;
  public size() {
    _w = 0;
    _h  = 0;
  }
  public size(int w, int h) {
    _w = w;
    _h = h;
  }
}

public class ImageProcessing extends JFrame {

    // current pixel array
    private int currentPixArray[] = null;

    // the file path of the image
    private String fileString = null;

    // used to display the image
    private JLabel imageLabel = null;

    // loading image
    private BufferedImage newImage;
    
    //current image
    private Image currentpic;
    private BufferedImage currentImage;

    //gray level 
    private int graylevel;

  // the height and width of the current image
    private int w;
    private int h;
    
    // the height and width after zero padding
    private int w_pad;
    private int h_pad;
    
    // the height and width of source image
    private int image_w;
    private int image_h;
    
    // zoom rate
    private double rate_w;
    private double rate_h;
    
    //coordinates
    private double f_i;
    private double f_j;
    
    /*separation between two histogram */
    private final int histPitch = 2;
    
    /* Fourier Transformation F(u,v) */
    private double Fu[] = null;
    private double Fv[] = null;
    
    /* Fast Fourier Transformation F(u,v) */
    private double FoddU[] = null;
    private double FoddV[] = null;
    private double FevenU[] = null;
    private double FevenV[] = null;
    private double FFu[] = null;
    private double FFv[] = null;

    public ImageProcessing(String title) {
        super(title);
        this.setSize(500, 550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menu
        JMenuBar jb = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        jb.add(fileMenu);
        
        //open
        JMenuItem openImageMenuItem = new JMenuItem("Open");
        fileMenu.add(openImageMenuItem);
        openImageMenuItem.addActionListener(new OpenListener());
        
        //save
        JMenuItem saveImageMenuItem = new JMenuItem("Save");
        fileMenu.add(saveImageMenuItem);
        saveImageMenuItem.addActionListener(new SaveListener());
        
        //exit
        JMenuItem exitMenu = new JMenuItem("Quit");
        fileMenu.add(exitMenu);
        exitMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu operateMenu = new JMenu("Operations");
        jb.add(operateMenu);

        JMenuItem RGBtoGrayMenuItem = new JMenuItem("toGrayScaleImg");
        operateMenu.add(RGBtoGrayMenuItem);
        RGBtoGrayMenuItem.addActionListener(new RGBtoGrayActionListener());
        
        JMenuItem histogramMenuItem = new JMenuItem("showHistogram");
        operateMenu.add(histogramMenuItem);
        histogramMenuItem.addActionListener(new HistogramActionListener());
        
        JMenuItem equalizeMenuItem = new JMenuItem("Equalize");
        operateMenu.add(equalizeMenuItem);
        equalizeMenuItem.addActionListener(new EqualizeActionListener());
        
        
        JMenuItem cMenuItem = new JMenuItem("toBWImg");
        operateMenu.add(cMenuItem);
        cMenuItem.addActionListener(new BWActionListener());
        
        JMenu viewMenu = new JMenu("View");
        jb.add(viewMenu);
        
        JMenuItem scaleMenuItem = new JMenuItem("Zoom");
        viewMenu.add(scaleMenuItem);
        scaleMenuItem.addActionListener(new ScaleActionListener());
        
        JMenuItem windowMenuItem = new JMenuItem("ViewAsWindow");
        viewMenu.add(windowMenuItem);
        windowMenuItem.addActionListener(new WindowActionListener());
        
        JMenu graylevelMenu = new JMenu("GrayLevel");
        //jb.add(graylevelMenu);
        
        JMenuItem graylevelMenuItem = new JMenuItem("SetGrayLevel");
        graylevelMenu.add(graylevelMenuItem);
        graylevelMenuItem.addActionListener(new GraylevelActionListener());
        
        JMenu filterMenu = new JMenu("Filtering");
        jb.add(filterMenu);
        
        JMenuItem averagingFilterlMenuItem = new JMenuItem("AveragingFilter");
        filterMenu.add(averagingFilterlMenuItem);
        averagingFilterlMenuItem.addActionListener(new AveragingFilterlActionListener());
        
        JMenuItem laplacianFilterlMenuItem = new JMenuItem("LaplacianFilter");
        filterMenu.add(laplacianFilterlMenuItem);
        laplacianFilterlMenuItem.addActionListener(new LaplacianFilterlActionListener());
        
        JMenuItem sobelFilterlMenuItem = new JMenuItem("SobelFilter");
        filterMenu.add(sobelFilterlMenuItem);
        sobelFilterlMenuItem.addActionListener(new SobelFilterlActionListener());
        
        JMenu hw3 = new JMenu("Advanced Filter");
        jb.add(hw3);
        
        JMenu ft = new JMenu("FourierTransformation");
        hw3.add(ft);
        
        JMenuItem dftMenuItem = new JMenuItem("DFT");
        ft.add(dftMenuItem);
        dftMenuItem.addActionListener(new DFTActionListener());
        
        JMenuItem idftMenuItem = new JMenuItem("IDFT");
        ft.add(idftMenuItem);
        idftMenuItem.addActionListener(new IDFTActionListener());
        
        JMenuItem fftMenuItem = new JMenuItem("FFT");
        //ft.add(fftMenuItem);
        fftMenuItem.addActionListener(new FFTActionListener());
        
        JMenuItem ifftMenuItem = new JMenuItem("IFFT");
        //ft.add(ifftMenuItem);
        ifftMenuItem.addActionListener(new IFFTActionListener());
        
        
        JMenu filterFre = new JMenu("FilteringInFD");
        hw3.add(filterFre);
        
        JMenuItem aveFilter = new JMenuItem("AveragingFilter");
        filterFre.add(aveFilter);
        aveFilter.addActionListener(new AveFilterActionListener());
        
        JMenuItem lapFilter = new JMenuItem("LaplacianFilter");
        filterFre.add(lapFilter);
        lapFilter.addActionListener(new LapFilterActionListener());
        
        JMenu hw4 = new JMenu("Noise And Histogram");
        jb.add(hw4);
        
        JMenu noisefilter = new JMenu("Denoising");
        hw4.add(noisefilter);
        
        JMenu meanfilters = new JMenu("MeanFilters");
        noisefilter.add(meanfilters);
        
        JMenu amf = new JMenu("ArithmeticMeanFilter");
        meanfilters.add(amf);
        
        JMenuItem amfItem1 = new JMenuItem("3 x 3");
        amf.add(amfItem1);
        amfItem1.addActionListener(new AMFThreeActionListener());
        
        JMenuItem amfItem2 = new JMenuItem("9 x 9");
        amf.add(amfItem2);
        amfItem2.addActionListener(new AMFNineActionListener());
        
        JMenu gmf = new JMenu("GeometricMeanFilter");
        meanfilters.add(gmf);
        
        JMenuItem gmfItem1 = new JMenuItem("3 x 3");
        gmf.add(gmfItem1);
        gmfItem1.addActionListener(new GMFThreeActionListener());
        
        JMenuItem gmfItem2 = new JMenuItem("9 x 9");
        gmf.add(gmfItem2);
        gmfItem2.addActionListener(new GMFNineActionListener());
        
        JMenu hmf = new JMenu("HarmonicMeanFilter");
        meanfilters.add(hmf);
        
        JMenuItem hmfItem1 = new JMenuItem("3 x 3");
        hmf.add(hmfItem1);
        hmfItem1.addActionListener(new HMFThreeActionListener());
        
        JMenuItem hmfItem2 = new JMenuItem("9 x 9");
        hmf.add(hmfItem2);
        hmfItem2.addActionListener(new HMFNineActionListener());
        
        JMenu chmf = new JMenu("ContraHarmonicMeanFilter");
        meanfilters.add(chmf);
        
        JMenuItem chmfItem1 = new JMenuItem("3 x 3");
        chmf.add(chmfItem1);
        chmfItem1.addActionListener(new CHMFThreeActionListener());
        
        JMenuItem chmfItem2 = new JMenuItem("9 x 9");
        chmf.add(chmfItem2);
        chmfItem2.addActionListener(new CHMFNineActionListener());
        
        JMenu sfilter = new JMenu("StatisticalFilters");
        noisefilter.add(sfilter);
        
        JMenuItem minfItem = new JMenuItem("MinFilter");
        sfilter.add(minfItem);
        minfItem.addActionListener(new MINFILTERActionListener());
        
        JMenuItem maxfItem = new JMenuItem("MaxFilter");
        sfilter.add(maxfItem);
        maxfItem.addActionListener(new MAXFILTERActionListener());
        
        JMenuItem medianfItem = new JMenuItem("MedianFilter");
        sfilter.add(medianfItem);
        medianfItem.addActionListener(new MEDIANFILTERActionListener());
        
        JMenu addnoise = new JMenu("AddNoise");
        hw4.add(addnoise);
        
        JMenuItem gnItem = new JMenuItem("GaussianNoise");
        addnoise.add(gnItem);
        gnItem.addActionListener(new GNActionListener());
        
        JMenuItem sapnItem = new JMenuItem("SaltandPepperNoise");
        addnoise.add(sapnItem);
        sapnItem.addActionListener(new SAPNActionListener());
        
        JMenu colorimg = new JMenu("ColorImage");
        hw4.add(colorimg);
        
        JMenuItem ehItem = new JMenuItem("Equalize");
        colorimg.add(ehItem);
        ehItem.addActionListener(new CIEHActionListener());
        
        JMenu cimghistogram = new JMenu("Histogram");
        colorimg.add(cimghistogram);
        
        JMenuItem redhistogramItem = new JMenuItem("RedChannel");
        cimghistogram.add(redhistogramItem);
        redhistogramItem.addActionListener(new CIRHActionListener());
        
        JMenuItem greenhistogramItem = new JMenuItem("GreenChannel");
        cimghistogram.add(greenhistogramItem);
        greenhistogramItem.addActionListener(new CIGHActionListener());
        
        JMenuItem bluehistogramItem = new JMenuItem("BlueChannel");
        cimghistogram.add(bluehistogramItem);
        bluehistogramItem.addActionListener(new CIBHActionListener());
        
        JMenuItem avehistogramItem = new JMenuItem("AverageHistogram");
        cimghistogram.add(avehistogramItem);
        avehistogramItem.addActionListener(new CIAHActionListener());
        
        JMenuItem sehItem = new JMenuItem("EqualizeUsingAverageRGB");
        cimghistogram.add(sehItem);
        sehItem.addActionListener(new CIEQActionListener());
        
        JMenuItem reduceintensity = new JMenuItem("ReduceIntensity");
        hw4.add(reduceintensity);
        reduceintensity.addActionListener(new RINTENSITYActionListener());
        
        JMenu dehaze = new JMenu("Dehazing");
        //jb.add(dehaze);
        
        JMenuItem darkchannel = new JMenuItem("DarkChannel");
        dehaze.add(darkchannel);
        darkchannel.addActionListener(new DARKCHActionListener());
        
        JMenuItem imgDehaze = new JMenuItem("Dehaze");
        dehaze.add(imgDehaze);
        imgDehaze.addActionListener(new DEHAZEActionListener());
        
        JMenu help = new JMenu("Help");
        jb.add(help);
        
        JMenuItem info = new JMenuItem("About");
        help.add(info);
        info.addActionListener(new INFOActionListener());

        this.setJMenuBar(jb);

        imageLabel = new JLabel("", JLabel.CENTER);
        JScrollPane pane = new JScrollPane(imageLabel);
        this.setLayout(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);

        this.setVisible(true);

    }

    /*------------------------------------------------------------------------------------------------*/
    
    private class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser jc = new JFileChooser();
            jc.setFileFilter(new FileFilter() {
                public boolean accept(File f) { 
                    if (f.getName().endsWith(".jpg") || f.isDirectory()|| f.getName().endsWith(".gif") || f.getName().endsWith(".png") || f.getName().endsWith(".bmp")) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return "图片(*.jpg,*.gif,*bmp,*png)";
                }
            });
            int returnValue = jc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jc.getSelectedFile();
                if (selectedFile != null) {
                    fileString = selectedFile.getAbsolutePath();
                    try {
                        newImage = ImageIO.read(new File(fileString));
                        w = newImage.getWidth();
                        h = newImage.getHeight();
                        graylevel = 256;
                        
                        int hxp = 1, wxp = 1;
                        while (hxp <= h) {
                          hxp *= 2;
                        }
                        h_pad = hxp ;
                        while (wxp <= w) {
                          wxp *= 2;
                        }
                        w_pad = wxp;
                        
                        image_w = w;
                        image_h = h;
                        currentPixArray = getPixArray(newImage, w, h);
                        currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
                        currentImage = toBufferedImage(currentpic);
                       
                        FoddU = new double[h_pad * w_pad / 2]; //real part of Fodd(u)
                        FoddV = new double[h_pad * w_pad / 2]; //imag part of Fodd(u)
                        FevenU = new double[h_pad * w_pad / 2];
                        FevenV = new double[h_pad * w_pad / 2];
                        FFu = new double[h_pad * w_pad];
                        FFv = new double[h_pad * w_pad];
                        imageLabel.setIcon(new ImageIcon(newImage));

                    } catch (IOException ex) {
                        System.out.println(ex);
                    }

                }
            }
            ImageProcessing.this.repaint();
        }
    }
    
    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser jc = new JFileChooser();
            int returnValue = jc.showSaveDialog(null);
            File f = jc.getSelectedFile();
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                try {
          ImageIO.write(currentImage, "jpg", f);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
            }
            ImageProcessing.this.repaint();
        }
    }
       
    private class RGBtoGrayActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int[] resultArray = RGBtoGray(currentPixArray);
            currentPixArray = resultArray;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(resultArray);
        }

    }
    
    private class HistogramActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
      plot_hist(currentPixArray);
      }
    }
    
    private class EqualizeActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int[] resultArray = equalize(currentPixArray);
            currentPixArray = resultArray;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(resultArray);
        }

    }
    
    private class BWActionListener implements ActionListener{
      
      public void actionPerformed(ActionEvent e) {
            int[] resultArray = toBW(currentPixArray);
            currentPixArray = resultArray;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(resultArray);
        }
      
    }
    
    private class GraylevelActionListener implements ActionListener{
      
      public void actionPerformed(ActionEvent e) {
            String L = JOptionPane.showInputDialog("Set level of gray");
            graylevel = Integer.parseInt(L);
            int[] resultArray = quantize(currentPixArray);
            currentPixArray = resultArray;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(resultArray);
            
        }
    }
    
    private class ScaleActionListener implements ActionListener{
      
      public void actionPerformed(ActionEvent e) {
            String width = JOptionPane.showInputDialog("Set width of photo");
            String height = JOptionPane.showInputDialog("Set height of photo");
            w = Integer.parseInt(width);
            h = Integer.parseInt(height);
            
            rate_h=((double)image_h) / h;
            rate_w=((double)image_w)/ w;
            
            int[] resultArray = myscale(currentPixArray);
            currentPixArray = resultArray;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(resultArray);
        }
    }
    
    private class WindowActionListener implements ActionListener {
        
      public void actionPerformed(ActionEvent e) {
          String width = JOptionPane.showInputDialog("Set width of photo");
          String height = JOptionPane.showInputDialog("Set height of photo");
          size v = new size(Integer.parseInt(width),Integer.parseInt(height));
          view_as_window(currentPixArray, v);
      }
    }
    
    private class AveragingFilterlActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        String box = JOptionPane.showInputDialog("Set the size of box averaging filter");
        int boxint = Integer.parseInt(box);
        int filter[][] = new int[boxint][boxint];
        for (int i = 0; i < boxint; i++) {
          for (int j = 0; j < boxint; j++) {
            filter[i][j] = 1;
          }
        }
        filter2d(currentPixArray, filter);
      }
    }
    
    private class LaplacianFilterlActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
          int filter[][] = new int[3][3];
          for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
              filter[i][j] = 0;
            }
          }
          filter[0][1] = 1;
          filter[1][0] = 1;
          filter[1][1] = -4;
          filter[1][2] = 1;
          filter[2][1] = 1;
          
          filter2d(currentPixArray, filter);
        }
      }
    
    private class SobelFilterlActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
          String box = JOptionPane.showInputDialog("Set the size of Sobel filter(2 or 3)");
          int boxint = Integer.parseInt(box);
          int filter[][] = new int[boxint][boxint];
          if (boxint == 2) {
          filter[0][0] = -1;
            filter[1][0] = 0;
            filter[1][0] = 0;
            filter[1][1] = 1;
          }
          if (boxint == 3) {
          filter[0][0] = -1;
            filter[0][1] = -2;
            filter[0][2] = -1;
            filter[1][0] = 0;
            filter[1][1] = 0;
            filter[1][2] = 0;
            filter[2][0] = 1;
            filter[2][1] = 2;
            filter[2][2] = 1;
          }
          
          filter2d(currentPixArray, filter);
        }
      }
    
    private class DFTActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
      int data[] = dft2d(currentPixArray, 0, new size(w, h));
      JFrame bf = new JFrame("Fourier Spectrum");
        bf.setSize(w + 30, h + 50);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();
        bf.setVisible(true);
      }
    }
    
    private class IDFTActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
        int data[] = dft2d(currentPixArray, 1, new size(w, h));
        JFrame bf = new JFrame("Fourier Spectrum");
          bf.setSize(w + 30, h + 50);
          JLabel Label = new JLabel("");
          JScrollPane pane1 = new JScrollPane(Label);
          bf.add(pane1, BorderLayout.CENTER);
          Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
          ImageIcon ii = new ImageIcon(buff);
          Label.setIcon(ii);
          Label.repaint();
          bf.setVisible(true);
        }
      }
    
    private class FFTActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
          int data[] = fft2d(currentPixArray, 0);
          JFrame bf = new JFrame("Fourier Spectrum");
          bf.setSize(w + 30, h + 50);
          JLabel Label = new JLabel("");
          JScrollPane pane1 = new JScrollPane(Label);
          bf.add(pane1, BorderLayout.CENTER);
          Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
          ImageIcon ii = new ImageIcon(buff);
          Label.setIcon(ii);
          Label.repaint();
          bf.setVisible(true);
        }
      }
    
    private class IFFTActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
          int data[] = fft2d(currentPixArray, 1);
          JFrame bf = new JFrame("Fourier Spectrum");
          bf.setSize(w + 30, h + 50);
          JLabel Label = new JLabel("");
          JScrollPane pane1 = new JScrollPane(Label);
          bf.add(pane1, BorderLayout.CENTER);
          Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
          ImageIcon ii = new ImageIcon(buff);
          Label.setIcon(ii);
          Label.repaint();
          bf.setVisible(true);
        }
      }
    
    private class AveFilterActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
      //String box = JOptionPane.showInputDialog("Set the size of box averaging filter");
        int boxint = 0;
        int filter[][] = new int[boxint][boxint];
        
        AveFilter ave = new AveFilter();
        ave.getResult(boxint, filter);
        
        int data[] = filter2d_freq(currentPixArray, filter);
        JFrame bf = new JFrame("Averaging Filter in Frequancy Domain");
        bf.setSize(w + 30, h + 50);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();
        bf.setVisible(true);
      }
    }

    private class LapFilterActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        int filter[][] = new int[3][3];
        LapFilter lap = new LapFilter();
        lap.getResult(filter);
          
        int data[] = filter2d_freq(currentPixArray, filter);
        JFrame bf = new JFrame("Laplacian Filter in Frequancy Domain");
        bf.setSize(w + 30, h + 50);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();
        bf.setVisible(true);
      }
    }
    
    /*********************************Homework 4**********************************************/
    /*********************************Homework 4**********************************************/
    private class AMFThreeActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        int filter[][] = new int[3][3];
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
             filter[i][j] = 1;
          }
        }
        filter2d(currentPixArray, filter);
      }
    }
    
    private class AMFNineActionListener implements ActionListener {
      
        public void actionPerformed(ActionEvent e) {
          int filter[][] = new int[9][9];
          for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
               filter[i][j] = 1;
            }
          }
          filter2d(currentPixArray, filter);
        }
     }
    
    private class GMFThreeActionListener implements ActionListener {
      
        public void actionPerformed(ActionEvent e) {
          Gthree three = new Gthree();
          int filter[][] = three.getResult();
          filter2d_3(currentPixArray, filter);
        }
      }
      
    private class GMFNineActionListener implements ActionListener {
        
          public void actionPerformed(ActionEvent e) {
              Gnine nine = new Gnine();
              int filter[][] = nine.getResult();
              filter2d_3(currentPixArray, filter);
          }
       }
    
    private class HMFThreeActionListener implements ActionListener {
      
        public void actionPerformed(ActionEvent e) {
           int filter[][] = new int[3][3];
           for (int i = 0; i < 3; i++) {
             for (int j = 0; j < 3; j++) {
                filter[i][j] = 1;
             }
           }
           filter2d_1(currentPixArray, filter);
         }
       }
    
    private class HMFNineActionListener implements ActionListener {
      
       public void actionPerformed(ActionEvent e) {
          int filter[][] = new int[9][9];
          for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
               filter[i][j] = 1;
            }
          }
          filter2d_1(currentPixArray, filter);
        }
      }
     
    private class CHMFThreeActionListener implements ActionListener {
      
         public void actionPerformed(ActionEvent e) {
            int filter[][] = new int[3][3];
            double Q = 0;
            
            CThree ct = new CThree();
            Q = ct.getResult(Q, filter);

            filter2d_2(currentPixArray, filter, Q);
         }
    }
     
    private class CHMFNineActionListener implements ActionListener {
      
        public void actionPerformed(ActionEvent e) {

           double Q = 0;
           int filter[][] = new int[9][9];

           CNine cn = new CNine();
           Q = cn.getResult(Q, filter);
           
           filter2d_2(currentPixArray, filter, Q);
         }
    }

    private class GNActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {

            double mean = 0;
            double svariance = 0;
            GNAction gna = new GNAction();
            GNAction2 gna2 = new GNAction2();
            mean = gna.getResult(mean);
            svariance = gna2.getResult(svariance);
            
            int result[] = addGaussianNoise(currentPixArray, mean, svariance);
            
            currentPixArray = result;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(result);
      }
    }
    
    private class SAPNActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {

        double real_sp = 0;
        double real_pp = 0;
        SAPNA sap = new SAPNA();
        SAPNA2 sap2 = new SAPNA2();
        real_sp = sap.getResult(real_sp);
        real_pp = sap2.getResult(real_pp);
        
        int result[] = addSaltandPepperNoise(currentPixArray, real_sp, real_pp);
        currentPixArray = result;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(result);
      }
    }
    
    private class MINFILTERActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        statisticalFilter(currentPixArray, 0);
      }
    }
    
    private class MAXFILTERActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        statisticalFilter(currentPixArray, 1);
      }
    }
    
    private class MEDIANFILTERActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        statisticalFilter(currentPixArray, 2);
      }
    }
    
    private class CIEHActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        ColorModel colorModel = ColorModel.getRGBdefault();
        int r[] = new int[h * w];
        int g[] = new int[h * w];
        int b[] = new int[h * w];

        int rbuff[] = new int[h * w];
        int gbuff[] = new int[h * w];
        int bbuff[] = new int[h * w];
        

        CIEH cieh = new CIEH();
        cieh.getResult(h, w, currentPixArray, rbuff, gbuff, bbuff, r,g,b);
        int rtemp[] = equalize(rbuff);
        int gtemp[] = equalize(gbuff);
        int btemp[] = equalize(bbuff);
        for (int i = 0; i < h * w; i++) {
          r[i] = colorModel.getRed(rtemp[i]);
          g[i] = colorModel.getGreen(gtemp[i]);
          b[i] = colorModel.getBlue(btemp[i]);
          }
        
        int result[] = new int[h * w];
        for (int i = 0; i < h * w; i++) {
        result[i] = (255 << 24) | (r[i] << 16) | (g[i] << 8) | b[i];
        }
        
          currentPixArray = result;
          currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
          currentImage = toBufferedImage(currentpic);
          showImage(result);
      }
    }
    
    private class CIRHActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        ColorModel colorModel = ColorModel.getRGBdefault();
        int r[] = new int[h * w];

        int rbuff[] = new int[h * w];
        
        CIRH cirh = new CIRH();
        cirh.getResult(h, w, currentPixArray, rbuff, r);

        plot_hist(rbuff);
      }
    }
    
    private class CIGHActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        ColorModel colorModel = ColorModel.getRGBdefault();
          int g[] = new int[h * w];

          int gbuff[] = new int[h * w];
          
          CIGH cigh = new CIGH();
          cigh.getResult(h, w, currentPixArray, gbuff, g);

          plot_hist(gbuff);
      }
    }
    
    private class CIBHActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        ColorModel colorModel = ColorModel.getRGBdefault();
        int b[] = new int[h * w];

        int bbuff[] = new int[h * w];
        
        CIBH cibh = new CIBH();
        cibh.getResult(h, w, currentPixArray, bbuff, b);

        plot_hist(bbuff);
      }
    }
    
    private class CIAHActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
          plot_hist(currentPixArray); 
      }
    }
    
    private class CIEQActionListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        int result[] = equalize_2(currentPixArray);
        currentPixArray = result;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(result);
        }
    }
    /*********************************Homework 4**********************************************/
    /*********************************Homework 4**********************************************/
    
    private class RINTENSITYActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        String text = JOptionPane.showInputDialog("Set the rate(double) you want to reduce");
        double rate = Double.parseDouble(text);
        int data[] = reduceIntensity(currentPixArray, rate);
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        JFrame bf = new JFrame("Result");
          bf.setSize(w + 30, h + 50);
          JLabel Label = new JLabel("");
          JScrollPane pane1 = new JScrollPane(Label);
          bf.add(pane1, BorderLayout.CENTER);
          ImageIcon ii = new ImageIcon(buff);
          Label.setIcon(ii);
          Label.repaint();

          bf.setVisible(true);
      }
    }
    
    /*****************************************Final Project*****************************************/
    /*****************************************Final Project*****************************************/
    private class DARKCHActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        int temp[] = getDarkChannel(currentPixArray);
        int data[] = new int[h * w];
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
          data[i * w + j] = (255 << 24) | (temp[i * w + j] << 16) | (temp[i * w + j] << 8) | temp[i * w + j];
          }
        }
        JFrame bf = new JFrame("DarkChannel");
          bf.setSize(w + 50, h + 50);
          JLabel Label = new JLabel("");
          JScrollPane pane1 = new JScrollPane(Label);
          bf.add(pane1, BorderLayout.CENTER);
          Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
          ImageIcon ii = new ImageIcon(buff);
          Label.setIcon(ii);
          Label.repaint();
          bf.setVisible(true);
      }
    }
    
    private class DEHAZEActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        int result[] = dehaze(currentPixArray);
        currentPixArray = result;
            currentpic = createImage(new MemoryImageSource(w, h, currentPixArray, 0, w));
            currentImage = toBufferedImage(currentpic);
            showImage(result);
      }
    }
    /*****************************************Final Project*****************************************/
    /*****************************************Final Project*****************************************/
    
    private class INFOActionListener implements ActionListener {
      
      public void actionPerformed(ActionEvent e) {
        JFrame vin = new JFrame("@Jay Pang");
        JPanel mjp = new JPanel();
        JLabel lab = new JLabel();
        lab.setText("Copyright  Jay Pang All Rights Reserved");
        vin.add(mjp);
        mjp.add(lab);
        vin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        vin.setSize(300,300); 
        vin.setVisible(true); 
        
      }
    }
    
    /*------------------------------------------------------------------------------------------------*/
    
    /*
     * get the pixel array of the image
     */
    private int[] getPixArray(Image im, int w, int h) {
      BufferedImage bimage = toBufferedImage(im);
      int pix[] = new int[w * h];
      
      bimage.getRGB(0, 0, w, h, pix, 0, w);
      return pix;
    }
    
    /*
     * convert to BufferedImage
     */
    public static BufferedImage toBufferedImage(Image image){
      if (image instanceof BufferedImage){
        return (BufferedImage)image;
      }
      image = new ImageIcon(image).getImage();
      BufferedImage bimage = null;
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      try{
        int transparency = Transparency.OPAQUE;
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null),transparency);
      }catch(HeadlessException e){
        //
      }
      if (bimage == null){
        int type = BufferedImage.TYPE_INT_RGB;
        bimage = new BufferedImage(image.getWidth(null),image.getHeight(null),type);
      }
      
      Graphics g = bimage.createGraphics();
      
      g.drawImage(image,0,0,null);
      g.dispose();
      
      return bimage;
    }

    /*
     * display the image
     */
    private void showImage(int[] srcPixArray) {
        Image pic = createImage(new MemoryImageSource(w, h, srcPixArray, 0, w));
        ImageIcon ic = new ImageIcon(pic);
        imageLabel.setIcon(ic);
        imageLabel.repaint();
    }
    
    /*------------------------------------------------------------------------------------------------*/
    
    /*
     * zoom
     */
    public int[] myscale(int[] ImageSource){
      int[] grayArray = new int[h * w];
      ColorModel colorModel = ColorModel.getRGBdefault();
      int i, j, r, g, b, x, y;
      double u, v;
      for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
               
                f_i = i*rate_h;
                f_j = j*rate_w;
                //x,y为浮点坐标（f_i,f_j）的整数部分
                x = (int) f_i;
                y = (int) f_j; 
                
                //u,v为浮点坐标（f_i,f_j）的小数部分
                u = f_i - x;
                v = f_j - y;
                
                int gray1 = colorModel.getRed(ImageSource[ (x*image_w+y > (image_w-1)*(image_h-1) ? (image_w-1)*(image_h-1) : x*image_w+y )]);
                int gray2 = colorModel.getRed(ImageSource[(x*image_w+y+1 > (image_w-1)*(image_h-1) ? (image_w-1)*(image_h-1) : x*image_w+y+1 )]);
                int gray3 = colorModel.getRed(ImageSource[((x+1)*image_w+y > (image_w-1)*(image_h-1) ? (image_w-1)*(image_h-1) : (x+1)*image_w+y )]);
                int gray4 = colorModel.getRed(ImageSource[((x+1)*image_w+y+1 > (image_w-1)*(image_h-1) ? (image_w-1)*(image_h-1) : (x+1)*image_w+y+1 )]);
                
                double gray = (1-u)*(1-v)*gray1 + (1-u)*v*gray2 + u*(1-v)*gray3 + u*v*gray4;
                r = g = b = (int)gray;
                grayArray[i * w + j] = (255 << 24) | (r << 16) | (g << 8) | b;
            }
        }
        return grayArray;
    }

    /*
     * convert to gray scale image
     */
    public int[] RGBtoGray(int[] ImageSource) {
        int[] grayArray = new int[h * w];
        
        RGBtoGr rgb = new RGBtoGr();
        rgb.getResult(ImageSource, grayArray, w, h);
        
        return grayArray;
    }
    
    /*
     * change its gray level
     */
    public int[] quantize(int[] ImageSource) {
      int m = 256 / graylevel;
      int n = 255 / (graylevel - 1);

      int temp[][] = new int[h][w];
      
      Qu qu = new Qu();
      qu.getResult(temp, ImageSource, graylevel, w, h, m, n);
      
      int data[] = new int[h * w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
        data[i * w + j] = (255 << 24) | (temp[i][j] << 16) | (temp[i][j] << 8) | temp[i][j];
        }
      }
      return data;
    }
    
    /*
     * convert to black and white image
     */
    public int[] toBW(int[] ImageSource) {
        int[] BWArray = new int[h * w];

        Bw bb = new Bw();
        bb.getResult(BWArray, ImageSource, w, h);
        
        return BWArray;
    }

    /*
     * histogram equalization
     */
    public int[] equalize(int[] srcPixArray) {
      int temp[] = new int[w * h];
        int data[] = new int[256];
        double p[] = new double[256];
        double c[] = new double[256];
        int k = 0;
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            temp[k] = (srcPixArray[i * w + j] & 0xff);
            k ++;
          }
        }
        for (int i = 0; i <= 255; i++) {
          data[i] = 0;
          c[i] = 0.0f;
        }
        for (int i = 0; i < w * h; i++) {
          for (int j = 0; j <= 255; j++) {
            if (temp[i] == j) {
              data[j] ++;
              break;
            }
          }
        }
        
        for (int i = 0; i <= 255; i++) {
          p[i] = (double)data[i] / (w * h);
        }
        
        for (int i = 0; i <= 255; i++) {
          for (int j = 0; j <= i; j++) {
            c[i] += p[j];
          }
        }
        
        /*int max, min; 
        max = min = (srcPixArray[0] & 0xff);
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            if (max < (srcPixArray[i * w + j] & 0xff)) {
              max = (srcPixArray[i * w + j] & 0xff);
            }
            else if (min > (srcPixArray[i * w + j] & 0xff)) {
              min = (srcPixArray[i * w + j] & 0xff);
            }
          }
        }*/
        
        int t[] = new int[w * h];
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
          //int hist = (int)(c[srcPixArray[i * w + j] & 0xff] * (max - min) + min);
          int hist = (int)(c[srcPixArray[i * w + j] & 0xff] * (graylevel - 1));
            t[i * w + j] = 255 << 24 | hist << 16 | hist << 8 | hist;
          }
        }
        return t;
    }
    
    /*
     * this function is used to calculate the max value in an array 
     */
    private int Max(int arr[]) {
      int max = arr[0];
      
      Maxx m = new Maxx();
      max = m.getResult(arr, max);
      

      return max;
    }
    
    /**
     * this function is used to draw the histogram
     */
    public BufferedImage paintHistogram(int v[]) {
      int width = 300;
      int height = 300;
      ColorModel colorModel = ColorModel.getRGBdefault();
      int r, g, b;
      int data[] = new int[256];
      for (int i = 0; i <= 255; i++) {
        data[i] = 0;
      }
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        r = colorModel.getRed(v[i * w + j]);
        g = colorModel.getGreen(v[i * w + j]);
        b = colorModel.getBlue(v[i * w + j]);
        for (int k = 0; k <= 255; k++) {
            if (r == k) {
              data[k] ++;
              break;
            }
        }
        for (int k = 0; k <= 255; k++) {
            if (g == k) {
              data[k] ++;
              break;
            }
          }
        for (int k = 0; k <= 255; k++) {
            if (b == k) {
              data[k] ++;
              break;
            }
          }
        }
      }
    
      BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D gp = buffImg.createGraphics();
      gp.setColor(Color.BLACK);
      gp.fillRect(0, 0, width, height);
      gp.setColor(Color.WHITE);
      int max = Max(data);
      double scaling = 200.0f/((double)max);
      
      gp.drawLine(10, height - 10, width - 10, height - 10);  // draw x axis
      gp.drawLine(10, height - 10, 10, 10);  //draw y axis

      for (int i = 0; i < data.length; i++) {
        gp.drawLine(10+histPitch+i, height-10, 10+histPitch+i, height-10-(int)(data[i] * scaling));
      }
      return buffImg;

    }
    
    /**
     * this function is used to display the histogram of the input image
     */
    public void plot_hist(int src[]) {
      BufferedImage buff = paintHistogram(src);
      JFrame bf = new JFrame("Histogram");
      bf.setSize(400, 400);
      JLabel Label = new JLabel("");
      JScrollPane pane1 = new JScrollPane(Label);
      bf.add(pane1, BorderLayout.CENTER);
      ImageIcon ii = new ImageIcon(buff);
      Label.setIcon(ii);
      Label.repaint();

      bf.setVisible(true);
    }
    
    /**
     * this function is used to extracts all patches of the input image
     */
    public void view_as_window(int src[], size patch) {
      int k = (w - patch._w + 1) * (h - patch._h + 1);
      int temp[][][] = new int[k][patch._h][patch._w];
      ColorModel colorModel = ColorModel.getRGBdefault();
       
      for (int i = 0; i < h - patch._h + 1; i++) {
        for (int j = 0; j < w - patch._w + 1; j++) {
          int p = i * (w - patch._w + 1) + j;
          for (int s = i; s < patch._h + i; s++) {
            for (int t = j; t < patch._w + j; t++) {
              temp[p][s - i][t - j] = colorModel.getRed(src[s * w + t]);
            }
          }
        }
      }
      
      Random rd = new Random();
      for (int i = 0; i < 8; i++) {
      String str = "Patch_" + i;
        JFrame bf = new JFrame(str);
        bf.setSize(patch._w + 10, patch._h + 10);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        int data[] = new int[patch._h * patch._w];
        int num = rd.nextInt(k);
        for (int j = 0; j < patch._h; j++) {
          for (int m = 0; m < patch._w; m++) {
            int rgb = temp[num][j][m];
            data[j * patch._w + m] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
          }
        }
        Image buff = createImage(new MemoryImageSource(patch._w, patch._h, data, 0, patch._w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();

        bf.setVisible(true);
      }
    }

    /*
     * dot product of two masks
     */
    private int Multiple(int v[][], int x[][], int k) {
      Mul m = new Mul();
      return m.getResult(v, x, k);
    }
    
    private int[][][] view_as_window_2(int src[], size patch) {
        int k = (w - patch._w + 1) * (h - patch._h + 1);
        int temp[][][] = new int[k][patch._h][patch._w];
        ColorModel colorModel = ColorModel.getRGBdefault();
         
        for (int i = 0; i < h - patch._h + 1; i++) {
          for (int j = 0; j < w - patch._w + 1; j++) {
            int p = i * (w - patch._w + 1) + j;
            for (int s = i; s < patch._h + i; s++) {
              for (int t = j; t < patch._w + j; t++) {
                temp[p][s - i][t - j] = colorModel.getRed(src[s * w + t]);
              }
            }
          }
        }
        return temp;
    }
    
    /**
     * this function is used to perform spatial filtering
     */
    public void filter2d(int src[], int filter[][]) {
      int sum = 0;
      int f_h = filter.length;
      int f_w = filter[0].length;
      ColorModel colorModel = ColorModel.getRGBdefault();
    
      for (int i = 0; i < f_h; i++) {
        for (int j =0; j < f_w; j++) {
        sum += filter[i][j];
        }
      }
     
      int half1 = f_h / 2;
      int half2 = f_w / 2;
      int kk[][][] = view_as_window_2(src, new size(f_w, f_h));
      
      int temp[][] = new int[h][w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          temp[i][j] = colorModel.getRed(src[i * w + j]);
        }
      }
      
      int k = (w - f_w + 1) * (h - f_h + 1);
      int m = half1, n = half2;
      for (int i = 0;i < k; i++) {
        if ((i + 1) % (w - f_w + 1) == 0) {
        temp[m][n] = Multiple(kk[i], filter, sum);
          m ++;
          n = half2;
        }
        else {
          temp[m][n] = Multiple(kk[i], filter, sum);
          n ++;
        }
      }
      
      JFrame bf = new JFrame("Filtering");
      bf.setSize(w + 50, h + 50);
      JLabel Label = new JLabel("");
      JScrollPane pane1 = new JScrollPane(Label);
      bf.add(pane1, BorderLayout.CENTER);
      int data[] = new int[h * w];
      for (int x = 0; x < h; x++) {
        for (int y = 0; y < w; y++) {
          int rgb = temp[x][y];
          data[x * w + y] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
        }
      }
      Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
      ImageIcon ii = new ImageIcon(buff);
      Label.setIcon(ii);
      Label.repaint();

      bf.setVisible(true);
    }
    
    
    /*
     * return the product of i+j (-1)
     */
    private int pro(int i, int j) {
      Pro p = new Pro();
      return p.getResult(i, j);
    }
    
    /*
     * this function is used to perform Fourier Transformation
     */
    private int[] dft(int src[], size s) {
      Fu = new double[s._h * s._w];  //real part of F(u,v)
      Fv = new double[s._h * s._w];  //imag part of F(u,v)
      ColorModel colorModel = ColorModel.getRGBdefault();
      int temp[][] = new int[s._h][s._w];
      int data[] = new int[s._h * s._w];
        
      double dData[] = new double[s._h * s._w];
      for (int i = 0; i < s._h; i++) {
        for (int j = 0; j < s._w; j++) {
          temp[i][j] = colorModel.getRed(src[i * s._w + j]) * pro(i, j);
        }
      }
      double hw = (double)1.0 / (s._h * s._w);
      for (int i = 0; i < s._h; i++) {
        for (int j = 0; j < s._w; j++) {
        double Real = 0.0;
      double Imag = 0.0;
        for (int m = 0; m < s._h; m++) {
          for (int n = 0; n < s._w; n++) {
          Real += temp[m][n] * Math.cos(2 * Math.PI * ((double)i * m / s._h + (double)j * n / s._w));
          Imag -= temp[m][n] * Math.sin(2 * Math.PI * ((double)i * m / s._h + (double)j * n / s._w));
          }
        }
        Fu[i * s._w + j] = Real * hw;
        Fv[i * s._w + j] = Imag * hw;
        dData[i * s._w + j] = Math.sqrt(Real * Real + Imag * Imag) * hw;
        }
      }
      for (int i = 0; i < s._h; i++) {
      for (int j = 0; j < s._w; j++) {
        double val = (dData[i * s._w + j]);
        val *= 100;
        if (val > 255.0) {
        val = 255.0;
        }
        int rgb = (int)val;
        data[i * s._w + j] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
      }
      }
      return data; 
    }
    
    /*
     * this function is used to perform inverse Fourier Transformation
     */
    private int[] idft(size s) {
      int data[] = new int[s._h * s._w];  
      double dData[] = new double[s._h * s._w];
      for (int i = 0; i < s._h; i++) {
        for (int j = 0; j < s._w; j++) {
          double Real = 0.0;
          for (int m = 0; m < s._h; m++) {
            for (int n = 0; n < s._w; n++) {
            Real += Fu[m * s._w + n] * Math.cos(2 * Math.PI * ((double)i * m / s._h + (double)j * n / s._w)) 
                - Fv[m * s._w + n] * Math.sin(2 * Math.PI * ((double)i * m / s._h + (double)j * n / s._w));
            }
          }
          dData[i * s._w + j] = Real;
        }
      }
      for (int i = 0; i < s._h; i++) {
        for (int j = 0; j < s._w; j++) {
        dData[i * s._w + j] *= pro(i, j); 
        }
      }
      for (int i = 0; i < s._h; i++) {
      for (int j = 0; j < s._w; j++) {
        int rgb = (int)dData[i * s._w + j];
        data[i * s._w + j] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
      }
      }
      return data;
    }
    
    /*
     * this function is used to perform Fourier Transformation and 
     * its inverse Fourier Transformation
     */
    public int[] dft2d(int src[], int flags, size s) {
      int data[] = new int[s._h * s._w];
      while (flags < 2) {
      data = dft(src, s);
      if (flags == 0) {
        break;
      }
        if (flags == 1) {
        data = idft(s);
        flags ++;
        }
      }
      return data;
    }
    
    /*************************************************************************************/
    /*
     * this function is used to perform Fast Fourier Transformation
     */
    private int[] fft(int src[]) {
      System.out.println("Hello FFT!!!");
      ColorModel colorModel = ColorModel.getRGBdefault();
      int data[] = new int[h * w];
      int half = h_pad * w_pad / 2;
      double oddData[] = new double[half];
      double evenData[] = new double[half];
      double dData[] = new double[h_pad * w_pad];
      
      int temp[] = new int[h_pad * w_pad];
      for (int i = 0; i < h_pad; i++) {
        for (int j = 0; j < w_pad; j++) {
          if (i < h && j < w) {
            temp[i * w_pad + j] = colorModel.getRed(src[i * w + j]) * pro(i, j);
          }
          else {
            temp[i * w_pad + j] = 0;
          }
        }
      }
      
      double ha = (double)1.0 / half;
      /* odd part */
      for (int i = 1; i < half; i += 2) {
      double Real = 0.0;
      double Imag = 0.0;
      for (int j = 1; j < half; j += 2) {
        Real += temp[j] * Math.cos(2 * Math.PI * (double)i * j / half);
      Imag -= temp[j] * Math.sin(2 * Math.PI * (double)i * j / half);
      }
      FoddU[i] = Real * ha;
    FoddV[i] = Imag * ha;
      oddData[i] = Math.sqrt(Real * Real + Imag * Imag) * ha;
      }
      /* even part */
      for (int i = 0; i < half; i += 2) {
        double Real = 0.0;
        double Imag = 0.0;
        for (int j = 0; j < half; j += 2) {
          Real += temp[j] * Math.cos(2 * Math.PI * (double)i * j / half);
        Imag -= temp[j] * Math.sin(2 * Math.PI * (double)i * j / half);
        }
        FevenU[i] = Real * ha;
      FevenV[i] = Imag * ha;
        evenData[i] = Math.sqrt(Real * Real + Imag * Imag) * ha;
      }
      /* get the first half number of |F(u)| */
      int xx = 0;
      int yy = 0;
      for (int i = 0; i < half; i++) {
      if (i % 2 == 0) {
        FFu[i] = FevenU[xx];
        FFv[i] = FevenV[xx];
        dData[i] = evenData[xx];
        xx ++;
      }
      else {
        FFu[i] = FoddU[yy];
        FFv[i] = FoddV[yy];
        dData[i] = oddData[yy];
        yy ++;
      }
      }
      for (int i = half; i < h_pad * w_pad; i++) {
      double a = FoddU[i - half] * Math.cos(Math.PI * (double)(i - half) / half) 
             + FoddV[i - half] * Math.sin(Math.PI * (double)(i - half) / half);
      double b = -(FoddU[i - half] * Math.sin(Math.PI * (double)(i - half) / half)) 
             + FoddV[i - half] * Math.cos(Math.PI * (double)(i - half) / half);
      FFu[i] = 0.5 * (FevenU[i - half] - a);
      FFv[i] = 0.5 * (FevenV[i - half] - b);
      double c = Math.sqrt((FevenU[i - half] - a) * (FevenU[i - half] - a) + (FevenV[i - half] - b) * (FevenV[i - half] - b));
      dData[i] = 0.5 * c;
      }
      
      double ddData[] = new double[h * w];
      for (int i = 0; i < h_pad; i++) {
      for (int j = 0; j < w_pad; j++) {
        if (i < h && j < w) {
         ddData[i * w + j] = dData[i * w_pad + j];
        }
      }
      }
    
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          double val = (ddData[i * w + j]);
          val *= 100;
          if (val > 255.0) {
          val = 255.0;
          }
          int rgb = (int)val;
          data[i * w + j] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
        }
      }
      return data;
    }
    
    /*
     * this function is used to perform inverse Fast Fourier Transformation
     */
    private int[] ifft() {
      System.out.println("Hello IFFT!!!");
      int data[] = new int[h * w];  
      double dData[] = new double[h_pad * w_pad];
      for (int i = 0; i < h_pad * w_pad; i++) {
        double Real = 0.0;
        for (int j = 0; j < h_pad * w_pad; j++) {
          Real += FFu[j] * Math.cos(Math.PI * ((double)i / h)) - FFv[j] * Math.sin(2 * Math.PI * ((double)i / h));
        }
        dData[i] = Real;
      }
      
      double ddData[] = new double[h * w];
      for (int i = 0; i < h_pad; i++) {
        for (int j = 0; j < w_pad; j++) {
          if (i < h && j < w) {
          ddData[i * w + j] = dData[i * w_pad + j];
          }
        }
      }
      
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
        ddData[i * w + j] *= pro(i, j); 
        }
      }
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        int rgb = (int)ddData[i * w + j];
        data[i * w + j] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
      }
      }
      return data;
    }
    
    /*
     * this function is used to perform Fast Fourier Transformation and 
     * its inverse Fast Fourier Transformation
     */
    public int[] fft2d(int src[], int flags) {
      int data[] = new int[h * w];
      if (flags == 0) {
        data = fft(src);
      }
      if (flags == 1) {
        data = ifft();
      }
      return data; 
    }
    /*************************************************************************************/
    
    /*
     * zero padding of two matrixes with the same size 
     */
    private int[][][] padding(int a[][], int b[][]) {
      int ah = a.length;
      int aw = a[0].length;
      int bh = b.length;
      int bw = b[0].length;
      int p = ah + bh;
      int q = aw + bw;
      int x[][] = new int[p][q];
      int y[][] = new int[p][q];
      for (int i = 0; i < p; i++) {
      for (int j = 0; j < q; j++) {
        if (i < ah && j < aw ) {
        x[i][j] = a[i][j];
        }
        else {
        x[i][j] = 0;
        }
      }
      }
      for (int i = 0; i < p; i++) {
        for (int j = 0; j < q; j++) {
          if (i < bh && j < bw ) {
          y[i][j] = b[i][j];
          }
          else {
          y[i][j] = 0;
          }
        }
        }
      int temp[][][] = new int[2][p][q];
      temp[0] = x;
      temp[1] = y;
      return temp;
    }
    
    /*
     * filtering in frequency domain
     */
    public int[] filter2d_freq(int src[], int filter[][]) {
      ColorModel colorModel = ColorModel.getRGBdefault();
      int rgb[][] = new int[h][w];
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        rgb[i][j] = colorModel.getRed(src[i * w + j]);
      }
      }
      int kk[][][] = padding(rgb, filter);
      int a[][] = kk[0];
      int b[][] = kk[1];
      int ab_h = a.length;
      int ab_w = a[0].length;
      int buff1[] = new int[ab_h * ab_w];
      int buff2[] = new int[ab_h * ab_w];
      for (int i = 0; i < ab_h; i++) {
        for (int j = 0; j < ab_w; j++) {
          int x = a[i][j];
          int y = b[i][j];
          buff1[i * ab_w + j] = (255 << 24) | (x << 16) | (x << 8) | x;
          buff2[i * ab_w + j] = (255 << 24) | (y << 16) | (y << 8) | y;
        }
      }
      int buffImg1[] = dft2d(buff1, 0, new size(ab_w, ab_h));
      int buffImg2[] = dft2d(buff2, 0, new size(ab_w, ab_h));
      int rgb1[][] = new int[ab_h][ab_w];
      int rgb2[][] = new int[ab_h][ab_w];
      for (int i = 0; i < ab_h; i++) {
        for (int j = 0; j < ab_w; j++) {
          rgb1[i][j] = colorModel.getRed(buffImg1[i * ab_w + j]);
        }
      }
      for (int i = 0; i < ab_h; i++) {
        for (int j = 0; j < ab_w; j++) {
          rgb2[i][j] = colorModel.getRed(buffImg2[i * ab_w + j]);
        }
      }
      
      int target[][] = new int[ab_h][ab_w];
      for (int i = 0; i < ab_h; i++) {
        for (int j = 0; j < ab_w; j++) {
          target[i][j] = rgb1[i][j] * rgb2[i][j];
        }
      }
      int vin[] = new int[ab_h * ab_w];
      for (int i = 0; i < ab_h; i++) {
        for (int j = 0; j < ab_w; j++) {
          int x = target[i][j];
          vin[i * ab_w + j] = (255 << 24) | (x << 16) | (x << 8) | x;
        }
      }
      int tarImg[] = dft2d(vin, 1, new size(ab_w, ab_h));
      int myrgb[][] = new int[ab_h][ab_w];
      for (int i = 0; i < ab_h; i++) {
        for (int j = 0; j < ab_w; j++) {
          myrgb[i][j] = colorModel.getRed(tarImg[i * ab_w + j]);
        }
      }
      int fin[] = new int[h * w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          int t = myrgb[i][j];
          fin[i * w + j] = (255 << 24) | (t << 16) | (t << 8) | t;
        }
      }
      return fin;
    }
    
    /*********************************Homework 4**********************************************/
    /*********************************Homework 4**********************************************/
    private int Multiple_1(int v[][], int x[][], int k) {
        double t = 0.0;
        int hh = x.length;
        int ww = x[0].length;
        for (int i = 0; i < hh; i++) {
          for (int j = 0; j < ww; j++) {
          double temp = (double)1 / (v[i][j] * x[i][j]);
            t += temp;
          }
        }
        int result = (int) (k / t);
        return result;
      }
    
    public void filter2d_1(int src[], int filter[][]) {
        int f_h = filter.length;
        int f_w = filter[0].length;
        int mul = f_h * f_w;
        ColorModel colorModel = ColorModel.getRGBdefault();
       
        int half1 = f_h / 2;
        int half2 = f_w / 2;
        int kk[][][] = view_as_window_2(src, new size(f_w, f_h));
        
        int temp[][] = new int[h][w];
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            temp[i][j] = colorModel.getRed(src[i * w + j]);
          }
        }
        
        int k = (w - f_w + 1) * (h - f_h + 1);
        int m = half1, n = half2;
        for (int i = 0;i < k; i++) {
          if ((i + 1) % (w - f_w + 1) == 0) {
          temp[m][n] = Multiple_1(kk[i], filter, mul);
            m ++;
            n = half2;
          }
          else {
            temp[m][n] = Multiple_1(kk[i], filter, mul);
            n ++;
          }
        }
        
        JFrame bf = new JFrame("Filtering");
        bf.setSize(w + 50, h + 50);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        int data[] = new int[h * w];
        for (int x = 0; x < h; x++) {
          for (int y = 0; y < w; y++) {
            int rgb = temp[x][y];
            data[x * w + y] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
          }
        }
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();

        bf.setVisible(true);
    }
    
    private int Multiple_2(int v[][], int x[][], double Q) {
      int hh = x.length;
      int ww = x[0].length;
      int g[][] = new int[hh][ww];
      for (int i = 0; i < hh; i++) {
      for (int j = 0; j < ww; j++) {
        g[i][j] = v[i][j] * x[i][j];
      }
      }
      double result1 = 0.0, result2 = 0.0;
      for (int i = 0; i < hh; i++) {
      for (int j = 0; j < ww; j++) {
        result1 += Math.pow(g[i][j], Q + 1);
        result2 += Math.pow(g[i][j], Q);
      }
      }
      int result = (int)(result1 / result2);
      return result;
    }
    
    public void filter2d_2(int src[], int filter[][], double Q) {
        int f_h = filter.length;
        int f_w = filter[0].length;
        ColorModel colorModel = ColorModel.getRGBdefault();
       
        int half1 = f_h / 2;
        int half2 = f_w / 2;
        int kk[][][] = view_as_window_2(src, new size(f_w, f_h));
        
        int temp[][] = new int[h][w];
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            temp[i][j] = colorModel.getRed(src[i * w + j]);
          }
        }
        
        int k = (w - f_w + 1) * (h - f_h + 1);
        int m = half1, n = half2;
        for (int i = 0;i < k; i++) {
          if ((i + 1) % (w - f_w + 1) == 0) {
          temp[m][n] = Multiple_2(kk[i], filter, Q);
            m ++;
            n = half2;
          }
          else {
            temp[m][n] = Multiple_2(kk[i], filter, Q);
            n ++;
          }
        }
        
        JFrame bf = new JFrame("Filtering");
        bf.setSize(w + 50, h + 50);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        int data[] = new int[h * w];
        for (int x = 0; x < h; x++) {
          for (int y = 0; y < w; y++) {
            int rgb = temp[x][y];
            data[x * w + y] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
          }
        }
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();

        bf.setVisible(true);
      }
    
    private int Multiple_3(int v[][], double power) {
      double temp[] = new double[v.length * v[0].length];
      int count = 0;
      for (int i = 0; i < v.length; i++) {
      for (int j = 0; j < v[0].length; j++) {
        temp[count] = Math.pow(v[i][j], power);
        count ++;
      }
      }
      double buff = 1.0;
      for (int i = 0; i < count; i++) {
      buff *= temp[i];
      }
      int result = (int)buff;
      return result;
    }
    
    public void filter2d_3(int src[], int filter[][]) {
      int f_h = filter.length;
      int f_w = filter[0].length;
      double power = (double)1 / (f_h * f_w);
      ColorModel colorModel = ColorModel.getRGBdefault();
       
      int half1 = f_h / 2;
      int half2 = f_w / 2;
      int kk[][][] = view_as_window_2(src, new size(f_w, f_h));
        
      int temp[][] = new int[h][w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          temp[i][j] = colorModel.getRed(src[i * w + j]);
        }
      }
        
      int k = (w - f_w + 1) * (h - f_h + 1);
      int m = half1, n = half2;
      for (int i = 0;i < k; i++) {
        if ((i + 1) % (w - f_w + 1) == 0) {
          temp[m][n] = Multiple_3(kk[i], power);
          m ++;
          n = half2;
        }
        else {
          temp[m][n] = Multiple_3(kk[i], power);
          n ++;
        }
      }
        
      JFrame bf = new JFrame("Filtering");
      bf.setSize(w + 50, h + 50);
      JLabel Label = new JLabel("");
      JScrollPane pane1 = new JScrollPane(Label);
      bf.add(pane1, BorderLayout.CENTER);
      int data[] = new int[h * w];
      for (int x = 0; x < h; x++) {
        for (int y = 0; y < w; y++) {
          int rgb = temp[x][y];
          data[x * w + y] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
        }
      }
      Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
      ImageIcon ii = new ImageIcon(buff);
      Label.setIcon(ii);
      Label.repaint();

      bf.setVisible(true);
    }
    
    public int[] addGaussianNoise(int src[], double mean, double sv) {
      double p[] = new double[256];
      int noise[] = new int[256];
      int rgb[][] = new int[h][w];
      ColorModel colorModel = ColorModel.getRGBdefault();
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        rgb[i][j] = colorModel.getRed(src[i * w + j]);
      }
      }
    
      double first = 1 / (Math.sqrt(2 * Math.PI) * sv);
      for (int i = 0; i <= 255; i++) {
      double temp = -((i - mean) * (i - mean)) / (2 * sv * sv);
      double second = Math.exp(temp);
      p[i] = first * second;
      noise[i] = (int)(p[i] * h * w);
      }
      /*
       * array location is used to store the pixels that do not add noise
       * equals 1 means that it has noise on it
       */
      int location[][] = new int[h][w];
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        location[i][j] = 0;
      }
      }
      /*
       * add Gaussian noise to the image
       */
      Random vv = new Random();
      int count = 0;
      for (int j = 0; j <= 255; j++) {
        for (int i = 0; i < h * w; i++) {
          if (count == noise[j]) {
            count = 0;
            break;
          }
          int r = vv.nextInt(w);
          int c = vv.nextInt(h);
          if (location[c][r] != 1) {
            rgb[c][r] += j;
            //rgb[c][r] = (rgb[c][r] > 255) ? 255 : rgb[c][r];
            location[c][r] = 1;
            count ++;
          }
        }
      }
      
      int max = rgb[0][0];
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        max = rgb[i][j] > max ? rgb[i][j] : max;
      }
      }
      double rate = (double)255 / max;
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          rgb[i][j] = (int)(rgb[i][j] * rate);
        }
      }
      
      int data[] = new int[h * w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          data[i * w + j] = (255 << 24) | (rgb[i][j] << 16) | (rgb[i][j] << 8) | rgb[i][j];
        }
      }
      return data;
    }
   
    public int[] addSaltandPepperNoise(int src[], double sp, double pp) {
      int rgb[][] = new int[h][w];
      ColorModel colorModel = ColorModel.getRGBdefault();
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          rgb[i][j] = colorModel.getRed(src[i * w + j]);
        }
      }
      Random vv = new Random();
      int size = (int)(h * w * 0.3);
      /* the number of pixels have salt noise */
      int salt = (int)(size * sp);
      /* the number of pixels have pepper noise */
      int pepper = (int)(size * pp);
      /*
       * array location is used to store the pixels that do not add noise
       * equals 1 means that it has noise on it
       */
      int location[][] = new int[h][w];
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        location[i][j] = 0;
      }
      }
      
      int m = 0;
      int n = 0;
      /*
       * add salt noise
       */
      for (int i = 0; i < h * w; i++) {
      if (m == salt) {
        break;
      }
      int r = vv.nextInt(w);
      int c = vv.nextInt(h);
      if (location[c][r] != 1) {
        rgb[c][r] = 255;
        location[c][r] = 1;
        m ++;
      }
      }
      /*
       * add pepper noise
       */
      for (int i = 0; i < h * w; i++) {
      if (n == pepper) {
        break;
      }
        int r = vv.nextInt(w);
        int c = vv.nextInt(h);
        if (location[c][r] != 1) {
          rgb[c][r] = 0;
          location[c][r] = 1;
          n ++;
        }
      }
      
      int data[] = new int[h * w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          data[i * w + j] = (255 << 24) | (rgb[i][j] << 16) | (rgb[i][j] << 8) | rgb[i][j];
        }
      }
      return data;
    }
    
    /*
     * return the min value in the specific array
     */
    private int minInArr(int v[][]) {
      int col = v.length;
      int row = v[0].length;
      int min = v[0][0];
      for (int i = 0; i < col; i++) {
      for (int j = 0; j < row; j++) {
        min = v[i][j] < min ? v[i][j] : min;
      }
      }
      return min;
    }
    
    /*
     * return the max value in the specific array
     */
    private int maxInArr(int v[][]) {
      int col = v.length;
      int row = v[0].length;
      int max = v[0][0];
      for (int i = 0; i < col; i++) {
        for (int j = 0; j < row; j++) {
          max = v[i][j] > max ? v[i][j] : max;
        }
      }
      return max;
    }
    
    /*
     * return the median value in the specific array
     */
    private int medianInArr(int v[][]) {
      int col = v.length;
      int row = v[0].length;
      int median;
      int temp[] = new int[col * row];
      for (int i = 0; i < col; i++) {
        for (int j = 0; j < row; j++) {
          temp[i * row + j] = v[i][j];
        }
      }
      
      /* bubble sort */
      for(int i = 0; i < col * row - 1; i++) {
      for (int j = 0; j < col * row - 1 - i; j++) {
        if (temp[j] > temp[j + 1]) {
          int t = temp[j];
          temp[j] = temp[j + 1];
          temp[j + 1] = t;
        }
      }
      }
      if ((col * row) % 2 != 0) {
      median = temp[col * row / 2];
      }
      else {
      median = (int)(0.5 * (temp[col * row / 2] + temp[(col * row - 1) / 2]));
      }
      return median;
    }
    
    public void statisticalFilter(int src[], int con) {
        int f_h = 3;
        int f_w = 3;
        
        ColorModel colorModel = ColorModel.getRGBdefault();
        int temp[][] = new int[h][w];
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            temp[i][j] = colorModel.getRed(src[i * w + j]);
          }
        }
       
        int half1 = f_h / 2;
        int half2 = f_w / 2;
        int kk[][][] = view_as_window_2(src, new size(f_w, f_h));
        
        if (con == 0) {
          int k = (w - f_w + 1) * (h - f_h + 1);
          int m = half1, n = half2;
          for (int i = 0;i < k; i++) {
            if ((i + 1) % (w - f_w + 1) == 0) {
            temp[m][n] = minInArr(kk[i]);
              m ++;
              n = half2;
            }
            else {
              temp[m][n] = minInArr(kk[i]);
              n ++;
            }
          }
        }
        if (con == 1) {
          int k = (w - f_w + 1) * (h - f_h + 1);
          int m = half1, n = half2;
          for (int i = 0;i < k; i++) {
            if ((i + 1) % (w - f_w + 1) == 0) {
              temp[m][n] = maxInArr(kk[i]);
              m ++;
              n = half2;
            }
            else {
              temp[m][n] = maxInArr(kk[i]);
              n ++;
            }
          }
        }
        if (con == 2) {
          int k = (w - f_w + 1) * (h - f_h + 1);
          int m = half1, n = half2;
          for (int i = 0;i < k; i++) {
            if ((i + 1) % (w - f_w + 1) == 0) {
              temp[m][n] = medianInArr(kk[i]);
              m ++;
              n = half2;
            }
            else {
              temp[m][n] = medianInArr(kk[i]);
              n ++;
            }
          }
        }
        
        JFrame bf = new JFrame("Filtering");
        bf.setSize(w + 50, h + 50);
        JLabel Label = new JLabel("");
        JScrollPane pane1 = new JScrollPane(Label);
        bf.add(pane1, BorderLayout.CENTER);
        int data[] = new int[h * w];
        for (int x = 0; x < h; x++) {
          for (int y = 0; y < w; y++) {
            int rgb = temp[x][y];
            data[x * w + y] = (255 << 24) | (rgb << 16) | (rgb << 8) | rgb;
          }
        }
        Image buff = createImage(new MemoryImageSource(w, h, data, 0, w));
        ImageIcon ii = new ImageIcon(buff);
        Label.setIcon(ii);
        Label.repaint();

        bf.setVisible(true);
    }
    
    public int[] equalize_2(int[] srcPixArray) {
      ColorModel colorModel = ColorModel.getRGBdefault();
      
      int rtemp[] = new int[h * w];
      int gtemp[] = new int[h * w];
      int btemp[] = new int[h * w];
      int temp[] = new int[h * w];
      
        int data[] = new int[256];
        double p[] = new double[256];
        double c[] = new double[256];
        
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            rtemp[i * w + j] = colorModel.getRed(srcPixArray[i * w + j]);
            gtemp[i * w + j] = colorModel.getGreen(srcPixArray[i * w + j]);
            btemp[i * w + j] = colorModel.getBlue(srcPixArray[i * w + j]);
            temp[i * w + j] = (int)((double)1.0 / 3 * (rtemp[i * w + j] + gtemp[i * w + j] + btemp[i * w + j]));
          }
        }
        for (int i = 0; i <= 255; i++) {
          data[i] = 0;
          c[i] = 0.0;
        }
        for (int i = 0; i < w * h; i++) {
          for (int j = 0; j <= 255; j++) {
            if (temp[i] == j) {
              data[j] ++;
              break;
            }
          }
        }
        
        /*for (int i = 0; i < w * h; i++) {
          for (int j = 0; j <= 255; j++) {
            if (rtemp[i] == j) {
              data[j] ++;
              break;
            }
          }
        }
        for (int i = 0; i < w * h; i++) {
          for (int j = 0; j <= 255; j++) {
            if (gtemp[i] == j) {
              data[j] ++;
              break;
            }
          }
        }
        for (int i = 0; i < w * h; i++) {
          for (int j = 0; j <= 255; j++) {
            if (btemp[i] == j) {
              data[j] ++;
              break;
            }
          }
        }*/
        
        for (int i = 0; i <= 255; i++) {
          p[i] = (double)data[i] / (w * h);
        }
        
        for (int i = 0; i <= 255; i++) {
          for (int j = 0; j <= i; j++) {
            c[i] += p[j];
          }
        }
        
        int t[] = new int[w * h];
        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
          int r = (int)(c[rtemp[i * w + j]] * (graylevel - 1));
          int g = (int)(c[gtemp[i * w + j]] * (graylevel - 1));
          int b = (int)(c[btemp[i * w + j]] * (graylevel - 1));
            t[i * w + j] = 255 << 24 | r << 16 | g << 8 | b;
          }
        }
        return t;
    }
    /*********************************Homework 4**********************************************/
    /*********************************Homework 4**********************************************/
    
    public int[] reduceIntensity(int src[], double rate) {
      ColorModel colorModel = ColorModel.getRGBdefault();
      int data[] = new int[h * w];
      for (int i = 0;i < h * w; i++) {
      int r = (int)(colorModel.getRed(src[i]) * (1.0 - rate));
      int g = (int)(colorModel.getGreen(src[i]) * (1.0 - rate));
      int b = (int)(colorModel.getBlue(src[i]) * (1.0 - rate));
      data[i] = (255 << 24) | (r << 16) | (g << 8) | b;
      }
      return data;
    }
    
    /*************************************Final Project***************************************/
    /*************************************Final Project***************************************/
    private int[][][] viewAsWindow(int src[], size patch) {
        int k = (w - patch._w + 1) * (h - patch._h + 1);
        int temp[][][] = new int[k][patch._h][patch._w];
         
        for (int i = 0; i < h - patch._h + 1; i++) {
          for (int j = 0; j < w - patch._w + 1; j++) {
            int p = i * (w - patch._w + 1) + j;
            for (int s = i; s < patch._h + i; s++) {
              for (int t = j; t < patch._w + j; t++) {
                temp[p][s - i][t - j] = src[s * w + t];
              }
            }
          }
        }
        return temp;
    }
    
    private int min(int a, int b, int c) {
      int result;
      result = b < a ? b : a;
      result = c < result ? c : result;
      return result;
    }
    
    private int darkMin(int r[][], int g[][], int b[][]) {
      int rmin = minInArr(r);
      int gmin = minInArr(g);
      int bmin = minInArr(b);
      int min = rmin < gmin ? rmin : gmin;
      min = bmin < min ? bmin : min;
      return min;
    }
    
    private int[] getDarkChannel(int src[]) {
      ColorModel colorModel = ColorModel.getRGBdefault();
      int r[] = new int[h * w];
      int g[] = new int[h * w];
      int b[] = new int[h * w];
      for (int i = 0; i < h * w; i++) {
        r[i] = colorModel.getRed(src[i]);
        g[i] = colorModel.getGreen(src[i]);
        b[i] = colorModel.getBlue(src[i]);
      }
      
      int temp[][] = new int[h][w];
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
          if (i < 7 || i > h - 1 - 7 || j < 7 || j > w - 1 - 7) {
          temp[i][j] = min(r[i * w + j], g[i * w + j], b[i * w + j]);
          }
          else {
          temp[i][j] = 255;
          }
      }
      }
      
      int rpatch[][][] = viewAsWindow(r, new size(15, 15));
      int gpatch[][][] = viewAsWindow(g, new size(15, 15));
      int bpatch[][][] = viewAsWindow(b, new size(15, 15));
      int total = (h - 15 + 1) * (w - 15 + 1);
      int half1 = 7;
      int half2 = 7;
      int m = half1, n = half2;
      for (int i = 0;i < total; i++) {
        if ((i + 1) % (w - 15 + 1) == 0) {
          temp[m][n] = darkMin(rpatch[i], gpatch[i], bpatch[i]);
          m ++;
          n = half2;
        }
        else {
          temp[m][n] = darkMin(rpatch[i], gpatch[i], bpatch[i]);
          n ++;
        }
      }
      int data[] = new int[h * w];
      for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        data[i * w + j] = temp[i][j];
      }
      }
      return data;
    }
    
    /*private int[][] getAtmosphericLight() {
      int dark[] = getDarkChannel(currentPixArray);
      for (int i = 0; i < dark.length - 1; i++) {
      for (int j = 0; j < dark.length - 1 - i; j++) {
        if (dark[i] < dark[i + 1]) {
        int temp = dark[i];
        dark[i] = dark[i + 1];
        dark[i + 1] = temp;
        }
      }
      }
      int topK = (int)(dark.length * 0.1);
      int top[] = new int[topK];
      for (int i = 0; i < topK; i++) {
        top[i] = dark[i]; 
      }
      
    }*/
    
    public int[] dehaze(int src[]) {
      ColorModel colorModel = ColorModel.getRGBdefault();
      int r[] = new int[h * w];
      int g[] = new int[h * w];
      int b[] = new int[h * w];
      for (int i = 0; i < h * w; i++) {
      r[i] = colorModel.getRed(src[i]);
      g[i] = colorModel.getGreen(src[i]);
      b[i] = colorModel.getBlue(src[i]);
      }
      
      return null;
    }
    /*************************************Final Project***************************************/
    /*************************************Final Project***************************************/
    
    /**
     * @param args
     */
    public static void main(String[] args) {
      new ImageProcessing("Image Processor");
    }

}