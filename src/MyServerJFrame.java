
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
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
public class MyServerJFrame extends javax.swing.JFrame {

    /**
     * Creates new form MyServerJFrame
     */
    ServerSocket ss;
    Socket s;
    private final int BUFFER_SIZE = 16 * 1024;
    int k = 0;
    //Chứa danh sách tất cả client
    HashMap clientAll = new HashMap();
    //-----11-----
    MyConnect myConn = new MyConnect();

    public MyServerJFrame() {
        try {
            //------1----
            myConn.connect();
            initComponents();
            //Tao serversocket và lắng nghe port 3211
            ss = new ServerSocket(3211);
            InetAddress addr = InetAddress.getLocalHost();

            //Host IP Address
            String ipAddress = addr.getHostAddress();
            //System.out.println("IP: "+ ipAddress);
            this.lbStatus.setText("Port: " + ss.getLocalPort() + " - IP: " + ipAddress);
            //Chấp nhận các yêu cầu kết nối từ client
            new ClientAccep().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientAccep extends Thread {

        //private Socket s;
        public void run() {
            while (true) {
                try {
                    //Kiem tra mat khau---1--
                    ResultSet rs = myConn.getData("login_client");
                    //serversocket chấp nhận socket của client kết nối
                    s = ss.accept();//doi client
                    //Doc du lieu của socket gui cho server
                    String i = new DataInputStream(s.getInputStream()).readUTF();//Ten của client chay trong server
                    System.out.println("i server: " + i);
                    //Kiem tra phan dangki
                    //Kiem tra dang nhap
                    if (i.contains("signup")) {
                        i = i.substring(6);
                        //System.out.println("i signup: "+i);
                        StringTokenizer st = new StringTokenizer(i, ":");
                        //Lay user va pass tu login gui den---1---
                        i = st.nextToken();
                        String pass = st.nextToken();
                        System.out.println("user: " + i + "; pass: " + pass);
                        int check1 = 0;
                        try {
                            while (rs.next()) {
                                if (rs.getString("usename").equals(i) && rs.getString("pass").equals(pass)) {
                                    check1 = 1;
                                    
                                    //Gui cho user la tai khoan nay da dang ki
                                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                    dout.writeUTF("no123");
                                    break;
                                }
                            }
                            //Dang ki tai khoan
                            if(check1==0){
                                myConn.insertAccount(i, pass);
                                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                dout.writeUTF("yes123");
                            }
                        } catch (SQLException ex) {
                            System.out.println("ERROR CONN SignUp");
                        }

                    } else {

                        StringTokenizer st = new StringTokenizer(i, ":");
                        //Lay user va pass tu login gui den---1---
                        i = st.nextToken();
                        String pass = st.nextToken();
                        //System.out.println("user: "+i+"; pass: "+pass);
                        int check = 0;
                        //Kiem tra user va pass
                        while (rs.next()) {
                            if (rs.getString("usename").equals(i) && rs.getString("pass").equals(pass)) {
                                //Kiiemr tra neu tu khoa cua client trung voi server thi báo da ton tai tai khoản trong server
                                check = 1;
                                if (clientAll.containsKey(i)) {
                                    //Gửi thông tin này cho client
                                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                    dout.writeUTF("Tài khoản này đa tồn tại trong server.");
                                } else {
                                    //them du lieu của i(key) va du du lieu(s client) vao trong server
                                    clientAll.put(i, s);
                                    //In ra man hinh hien thi cua server
                                    msgBox.append(i + " đang online \n");
                                    // Truyen du lieu cho socket đó
                                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                                    dout.writeUTF("");
                                    //Nhan tin nhan của client
                                    new MsgRead(s, i).start();
                                    new PrepareClientList().start();
                                }
                            }
                        }
                        if (check == 0) {
                            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                            dout.writeUTF("Tài khoản của ban chưa đăng kí");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Doc tin nhan
    class MsgRead extends Thread{
        Socket s;
        String USER;
        MsgRead(Socket s,String use){
            this.s=s;
            this.USER=use;
        }
        public void run(){
            //Kiểm tra sanh sach client nay có trống(rỗng ) không
            while(!clientAll.isEmpty()){
                try{
                    //Nhan du lieu tu client truyen tới
                    String i= new DataInputStream(s.getInputStream()).readUTF();
                    //System.out.println("Kq i: "+i);
                    //Neu du lieu truyên tơi bằng chuỗi này thi xóa client nay hay thoát client nay khỏi server
                    if(i.equals("client thoat khoi server thanh cong")){
                        //System.out.println("thoát client khỏi server");
                        //Xóa Use ra khỏi danh sách các client
                        clientAll.remove(USER);
                        //In thong báo client ta màn hình server
                        msgBox.append(USER+" :thoát\n");
                        new PrepareClientList().start();
                        
                        //Tập hợp các key của danh sách client
                        //Vi du nhu k=[long,lan,phuong]
                        Set<String> k=clientAll.keySet();
                        Iterator itr=k.iterator();
                        while(itr.hasNext()){
                            String key=(String)itr.next();//Lay Key( phan tu UserName)
                            //Neu USER khong co key(usename) 
                            if(!key.equalsIgnoreCase(USER)){
                                try{
                                    //Truyen du lieu cua Socket cúa key nay
                                    new DataOutputStream(((Socket)clientAll.get(key)).getOutputStream()).writeUTF("< "+ USER +" nhắn mọi người >"+i);
                                }catch(Exception e){
                                    clientAll.remove(key);
                                    msgBox.append(key+": thoát");
                                    new PrepareClientList().start();
                                }
                            }
                            
                        }
                    }
                    //Gui msg ca nhan
                    else if (i.contains("#123456789long17ctt4")) {
                        i = i.substring(20);
                        //System.out.println("i: "+i);
                        StringTokenizer st = new StringTokenizer(i, ":");
                        String users = st.nextToken();
                        i = st.nextToken();
                        try {
                            new DataOutputStream(((Socket) clientAll.get(users)).getOutputStream()).writeUTF("< " + USER + " đến "+users +"> " + i);
                        } catch (Exception e) {
                            clientAll.remove(users);
                            msgBox.append(users + ": thoát");
                            new PrepareClientList().start();
                        }
                        //Gui tin nhan cho tat ca moi nguoi
                    }else if(i.contains("#MSG_ALL")){
                        i = i.substring(8);
                        Set k=clientAll.keySet();
                        Iterator itr=k.iterator();
                        while(itr.hasNext()){
                            String key=(String)itr.next();
                            if(!key.equalsIgnoreCase(USER)){
                                try{
                                   new DataOutputStream(((Socket) clientAll.get(key)).getOutputStream()).writeUTF("< "+USER+" nhắn tin nhắn >"+i); 
                                }catch(Exception ex){
                                    clientAll.remove(key);
                                    msgBox.append(key+": thoát");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                        //Gui file ca nhan
                    }else if(i.contains("CMD_SENDFILE")){
                        i = i.substring(12);
                        StringTokenizer st = new StringTokenizer(i, ":");
                        String users = st.nextToken();
                        //System.out.println("User server: "+users);
                        i = st.nextToken();
                        //System.out.println("ND server: "+i);
                        try {
                            new DataOutputStream(((Socket) clientAll.get(users)).getOutputStream()).writeUTF("sendfile1"+"< " + USER + " đến  " + users + ">" +":"+i);
                            InputStream dis = s.getInputStream();
                            byte[] bytes=new byte[BUFFER_SIZE];
                            int count=dis.read(bytes);
                            System.out.println("count: "+count);
                            new DataOutputStream(((Socket) clientAll.get(users)).getOutputStream()).write(bytes, 0, count);
                            //dos.flush();
                            //dos.close();
                            System.out.println("Server gui file thanh cong cho client");
                        } catch (IOException e) {
                            
                            clientAll.remove(users);
                            msgBox.append(users + ": loi dui du lieu file \n");
                            new PrepareClientList().start();
                        }
                    }
                    else if (i.contains("#CMD1_SENDFILE_ALL")) {
                        i = i.substring(18);
                        Set k = clientAll.keySet();
                        Iterator itr = k.iterator();
                         byte[] bytes = new byte[BUFFER_SIZE];
                        InputStream dis = s.getInputStream();
                        int count = dis.read(bytes);
                        while (itr.hasNext()) {
                            String key = (String) itr.next();
                            if (!key.equalsIgnoreCase(USER)) {
                                try {
                                    System.out.println("Key file: "+key);
                                    new DataOutputStream(((Socket) clientAll.get(key)).getOutputStream()).writeUTF("sendfile1"+"< " + USER + " đến  " + key + ">" +":"+i);
                                    
                                    System.out.println("count: " + count);
                                    new DataOutputStream(((Socket) clientAll.get(key)).getOutputStream()).write(bytes, 0, count);
                                    //dos.flush();
                                    //dos.close();
                                    System.out.println("Server gui file thanh cong cho client");
                                } catch (IOException e) {

                                    clientAll.remove(key);
                                    msgBox.append(key + ": loi dui du lieu file \n");
                                    new PrepareClientList().start();
                                }
                            }
                        }

                    } else {
                        System.out.println("Gui that bai");
                    }
                    //break;
                }catch(IOException e){
                    
                    e.printStackTrace();
                }
            }
            //System.out.println("run 2");
        }
     }
        //Chuan bi danh sach cac client
    class PrepareClientList extends Thread {

        @Override
        public void run() {
            try {
                //Tin nhan rong
                String magin = "";
                //Lay khoa du lieu tu cac danh sách client
                //System.out.println("Hello 12");
                Set k = clientAll.keySet();
                Iterator itr = k.iterator();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    magin += key + ",";
                }
                if (magin.length() != 0) {
                    magin = magin.substring(0, magin.length() - 1);
                }
                //Lay du lieu cac sọcket cua client
                itr = k.iterator();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    try {
                        System.out.println("margin: "+magin);
                        //Server gưi du lieu tin nhan cho client
                        new DataOutputStream(((Socket) clientAll.get(key)).getOutputStream()).writeUTF("message"+magin);
                    } catch (Exception e) {
                        //Neu loi minh se xoa ra khoi danh sach client
                        // TODO: handle exception
                        clientAll.remove(key);
                        msgBox.append(key + " :thoát");
                        //new PrepareClientList().start();
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgBox = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Myserver");

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chattingapp/photo/icons8-server-80.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SERVER");

        lbStatus.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        lbStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbStatus.setText(":Load...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22))))
        );

        msgBox.setColumns(20);
        msgBox.setRows(5);
        jScrollPane1.setViewportView(msgBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(MyServerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyServerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyServerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyServerJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyServerJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JTextArea msgBox;
    // End of variables declaration//GEN-END:variables
}
