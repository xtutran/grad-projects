package gui;

import evaluation.Evaluator;
import features.HSV166Histogram.DistanceFunction;
import org.apache.commons.io.FilenameUtils;
import search.ImageIndexing;
import search.ImageSearcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author Le Hoang
 */
public class MainFrame extends javax.swing.JFrame {

    public static String IMAGE_DB = "features.db";
    public final String[] DistanceMethodNames = {"Histogram Euclidean Distance (HED)",
            "Histogram Quadratic Distance Measures (HQDM)", "Integrated Histogram Bin Matching (IHBM)"};
    // files fields
    String inputFileName = "";
    File inputFile = null;
    boolean fileIsOK = false;
    BufferedImage targetImage;
    // folder fields
    String indexingFolderName = "";
    File indexingFolder = null;
    // result fields
    BufferedImage resultImage;
    // Evaluator
    Evaluator evaluator;
    DistanceFunction distanceMethod = DistanceFunction.HED;
    int kForNearestNeibor = 5;
    // additional fields
    private javax.swing.JPanel selectFolderPanel;
    private javax.swing.JButton folderSelect;
    private javax.swing.JButton calculateFeatures;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel featuresFile;
    private javax.swing.JLabel evaluationFile;
    private javax.swing.JTextField folderNameTextField;
    // addition fields
    private javax.swing.JLabel precisionContent;
    private javax.swing.JLabel precisionLabel;
    private javax.swing.JLabel recallContent;
    private javax.swing.JLabel recallLabel;
    private javax.swing.JLabel timeContent;
    // Variables declaration - do not modify
    private javax.swing.JLabel timeLabe;
    private javax.swing.JPanel statisticsPanel;
    private javax.swing.JButton browseInputImage;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox distanceMethodSelector;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel footerInfo;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel searchStatus;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel resultPanel;
    private ImagePanel inputImagePanel;
    private javax.swing.JPanel jPanel6;
    private ImagePanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable resultSet;
    private javax.swing.JTextField inputImageAbsolutePath;
    private ImageIndexing ii = ImageIndexing.getInstance(IMAGE_DB);
    private ImageSearcher imageSearcher = ImageSearcher.getInstance();

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    public static void main(String args[]) {
        /*
		 * Set the Nimbus look and feel
		 */
        // <editor-fold defaultstate="collapsed"
        // desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
        // indexing

        // Indexing();
        // int[] hsv = new int[3];
        // HSV166Histogram.rgb2hsv(196, 196, 0, hsv);
        // HSV166Histogram.quant(hsv);
        // for (int i = 0; i < 3; i++) {
        // System.out.println(hsv[i]);
        // }
        // HashSet<Integer> hs = new HashSet<>();
        // hs.add(new Integer(1));
        // if ( hs.contains(new Integer(1))) {
        // System.out.println("That's true");
        // } else {
        // System.out.println("That's false");
        // }
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

		/*
		 * Create and display the form
		 */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    /**
     * @param path the command line arguments
     */
    public void Indexing(String path) {
        ii.createImageDb(path, false);
        ii.indexImages();
    }

    public void initSelectFolderPanel() {
        // jPanel 0 code added
        folderNameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        featuresFile = new javax.swing.JLabel();
        folderSelect = new javax.swing.JButton();
        calculateFeatures = new javax.swing.JButton();
        selectFolderPanel = new javax.swing.JPanel();
        evaluationFile = new javax.swing.JLabel();

        // featuresFile.setText("Features database: FeaturesDatabase.txt");
        // evaluationFile.setText("Evaluation: Evaluation.txt");
        featuresFile.setForeground(Color.RED);
        evaluationFile.setForeground(Color.RED);
        jLabel3.setText("Image Folder");
        folderSelect.setText("Browse");

        folderSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                folderSelectActionPerformed(evt);
            }
        });
        calculateFeatures.setText("Recalculate Features");
        calculateFeatures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateFeaturesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout selectFolderPanelLayout = new javax.swing.GroupLayout(selectFolderPanel);
        selectFolderPanel.setLayout(selectFolderPanelLayout);
        selectFolderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Calculate features"));
        selectFolderPanelLayout.setHorizontalGroup(selectFolderPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(selectFolderPanelLayout.createSequentialGroup().addGroup(selectFolderPanelLayout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(selectFolderPanelLayout.createSequentialGroup().addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(folderNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 240,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14,
                                        Short.MAX_VALUE)
                                .addComponent(folderSelect))
                        .addGroup(selectFolderPanelLayout.createSequentialGroup()
                                .addComponent(featuresFile, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60).addComponent(calculateFeatures, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(selectFolderPanelLayout.createSequentialGroup().addComponent(evaluationFile,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap()));
        selectFolderPanelLayout.setVerticalGroup(selectFolderPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(selectFolderPanelLayout.createSequentialGroup().addContainerGap()
                        .addGroup(selectFolderPanelLayout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(folderNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(folderSelect))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                selectFolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(calculateFeatures)
                                        .addGroup(selectFolderPanelLayout.createSequentialGroup()
                                                .addComponent(featuresFile, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(evaluationFile, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(7, 7, 7)));
    }

    public void initStatisticsPanel() {
        timeLabe = new javax.swing.JLabel();
        precisionLabel = new javax.swing.JLabel();
        recallLabel = new javax.swing.JLabel();
        timeContent = new javax.swing.JLabel();
        precisionContent = new javax.swing.JLabel();
        recallContent = new javax.swing.JLabel();
        statisticsPanel = new JPanel();

        timeLabe.setText("Time");
        precisionLabel.setText("Precision");
        recallLabel.setText("Recall");
        timeContent.setText("");
        precisionContent.setText("");
        recallContent.setText("");

        javax.swing.GroupLayout statisTicslayout = new javax.swing.GroupLayout(statisticsPanel);
        statisticsPanel.setLayout(statisTicslayout);
        statisticsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Statistics"));
        statisTicslayout.setHorizontalGroup(statisTicslayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statisTicslayout.createSequentialGroup().addContainerGap().addGroup(statisTicslayout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(timeLabe)
                        .addGroup(statisTicslayout.createSequentialGroup().addGap(10, 10, 10).addComponent(timeContent,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addGroup(statisTicslayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(precisionLabel)
                                .addGroup(statisTicslayout.createSequentialGroup().addGap(10, 10, 10).addComponent(
                                        precisionContent, javax.swing.GroupLayout.PREFERRED_SIZE, 47,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(statisTicslayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(statisTicslayout.createSequentialGroup().addGap(10, 10, 10).addComponent(
                                        recallContent, javax.swing.GroupLayout.PREFERRED_SIZE, 47,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(recallLabel))
                        .addGap(27, 27, 27)));
        statisTicslayout.setVerticalGroup(statisTicslayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statisTicslayout.createSequentialGroup().addContainerGap()
                        .addGroup(statisTicslayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(timeLabe).addComponent(precisionLabel).addComponent(recallLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(statisTicslayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(timeContent).addComponent(precisionContent).addComponent(recallContent))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    public void initInputPanel() {
        inputPanel = new javax.swing.JPanel();
        inputImagePanel = new ImagePanel();
        jLabel1 = new javax.swing.JLabel();
        inputImageAbsolutePath = new javax.swing.JTextField();
        browseInputImage = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));
        inputImagePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(inputImagePanel);
        inputImagePanel.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 125, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE));

        jLabel1.setText("Image to search");

        inputImageAbsolutePath.setMaximumSize(new Dimension(200, 2000));
        inputImageAbsolutePath.setEditable(false);

        browseInputImage.setText("Browse");
        browseInputImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseInputImageActionPerformed(evt);
            }
        });

        jLabel5.setText("  ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout
                .setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup().addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                                                jPanel6Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addComponent(browseInputImage,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 82,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        34, Short.MAX_VALUE))
                                                        .addComponent(inputImageAbsolutePath,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 200,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(jLabel5)
                        .addGap(27, 27, 27)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1).addComponent(inputImageAbsolutePath,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(browseInputImage))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
                        .addComponent(inputImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(inputImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE));
    }

    public void initSearchPanel() {
        searchPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        distanceMethodSelector = new javax.swing.JComboBox();
        searchButton = new javax.swing.JButton();
        searchStatus = new javax.swing.JLabel();

        searchButton.setEnabled(false);

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel2.setText("Distance measure method");

        distanceMethodSelector.setModel(new javax.swing.DefaultComboBoxModel(DistanceMethodNames));
        distanceMethodSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distanceMethodSelectorActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        searchStatus.setText("  ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(searchStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 333,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(
                                        jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(
                                                        jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(distanceMethodSelector, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))))
                        .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2).addComponent(distanceMethodSelector,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(searchButton, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
                                        searchStatus, javax.swing.GroupLayout.Alignment.TRAILING))));

    }

    public void initResultPanel() {
        resultPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultSet = new javax.swing.JTable();
        jPanel7 = new ImagePanel();

        resultPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));

        resultSet.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{{null, null}, {null, null}, {null, null}, {null, null}},
                new String[]{"FilePath", "Relevant"}));

        resultSet.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        resultSet.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
                resultSetSelectionPerformed(e);
            }

        });
        jScrollPane1.setViewportView(resultSet);

        jScrollPane2.setViewportView(jScrollPane1);

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 125, Short.MAX_VALUE));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 121, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup().addContainerGap(19, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                        Short.MAX_VALUE))));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        footerInfo = new javax.swing.JLabel();

        initSelectFolderPanel();
        initInputPanel();
        initStatisticsPanel();
        initResultPanel();
        initSearchPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        footerInfo.setText("Image Search v1.0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(selectFolderPanel, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(searchPanel, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(resultPanel, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(inputPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(statisticsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE).addComponent(footerInfo).addGap(25, 25, 25)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addComponent(selectFolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()
                        .addComponent(inputPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resultPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statisticsPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(footerInfo)));

        pack();
    }// </editor-fold>

    private void folderSelectActionPerformed(java.awt.event.ActionEvent evt) {
        // Browse button
        JFileChooser jf = new JFileChooser(".");
        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jf.showOpenDialog(this) != 0) {

            return;
        }
        indexingFolder = jf.getSelectedFile();
        indexingFolderName = indexingFolder.getAbsolutePath();
        folderNameTextField.setText(indexingFolderName);

        if (indexingFolderName != null) {
            // TODO: review
            // ImageIndexing.setPath(indexingFolderName);
            File features = new File(IMAGE_DB);
            File evaluation = new File(indexingFolderName + "\\Evaluation.txt");
            if (!features.exists()) {
                featuresFile.setForeground(Color.RED);
                searchButton.setEnabled(false);
            } else {
                featuresFile.setForeground(Color.BLUE);
                searchButton.setEnabled(true);
            }
            if (!evaluation.exists()) {
                evaluationFile.setForeground(Color.RED);
            } else {
                evaluationFile.setForeground(Color.BLUE);
                evaluator = new Evaluator(indexingFolderName);
            }
        }
    }

    private void calculateFeaturesActionPerformed(java.awt.event.ActionEvent evt) {
        // Recalculate features
        Indexing(indexingFolderName);
        if (!searchButton.isEnabled()) {
            featuresFile.setForeground(Color.BLUE);
            searchButton.setEnabled(true);
        }
    }

    private void browseInputImageActionPerformed(java.awt.event.ActionEvent evt) {
        // Browse button
        JFileChooser jf = new JFileChooser(".");
        jf.addChoosableFileFilter(new ImageFilter());

        jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jf.setMultiSelectionEnabled(false);
        if (jf.showOpenDialog(this) != 0) {
            return;
        }
        inputFile = jf.getSelectedFile();
        inputFileName = inputFile.getAbsolutePath();
        inputImageAbsolutePath.setText(inputFileName);
        if (inputFile == null) {

            jLabel5.setText("There is no selected file");
            return;
        }
        try {
            targetImage = ImageIO.read(inputFile);
            inputImagePanel.setImage(targetImage);
            inputImagePanel.paintComponents(inputImagePanel.getGraphics());
            jLabel5.setForeground(Color.BLUE);
            jLabel5.setText("Image is OK");
            fileIsOK = true;
            this.repaint();
        } catch (IOException ex) {
            jLabel5.setText("Image exception");
        }
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Search button
        searchStatus.setForeground(Color.RED);
        if (!fileIsOK) {
            searchStatus.setText("File to search is not available");
            return;
        }

        searchStatus.setForeground(Color.BLUE);
        searchStatus.setText("Searching...");
        repaint();
        Calendar rightNow = Calendar.getInstance();
        long begin = rightNow.getTimeInMillis();
        Object[][] results = new Object[0][];
        try {
            imageSearcher.calcSimilarity(inputFile, distanceMethod);
            results = imageSearcher.search(inputFile, distanceMethod, 3);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // String[] tempoResults = new String[results.length];
        //
        // for (int i = 0; i < results.length; i++) {
        // tempoResults[i] = results[i][0];
        // int lastSeparator = tempoResults[i].lastIndexOf('\\');
        // tempoResults[i] = tempoResults[i].substring(lastSeparator + 1);
        // }
        //
        // for (int i = 0; i < results.length; i++) {
        // results[i] = new String[2];
        // results[i][0] = tempoResults[i];
        //
        // if (evaluator == null) {
        // results[i][1] = "";
        // } else if (evaluator.evaluate(inputFileName, tempoResults[i])) {
        // results[i][1] = "YES";
        // } else {
        // results[i][1] = "NO";
        // }
        // }
        //
        // Pair<Float> precisionAndRecall = new Pair<Float>(0f, 0f);
        // if (evaluator == null) {
        // } else {
        // evaluator.calculatePrecisionAndRecall(inputFileName, tempoResults,
        // precisionAndRecall);
        // }
        //
        // DecimalFormat format = new DecimalFormat("0.000");
        // String numberInTrueFormat = format.format(precisionAndRecall.first);
        // precisionContent.setText(numberInTrueFormat);
        // numberInTrueFormat = format.format(precisionAndRecall.second);
        // recallContent.setText(numberInTrueFormat);
        //
        // searchStatus.setForeground(Color.GREEN);
        // searchStatus.setText("Completed");
        //
        // rightNow = Calendar.getInstance();
        // long end = rightNow.getTimeInMillis();
        //
        // long duration = end - begin;
        // System.out.println(duration);
        // duration /= 1000;
        // int h = 0, m = 0, s = 0;
        // h = (int) (duration / 3600);
        // m = (int) ((duration % 3600) / 60);
        // s = (int) ((duration % 3600) % 60);
        // StringBuilder sb = new StringBuilder();
        // if (h != 0) {
        // sb.append(h + " h ");
        // }
        // if (m != 0) {
        // sb.append(m + " m ");
        // }
        // sb.append(s + " s");
        //
        // timeContent.setText(sb.toString());
        String[] headers = {"FilePath", "Relevant"};
        DefaultTableModel model = new DefaultTableModel(results, headers);

        resultSet.setModel(model);
        repaint();
    }

    private void resultSetSelectionPerformed(ListSelectionEvent e) {
        int index = resultSet.getSelectedRow();
        if (index < 0) {
            return;
        }
        String s = (String) resultSet.getValueAt(index, 0);
        if (s != null) {
            try {
                resultImage = ImageIO.read(new File(s));
                jPanel7.setImage(resultImage);
                jPanel7.paintComponents(inputImagePanel.getGraphics());
                this.repaint();
            } catch (IOException ex) {
            }
        }
    }

    private void distanceMethodSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Select feature extraction
        switch (distanceMethodSelector.getSelectedIndex()) {
            case 0:
                distanceMethod = DistanceFunction.HED;
                break;
            case 1:
                distanceMethod = DistanceFunction.HQDM;
                break;
            case 2:
                distanceMethod = DistanceFunction.IHBM;
                break;
        }
    }
    // End of variables declaration
}

class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel() {

    }

    public void setImage(BufferedImage _image) {
        image = _image;
    }

    public void paintComponent(Graphics g) {
        if (image != null) {

            Image icon = image.getScaledInstance(130, 130, BufferedImage.SCALE_FAST);
            g.drawImage(icon, 0, 0, null); // see javadoc for more info on the
            // parameters
        }
    }
}

class ImageFilter extends FileFilter {

    // Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        // if (f.isDirectory()) {
        // return true;
        // }

        String extension = FilenameUtils.getExtension(f.getName());
        return extension.equals("tiff") || extension.equals("tif") || extension.equals("gif")
                || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png");
    }

    // The description of this filter
    public String getDescription() {
        return "Image Files";
    }
}
