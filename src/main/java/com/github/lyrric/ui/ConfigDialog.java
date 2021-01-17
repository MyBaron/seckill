package com.github.lyrric.ui;

import com.github.lyrric.conf.Config;
import com.github.lyrric.util.ParseUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2020-07-23.
 * 设置微信的cookie
 * @author wangxiaodong
 */
public class ConfigDialog extends JDialog {

    JButton submit;

    JTextField sessionId;

    Frame owner;

    boolean success = false;
    public ConfigDialog(Frame owner) {
        super(owner, true);
        this.owner = owner;
        init();
    }

    public void init(){
        this.setLayout(null);
        this.setTitle("请输入Cookie");

        JLabel tkLabel = new JLabel("Cookie：");
        tkLabel.setBounds(10, 20, 60, 25);
        this.add(tkLabel);
        sessionId = new JTextField();
        sessionId.setBounds(70, 20, 350, 25);
        this.add(sessionId);


        submit = new JButton("保存");
        submit.setBounds(280, 100, 100, 40);
        submit.addActionListener(e -> {
            if( sessionId.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "请输入Cookie","提示", JOptionPane.PLAIN_MESSAGE);
            }else{
                Config.cookie = sessionId.getText();
                success = true;
                this.dispose();
            }
        });

        this.add(submit);
        this.setVisible(false);
        this.setBounds(500, 500, 460, 200);
        this.setResizable(false);
        setLocationRelativeTo(null);
    }

    public boolean success(){
        return success;
    }
}
