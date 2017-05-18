/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package formPerpus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import opennlp.tools.stemmer.Stemmer;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import perpustakaan.freqDokumen;

/**
 *
 * @author ASUS
 */
public class menuUtama extends javax.swing.JFrame {

    /**
     * Creates new form menuUtama
     */
    public String files;
    public int byk;
    private File tfolder, tmp[];
    public Vector nama = new Vector();
    public Vector isiDokumen = new Vector();
    Map<String, Set<freqDokumen>> pemetaanSumber = new HashMap<>();

    public void kosongkanTable() {
        try {
            String kosong = "TRUNCATE TABLE tb_kopus";
            java.sql.Connection conn = (java.sql.Connection) koneksi_database.koneksi.koneksiDB();
            java.sql.PreparedStatement pst = conn.prepareStatement(kosong);
            pst.execute();
        } catch (Exception e) {
        }
    }

    public void tambahSumber() {
        try {
            Vector tmp = new Vector();
            cekSumber(tmp);
            for (int i = 0; i < jListData.getModel().getSize(); i++) {
                if (tmp.size() != 0) {
                    for (int j = 0; j < tmp.size(); j++) {
                        if (tmp.get(j) != jListData.getModel().getElementAt(i)) {
                            int b = 0;
                            String sql = "insert into tb_sumber values('" + b + "','" + nama.get(i).toString() + "','" + jListData.getModel().getElementAt(i) + "')";
                            java.sql.Connection conn = (java.sql.Connection) koneksi_database.koneksi.koneksiDB();
                            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                            pst.execute();
                        }
                    }
                } else {
                    int b = 0;
                    String sql = "insert into tb_sumber values('" + b + "','" + nama.get(i).toString() + "','" + jListData.getModel().getElementAt(i) + "')";
                    java.sql.Connection conn = (java.sql.Connection) koneksi_database.koneksi.koneksiDB();
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.execute();
                }

            }
        } catch (Exception e) {
        }
    }

    public void cekSumber(Vector d) {
        try {
            Statement st = koneksi_database.koneksi.koneksiDB().createStatement();
            String NamaSumber = "select url from tb_sumber";
            ResultSet res = st.executeQuery(NamaSumber);
            while (res.next()) {
                //System.out.println(res.getString("url"));
                d.add(res.getString("url"));
            }
            st.close();
            res.close();
        } catch (Exception e) {
        }
    }

    public void PlainText() {
        try {
            Statement state = koneksi_database.koneksi.koneksiDB().createStatement();
            String banyakKolom = "select url from tb_sumber";
            ResultSet res = state.executeQuery(banyakKolom);
            InputStream is;
            while (res.next()) {
                //System.out.print(res.getString("url"));
                try {
                    is = new FileInputStream(res.getString("url"));
                    BodyContentHandler ch = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    Parser parser = new AutoDetectParser();
                    parser.parse(is, ch, metadata, new ParseContext());
                    //System.out.println(ch.toString());
                    isiDokumen.add(ch.toString());
                } catch (Exception e) {
                }
            }
            state.close();
            res.close();
        } catch (Exception e) {
        }
    }

    public void tokenKata() {
        PlainText();
        for (int a = 0; a < isiDokumen.size(); a++) {
            StringTokenizer st = new StringTokenizer(isiDokumen.get(a).toString().toLowerCase(), "\t\n:;,(). ");
            freqDokumen dokumen = new freqDokumen("D" + (a + 1));
            while (st.hasMoreTokens()) {
                String kata = st.nextToken();
                dokumen.put(kata);
                Set<freqDokumen> dok = pemetaanSumber.get(kata);
                if (dok == null) {
                    dok = new HashSet<>();
                    pemetaanSumber.put(kata, dok);
                }
                dok.add(dokumen);
            }
        }
        StringBuilder build = new StringBuilder();
        for (String kata : pemetaanSumber.keySet()) {
            Set<freqDokumen> dok = pemetaanSumber.get(kata);
            build.append(kata + "\t");
            for (freqDokumen dokumen : dok) {
                build.append(dokumen.getNamaDokumen() + ":" + dokumen.getBanyak(kata));
                build.append(", ");
            }
            build.delete(build.length() - 2, build.length() - 1);
            build.append("\n");
        }
        //System.out.println(build);
        Vector tampung = new Vector();
        tampung.removeAllElements();
        String[] tmp = build.toString().split("\n");
        for (String a : tmp) {
            String[] t = a.split("\t");
            for (String b : t) {
                tampung.add(b);
            }
        }

        for (int i = 0; i < tampung.size(); i++) {
            if (i % 2 == 0) {
                try {
                    String sql = "insert into tb_kopus values('" + 0 + "','" + tampung.get(i) + "','" + tampung.get(i + 1) + "')";
                    java.sql.Connection con = (java.sql.Connection) koneksi_database.koneksi.koneksiDB();
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.execute();
                } catch (Exception e) {
                }
            }
        }
    }

    public menuUtama() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jThasil = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListData = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Search");

        jThasil.setColumns(20);
        jThasil.setRows(5);
        jScrollPane1.setViewportView(jThasil);

        jScrollPane2.setViewportView(jListData);

        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        jMenu1.setText("File");

        jMenuItem1.setText("Tambah 1 File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Tambah File 1 Folder");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("About");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        JFileChooser load_file = new JFileChooser();
        int jes = load_file.showOpenDialog(this);
        if (jes == JFileChooser.APPROVE_OPTION) {
            File f = load_file.getSelectedFile();
            nama.add(load_file.getName(f));
            String sumber = f.getPath().replace("\\", "/");
            DefaultListModel dlm = new DefaultListModel();
            dlm.addElement(sumber);
            jListData.setModel(dlm);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Pilih Directori");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            tfolder = chooser.getSelectedFile();
            String sumber = tfolder.toString().replace("\\", "/");
            tmp = tfolder.listFiles();
            DefaultListModel dlm = new DefaultListModel();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isFile()) {
                    dlm.addElement(sumber + "/" + tmp[i].getName());
                    jListData.setModel(dlm);
                    nama.add(tmp[i].getName());
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Tidak Ada Folder Yang Dipilih");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            tambahSumber();
            kosongkanTable();
            tokenKata();
            //System.out.print(jListData.getModel().getSize());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuUtama().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListData;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextArea jThasil;
    // End of variables declaration//GEN-END:variables
}
