/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stdmgmtapp.dao;

import stdmgmtapp.db.DbConnection;
import stdmgmtapp.dto.Course;
import stdmgmtapp.dto.Score;
import stdmgmtapp.dto.Student;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import stdmgmtapp.AddCourse;
import stdmgmtapp.dto.Attendence;
import stdmgmtapp.dto.StuCourseCombo;

/**
 *
 * @author hp
 */
public class Myfunction {
    public static int count(String table){
        int total=0;
    Connection conn=DbConnection.getConnection();
    Statement st;
        try {
            st=conn.createStatement();
            ResultSet rs=st.executeQuery("SELECT COUNT(*) as 'total' from "+table);
            while(rs.next()){
             total=rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;

    }
    public static void addStudent(Student s){
         
       
            Connection conn=DbConnection.getConnection();
           PreparedStatement ps;
            try {
                ps=conn.prepareStatement("INSERT INTO student(fitst_name,last_name,sex,birthdate,phone,address,image) VALUES(?,?,?,?,?,?,?)");
                ps.setString(1,s.getFitst_name());
            ps.setString(2,s.getLast_name());
            ps.setString(3,s.getSex());
            ps.setString(4,s.getBirthdate());
            ps.setString(5,s.getPhone());
            ps.setString(6,s.getAddress());
            
           InputStream is;
           try {
                is = new FileInputStream(new File(s.getImage().toString()));
                ps.setBlob(7,is);
                ps.executeUpdate();
           } catch (FileNotFoundException ex) {
               Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
           }
          
            } catch (SQLException ex) {
                Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            }
    
    public static void fillStudentTable(JTable table,String valueToSearch){
    Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("SELECT * FROM `student` WHERE CONCAT(`fitst_name`,`last_name`,`address`,`phone`,`sex`)LIKE ?");
            ps.setString(1, "%"+valueToSearch+"%");
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[8];
            row[0]=rs.getInt(1);
            row[1]=rs.getString(2);
            row[2]=rs.getString(3);
            row[3]=rs.getString(4);
            row[4] = rs.getString(5);
            row[5]=rs.getString(6);
            row[6]=rs.getString(7);
            row[7]=rs.getBytes(8);
            //byte[]b =rs.getBytes(8);
            //image code
//            if(b!=null){
//                ImageIcon image=new ImageIcon(new ImageIcon(b).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
//            row[7]=rs.getBlob(8);
//            }
//            else 
//            row[7]=null;
//            
            d.addRow(row);
           // ManageStudentsForm.table.getColumnModel().getColumn(7).setPreferredWidth(60);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void updateRecord(Student s){
    try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("UPDATE `student` SET `fitst_name`=?,`last_name`=?,`sex`=? ,`birthdate`=?,`phone`=?,`address`=? ,`image`=? where `id`=?");
            ps.setString(1,s.getFitst_name());
            ps.setString(2,s.getLast_name());
            ps.setString(3,s.getSex());
            ps.setString(4,s.getBirthdate());
            ps.setString(5,s.getPhone());
            ps.setString(6,s.getAddress());
            
            InputStream is;
           try {
                is = new FileInputStream(new File(s.getImage()));
                ps.setBlob(7,is);
                
           } catch (FileNotFoundException ex) {
               Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
           }
            ps.setString(8,s.getId()+"");
            
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "student record updated...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
     public static void deleteRecord(int id ){
         /*
        ALTER TABLE score
add CONSTRAINT fk_score_student
FOREIGN KEY(`student_id`)
REFERENCES student(Id)
ON DELETE CASCADE
         */
         int yesorno=JOptionPane.showConfirmDialog(null, "Score will also be deleted","Delete Student" ,JOptionPane.OK_CANCEL_OPTION,0);
         if(yesorno==0){
         try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("DELETE FROM `student` WHERE `id`="+id);
           
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "student record deleted...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    }
     
    public static void addCourse(Course s){
     
        try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("INSERT INTO `course`(`label`,`hours_number`) VALUES( ? , ?) ");
            ps.setString(1,s.getLabel());
            ps.setString(2,s.getHours_number().toString());
            
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "new course added..");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
    
    //methods of coursess.............
    public static boolean isCourseExist(String label){
      boolean b=false;
      Connection con=DbConnection.getConnection();
      PreparedStatement ps;
        try {
            ps=con.prepareStatement("SELECT * FROM `course` WHERE `label`=?");
            ps.setString(1, label);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                b=true;
                //return false;
        } catch (SQLException ex) {
            Logger.getLogger(AddCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
  
  }
    public static void deleteCourse(int id ){
    int yesorno=JOptionPane.showConfirmDialog(null, "Score will also be deleted","Delete Course" ,JOptionPane.OK_CANCEL_OPTION,0);
         if(yesorno==JOptionPane.OK_OPTION){
        try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("DELETE FROM `course` WHERE `id`="+id);
           
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "course deleted...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
         }
    
    }
    
    public static void updateCourse(Course s){
    try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("UPDATE `course` SET `label`=?,`hours_number`=? where `id`=?");
            ps.setString(1,s.getLabel());
            ps.setString(2,s.getHours_number()+"");
            ps.setString(3,s.getId()+"");
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "Course  updated...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public static void fillCourseTable(JTable table,String valueToSearch){
        
    Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("SELECT * FROM `course` WHERE CONCAT(`label`,`hours_number`)LIKE ?");
            
            ps.setString(1, "%"+valueToSearch+"%");
            ResultSet rs=ps.executeQuery();
            //System.out.println("fillfunction");
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            
            Object row[];
            while(rs.next())
            {
            row=new Object[3];
            row[0]=rs.getInt(1);
            row[1]=rs.getString(2);
            row[2]=rs.getInt(3);
            
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static int getCourseId(String label){
    int cid=0;
    Connection con=DbConnection.getConnection();
      PreparedStatement ps;
        try {
            ps=con.prepareStatement("SELECT * FROM `course` WHERE `label`=?");
            ps.setString(1, label);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                cid=rs.getInt(1);
                //return false;
        } catch (SQLException ex) {
            Logger.getLogger(AddCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
    return cid;
    }
     
    public static void coursecombo(JComboBox cb){
        Connection con=DbConnection.getConnection();
      PreparedStatement ps;
        try {
            ps=con.prepareStatement("SELECT * FROM `course` ");
            
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {   cb.addItem(rs.getString(2));}
                //return false;
        } catch (SQLException ex) {
            Logger.getLogger(AddCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //score methodsssssssssssss
    public static void insertscore(Score s){
    Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
        try {
            ps=conn.prepareStatement("INSERT INTO `score`(`student_id`,`course_id`,`student_score`,`description`) VALUES (?,?,?,?)");
            ps.setString(1,s.getSid()+"");
            ps.setString(2,s.getCid()+"");
            ps.setString(3,s.getScore()+"");
            ps.setString(4,s.getDesc());
            if(ps.executeUpdate()>0)
              JOptionPane.showMessageDialog(null, "Score added...");
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }public static void deleteScore(int sid ,int cid){
    try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("DELETE FROM `score` WHERE `student_id`=? AND `course_id`=?");
           ps.setString(1, sid+"");
           ps.setString(2, cid+"");
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "Score deleted...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public static void updateScore(Score s){
    try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("UPDATE `score` SET `student_score`=?,`description`=? where `student_id`=? AND course_id=?");
            ps.setString(1,s.getScore()+"");
            ps.setString(2,s.getDesc()+"");
            ps.setString(3,s.getSid()+"");
            ps.setString(4,s.getCid()+"");
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "Score  updated...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public static void fillScoreTable(JTable table,String valueToSearch){
    Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("SELECT * FROM `score` WHERE CONCAT(`student_id`,`course_id`,`student_score`,`description`)LIKE ?");
            ps.setString(1, "%"+valueToSearch+"%");
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[4];
            row[0]=rs.getInt(1);
            row[1]=rs.getInt(2);
            row[2]=rs.getInt(3);
            row[3]=rs.getString(4);
            
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public static void showScores(JTable table){
    Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement(" SELECT `student_id`,`fitst_name`,`last_name`,`label`,`student_score` from `score` "
                    + "INNER JOIN student as stab on stab.id=student_id "
                    + "INNER JOIN course as ctab on ctab.id=course_id");
           // ps.setString(1, "%"+valueToSearch+"%");
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[5];
            row[0]=rs.getInt(1);
            row[1]=rs.getString(2);
            row[2]=rs.getString(3);
            row[3]=rs.getString(4);
            row[4] = rs.getInt(5);
            
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
//methods of attendence module
//       public static void fillattable(int cid){
//           Connection conn=DbConnection.getConnection();
//    PreparedStatement ps;
//    ps.prepareStatement(select `id`,`fitst_name`,`last_name` from student where ``)
//    
//    ResultSet rs=ps.executeQuery();
//            //System.out.println("fillfunction");
//            DefaultTableModel d=(DefaultTableModel)table.getModel();
//            
//            Object row[];
//            while(rs.next())
//            {
//            row=new Object[3];
//            row[0]=rs.getInt(1);
//            row[1]=rs.getString(2);
//            row[2]=rs.getInt(3);
//            
//            d.addRow(row);
//            }
//       }

public static void fillEnrollTable(JTable table,String valuetosearch){
 Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("select `id`,`fitst_name`,`last_name`from `student` WHERE CONCAT(`id`,`fitst_name`,`last_name`)LIKE ?");
            ps.setString(1, "%"+valuetosearch+"%");
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[4];
            row[0]=rs.getInt(1);
            row[1]=rs.getString(2);
            row[2]=rs.getString(3);
            row[3]=false;
            
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    public static void addRecordinstucourseTable(StuCourseCombo sc) {
       Connection conn=DbConnection.getConnection();
       PreparedStatement ps;
        try {
            ps=conn.prepareStatement("INSERT INTO `studentcoursetable`(`sid`,`sfname`,`slname`,`cid`) VALUES (?,?,?,?)");
            ps.setString(1,sc.getSid()+"");
            ps.setString(2,sc.getSfname()+"");
            ps.setString(3,sc.getSlname()+"");
            ps.setString(4,sc.getCid()+"");
            ps.executeUpdate();
            
              
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

   
            public static void fillattendencetable(JTable table,String cid){
    Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("SELECT sid,sfname,slname FROM `studentcoursetable` WHERE `cid`=?");
            ps.setString(1,cid);
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[4];
            row[0]=rs.getInt(1);
            row[1]=rs.getString(2);
            row[2]=rs.getString(3);
            row[3]="Attendence";
            
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            public static void addAttendence(Attendence a){
                 Connection conn=DbConnection.getConnection();
           PreparedStatement ps;
            try {
                ps=conn.prepareStatement("INSERT INTO attendencetable(sid,cid,Attendence,Date,Time) VALUES(?,?,?,?,?)");
                ps.setString(1,a.getSid()+"");
            ps.setString(2,a.getCid()+"");
            ps.setString(3,a.getAttendence());
            ps.setString(4,a.getDate()+"");
            ps.setString(5,a.getTime()+"");
            
          ps.executeUpdate();
           
          
            } catch (SQLException ex) {
                Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

    public static void updateAttinstucoursetable(int cid,int sid,int n) {
       try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
             ps=conn.prepareStatement("SELECT `Totalpresent`,`Totalelecture` FROM `studentcoursetable` WHERE `cid`=? AND `sid`=?");
             ps.setString(1,cid+"");
             ps.setString(2,sid+"");
             ResultSet rs=ps.executeQuery();
             rs.next();
             int totalpresent=rs.getInt(1);
             int totallecture=rs.getInt(2);
             if(n==1){
            ps=conn.prepareStatement("UPDATE `studentcoursetable` SET `Totalpresent`=?,`Totalelecture`=? where `cid`=? AND `sid`=?");
            ps.setString(1,totalpresent+1+"");
            ps.setString(2,totallecture+1+"");
            ps.setString(3,cid+"");
            ps.setString(4,sid+"");
            ps.executeUpdate();
            }
             else
             {
                 ps=conn.prepareStatement("UPDATE `studentcoursetable` SET `Totalelecture`=? where `cid`=? AND `sid`=?");
           // ps.setString(1,totalpresent+1+"");
            ps.setString(1,totallecture+1+"");
            ps.setString(2,cid+"");
            ps.setString(3,sid+"");
            ps.executeUpdate();
             }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public static void fillattcountable(JTable table,String search) {
         Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("SELECT * FROM `studentcoursetable` WHERE CONCAT(`sid`,`sfname`,`slname`,`cid`,`Totalpresent`,`Totalelecture`)LIKE ?");
            ps.setString(1,"%"+search+"%");
           // ps.setString(1,cid);
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[6];
            row[0]=rs.getInt(1);
            row[1]=rs.getString(2);
            row[2]=rs.getString(3);
            row[3]=rs.getInt(4);
            row[4]=rs.getInt(5);
            row[5]=rs.getInt(6);
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
     public static void fillattdatewisetable(JTable table,String search) {
         Connection conn=DbConnection.getConnection();
    PreparedStatement ps;
   
        try {
            ps=conn.prepareStatement("SELECT * FROM `attendencetable` WHERE CONCAT(`sid`,`cid`,`Attendence`,`Date`,`Time`)LIKE ?");
            ps.setString(1,"%"+search+"%");
           // ps.setString(1,cid);
            ResultSet rs=ps.executeQuery();
            DefaultTableModel d=(DefaultTableModel)table.getModel();
            Object row[];
            while(rs.next()){
            row=new Object[5];
            row[0]=rs.getInt(1);
            row[1]=rs.getInt(2);
            row[2]=rs.getString(3);
            row[3]=rs.getString(4);
            row[4]=rs.getString(5);
            //row[5]=rs.getInt(6);
            d.addRow(row);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void editattendence(String attendence, int cid, int sid) {
      try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("UPDATE `attendencetable` SET `Attendence`=? WHERE `sid`=? AND `cid`=?");
            System.out.println("ara hai");
            ps.setString(1,attendence);
            ps.setString(2,sid+"");
            ps.setString(3,cid+"");
           //int i= ps.executeUpdate();
            //System.out.println(i);
            //ps.setString(4,s.getCid()+"");
            if(ps.executeUpdate()>0){
                System.out.println("ara hai if mai");
                JOptionPane.showMessageDialog(null, "Attendence  updated...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        } 
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public static void editdate(String date1, String date2) {
       try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("UPDATE `attendencetable` SET `Date`=? WHERE `Date`=?");
            System.out.println("ara hai");
            ps.setString(1,date1);
            ps.setString(2,date2);
            //ps.setString(3,cid);
           //int i= ps.executeUpdate();
            //System.out.println(i);
            //ps.setString(4,s.getCid()+"");
            if(ps.executeUpdate()>0){
                System.out.println("ara hai if mai");
                JOptionPane.showMessageDialog(null, "All records with Corresponding Date are updated...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void edittime(String time1, String time2) {
      try {
            Connection conn=DbConnection.getConnection();
            PreparedStatement ps;
            ps=conn.prepareStatement("UPDATE `attendencetable` SET `Time`=? WHERE `Time`=?");
            System.out.println("ara hai");
            ps.setString(1,time1);
            ps.setString(2,time2);
            //ps.setString(3,cid);
           //int i= ps.executeUpdate();
            //System.out.println(i);
            //ps.setString(4,s.getCid()+"");
            if(ps.executeUpdate()>0){
                System.out.println("ara hai if mai");
                JOptionPane.showMessageDialog(null, "All records with Corresponding time are updated...");
            }
            } catch (SQLException ex) {
            Logger.getLogger(Myfunction.class.getName()).log(Level.SEVERE, null, ex);
        }     // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
