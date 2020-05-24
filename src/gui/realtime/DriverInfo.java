package gui.realtime;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverInfo extends JFrame{
    private JTextField nameIn;
    private JTextField telIn;
    private JTextField emailIn;
    private JButton upload;
    private JPanel mainPanel;
    private String name;
    private String tel;
    private String email;

    public DriverInfo() {
        super("Enter your information");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        this.setSize(200,200);
        upload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==upload){
                    name =nameIn.getText();
                    tel=telIn.getText();
                    email=emailIn.getText();
                    disappear();
                }
            }
        });
    }


    @Override
    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    private void disappear(){
        this.dispose();

    }
}
