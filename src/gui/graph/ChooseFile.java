package gui.graph;

import database.DbMethod;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChooseFile extends JFrame{
    private JPanel mainPanel;
    private JList list;
    private JButton foundDriver;
    private JTextField driverIn;
    private JTextField dateIn;
    private JButton foundDate;
    private JLabel hints;
    private DefaultListModel<String> listModel;
    private String driverText;
    private String dateText;
    private DbMethod db;
    private String stageIndicator="idle";
    private String raceID="123";

    public String getRaceID() {
        return raceID;
    }

    public ChooseFile() {
        super("choose a record");
        final String[] selected = new String[1];
        db=new DbMethod();
        db.connectDb();
        listModel=new DefaultListModel<String>();
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        this.setContentPane(mainPanel);
        this.setSize(400,500);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){

                }

            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(list.getSelectedIndex()!=-1){
                    if(e.getClickCount()==2){
                        selected[0] =(String) list.getSelectedValue();
                        String ID=selected[0].substring(selected[0].indexOf(':')+1,selected[0].indexOf(','));
                        if(stageIndicator.equals("Driver")){
                            searchDriverInRace(ID);
                        }else if(stageIndicator.equals("Race")){
                            raceID=ID;
                            disappear();
                        }

                    }
                }
            }


        });
        foundDriver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==foundDriver){
                    driverText=driverIn.getText();
                    searchDriverInDriver(driverText);
                    stageIndicator="Driver";
                }
            }
        });
        foundDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==foundDate){
                    dateText=dateIn.getText();
                    listModel.clear();
                    searchDateInRace(dateText);

                    stageIndicator="Race";
                }
            }
        });

    }
    private void disappear(){
        this.dispose();

    }
    private void searchDateInRace(String dateText) {
        String sql;
        if (!dateText.equals(""))
        sql="SELECT raceID,driverID,time,trackID from ee5.race where time between'"+dateText+" 00:00:00' and '"+dateText+" 23:59:59';" ;
        else sql="SELECT raceID,driverID,time,trackID from ee5.race ;" ;
        listModel.clear();
        ResultSet resultSet=db.executeSql(sql);
        try{
            if(resultSet.next()){
                do {
                    listModel.addElement("RaceID:"+resultSet.getInt(1)+",DriverID:"+resultSet.getInt(2)+",time:"+resultSet.getString(3)+",trackID:"+resultSet.getInt(4));
                } while(resultSet.next());
                hints.setText("Double click to get the race data selected");
            }else {
                listModel.addElement("DATE 404!!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void searchDriverInDriver(String driverText){
        String sql="SELECT * from ee5.driver where name like '%"+driverText+"%'";
        listModel.clear();
        ResultSet resultSet=db.executeSql(sql);
        try{
            if(resultSet.next()){
                do {
                    listModel.addElement("DriverID:"+resultSet.getInt(1)+",name:"+resultSet.getString(2)+",email:"+resultSet.getString(3));
                } while(resultSet.next());
            hints.setText("Double click to get the race data of the driver selected");
            }else {
                listModel.addElement("DRIVER 404!!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    private void searchDriverInRace(String driverId){
        String sql="SELECT raceID,driverID,time,trackID from ee5.race where driverID= "+driverId+";";
        listModel.clear();
        ResultSet resultSet=db.executeSql(sql);
        try{
            if(resultSet.next()){
                do {
                    listModel.addElement("RaceID:"+resultSet.getInt(1)+",DriverID:"+resultSet.getInt(2)+",time:"+resultSet.getString(3)+",trackID:"+resultSet.getInt(4));
                } while(resultSet.next());
                hints.setText("Double click to get the race data selected");
                stageIndicator="Race";
            }else {
                listModel.addElement("RACE 404!!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
