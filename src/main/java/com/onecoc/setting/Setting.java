package com.onecoc.setting;

import javax.swing.*;

/**
 * @author yuany
 */
public class Setting {
    private JTextField 接口地址TextField;
    private JPanel panel1;
    private JButton 保存Button;
    private JTextArea booleanJavaLangBooleanTextArea;

    public JTextField get接口地址TextField() {
        return 接口地址TextField;
    }

    public Setting set接口地址TextField(JTextField 接口地址TextField) {
        this.接口地址TextField = 接口地址TextField;
        return this;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public Setting setPanel1(JPanel panel1) {
        this.panel1 = panel1;
        return this;
    }

    public JButton get保存Button() {
        return 保存Button;
    }

    public Setting set保存Button(JButton 保存Button) {
        this.保存Button = 保存Button;
        return this;
    }
}
