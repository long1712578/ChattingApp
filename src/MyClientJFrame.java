
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ABC
 */
public class MyClientJFrame extends javax.swing.JFrame {

    String User, clientUser ="";
    private DataInputStream din;
    private DataOutputStream dout;
    private DefaultListModel dim=null;
    private Socket socket;
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    public final static String
       FILE_TO_RECEIVED = "D:\\"; 
    //public final static int FILE_SIZE = 6022386;
    private final int BUFFER_SIZE =16*1024; 
    /**
     * Creates new form MyClientJFrame
     */
    public MyClientJFrame() {
        initComponents();
    }

    public MyClientJFrame(String use, Socket s) {
        User = use;
        socket=s;
        try {
            initComponents();
            
            dim = new DefaultListModel();
            
            //dim.addElement("17ctt4");
            //System.out.println("UL12: "+dim.toString());
            //UL.setModel(dim);
            lbadmin.setText(use);
            //Chuyen doc du lieu
            //Luon du lieu đầu vào đầu ra
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            
            
           // is = s.getInputStream();
            new Read(socket).start();
           
            //System.out.println("oke2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Chuyen doc du lieu tu server
    //Nhan du lieu cua server trong do co file
    class Read extends Thread {
        Socket socket1;
        Read(Socket s){
            socket1=s;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    //Nhận (read) du lieu tu server gui ve
                    String m = din.readUTF();
                    
                    //Kiemr tra ca ki tu nay co trong du lieu server gui ve khong
                    //Kiem tra co phai tin nhan khong
                    //them danh sach
                    if (m.contains("message")) {
                        //Loai bo các kí tự không xử lý
                        m = m.substring(7);
                        dim.clear();
                        //Chuyen du lieu tu string sang tokenizer
                        //Tách chuỗi thành phân lớp phân cách bởi dấu phẩy
                        StringTokenizer st = new StringTokenizer(m,",");
                        //System.out.println(m);
                        while (st.hasMoreTokens()) {
                            String u = st.nextToken();
                            if (!User.equals(u)) {
                                dim.addElement(u);
                            } 
                        }
                        System.out.println("UL List: "+dim.toString());
                        UL.setModel(dim);
                    }
                    else {
                        if (m.contains("sendfile1")) {
                            m = m.substring(9);
                            StringTokenizer st = new StringTokenizer(m, ":");
                            String n = st.nextToken();
                            System.out.println("msg: " + n);
                            String filename = st.nextToken();
                            System.out.println("file..." + filename);
                            txt_Show.append(" " + m + "\n");
                            //Luu ten file vao ULFile
                            
                            
                            String path = FILE_TO_RECEIVED + filename;

                            fos = new FileOutputStream(path);
                         
                            //DataInputStream input = socket2.getInputStream();
                            InputStream is = socket1.getInputStream();
                            
                            //ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(MyClientJFrame.this, "Downloading file please wait...", input);
                            //BufferedInputStream bis = new BufferedInputStream(input);
                            byte[] bytes = new byte[BUFFER_SIZE];
                            
                            int count = is.read(bytes);
                            //System.out.println("count rev: " + count);
                            fos.write(bytes, 0, count);
                            fos.close();
                        } else {
                            txt_Show.append(" " + m + "\n");
                        }
                    }
                } catch (Exception e) {
                    break;
                }

            }
        }
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
        lbHA = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        bnPhone = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbHA2 = new javax.swing.JLabel();
        lbadmin = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtMsg = new javax.swing.JTextField();
        btnFile = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_Show = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        UL = new javax.swing.JList<>();
        btnXuat = new javax.swing.JButton();
        btnGroup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 0, 255));

        lbHA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chattingapp/photo/admin.png"))); // NOI18N

        lbName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbName.setText("User");

        bnPhone.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bnPhone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chattingapp/photo/phone.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbHA, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(bnPhone)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bnPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbHA, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 255));

        lbHA2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHA2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chattingapp/photo/male.png"))); // NOI18N

        lbadmin.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lbadmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbadmin.setText("admin");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbadmin, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbHA2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbHA2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbadmin, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chattingapp/photo/attachment.png"))); // NOI18N
        btnFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileActionPerformed(evt);
            }
        });

        btnSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chattingapp/photo/msg.png"))); // NOI18N
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtMsg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFile, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSend)
                    .addComponent(btnFile, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        txt_Show.setEditable(false);
        txt_Show.setColumns(20);
        txt_Show.setRows(5);
        jScrollPane1.setViewportView(txt_Show);

        UL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ULMousePressed(evt);
            }
        });
        UL.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ULValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(UL);

        btnXuat.setText("LogOut");
        btnXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatActionPerformed(evt);
            }
        });

        btnGroup.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        btnGroup.setText("GROUP CHAT");
        btnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnGroup, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(21, 21, 21)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGroup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ULMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ULMousePressed
        // TODO add your handling code here:
        String user1=UL.getSelectedValue();
        lbName.setText(user1);
        clientUser=user1;
    }//GEN-LAST:event_ULMousePressed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        //Truyen du liei du lieu
        try {
            String m = txtMsg.getText();
            System.out.println(m);
            String n = m;
            String CU = clientUser;
            if (!clientUser.isEmpty()) {
                //Gui tin nhan #Dau tien ma nhan biet, cu ten nguoi nhan, n la tin nhan
                m = "#123456789long17ctt4" + CU + ":" + n;
                dout.writeUTF(m);
                txtMsg.setText("");
                txt_Show.append("< Tôi gửi cho " + CU + " > " + n + "\n");
            } else {
                dout.writeUTF("#MSG_ALL"+m);
                txtMsg.setText("");
                txt_Show.append("< Tôi gửi cho tất cả mọi người >" + n + "\n");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Nguoi gui khong ton tai");
        }
        
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileActionPerformed
        // TODO add your handling code here:
        //gửi file
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        int action = chooser.showOpenDialog(btnFile);
        if (action == JFileChooser.APPROVE_OPTION) {
            File File1 = chooser.getSelectedFile();
            String fileName = File1.getName();
            //System.out.println(fileName);
            try {
                String m = fileName;
                //System.out.println(m);
                String n = m;
                String CU = clientUser;
                System.out.println("gui CU: "+CU);
                if (!clientUser.isEmpty()) {
                    txt_Show.append("< tôi gửi file đến " + CU + " > " + m + "\n");
                       long length = File1.length();
                       //int filesize = (int)Math.ceil(length / BUFFER_SIZE); // phương thức nhận kích thước file
                       dout.writeUTF("CMD_SENDFILE"+CU+":"+fileName);
                       
                       //Tao stream
                       FileInputStream input=new FileInputStream(File1);
                       //DataOutputStream output=new DataOutputStream(socket.getOutputStream());
                       byte[] byteFile=new byte[BUFFER_SIZE];
                       int count=input.read(byteFile);
                       System.out.println("gui file dem: "+count);
                       dout.write(byteFile,0,count);
                        
                        txt_Show.append("Bạn gửi file thanh công \n");
                        //Dong  gui file 
                       // output.flush();
                        //output.close();
                        //input.close();
                        System.out.println("gui file len server thanh cong");
                       
                } else {
                    dout.writeUTF("#CMD1_SENDFILE_ALL"+ m);
                    FileInputStream input = new FileInputStream(File1);
                    //DataOutputStream output=new DataOutputStream(socket.getOutputStream());
                    byte[] byteFile = new byte[BUFFER_SIZE];
                    int count = input.read(byteFile);
                    System.out.println("gui file dem: " + count);
                    dout.write(byteFile, 0, count);

                    txt_Show.append(" Bạn gửi file cho mọi người thành công \n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nguoi gui khong ton tai");
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnFileActionPerformed

    private void btnXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatActionPerformed
        // TODO add your handling code here:
        String i="client thoat khoi server thanh cong";
        try{
            dout.writeUTF(i);
            this.dispose();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnXuatActionPerformed

    private void ULValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ULValueChanged
        // TODO add your handling code here:
        clientUser=(String)UL.getSelectedValue();
        lbName.setText(clientUser);
    }//GEN-LAST:event_ULValueChanged

    private void btnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupActionPerformed
        // TODO add your handling code here:
        clientUser="";
        lbName.setText("User");
        
    }//GEN-LAST:event_btnGroupActionPerformed

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
            java.util.logging.Logger.getLogger(MyClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MyClientJFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> UL;
    private javax.swing.JLabel bnPhone;
    private javax.swing.JButton btnFile;
    private javax.swing.JButton btnGroup;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnXuat;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbHA;
    private javax.swing.JLabel lbHA2;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbadmin;
    private javax.swing.JTextField txtMsg;
    private javax.swing.JTextArea txt_Show;
    // End of variables declaration//GEN-END:variables
    /*
        Hàm này sẽ mở 1 file chooser de luu file
    */
}
