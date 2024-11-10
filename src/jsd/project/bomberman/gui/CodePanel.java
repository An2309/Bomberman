package jsd.project.bomberman.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CodePanel extends JFrame implements ActionListener {
    private final JTextField codeField = new JTextField();
    private final Frame frame;

    public CodePanel(Frame _frame) {
        frame = _frame;
        setTitle("Code");
        setSize(400, 100);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JButton btEnter = new JButton("Enter");
        btEnter.addActionListener(this);
        JLabel code = new JLabel("Enter Code: ");
        add(code, BorderLayout.WEST);
        add(codeField, BorderLayout.CENTER);
        add(btEnter, BorderLayout.PAGE_END);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String code = codeField.getText();

        if (frame.validCode(code)) {
            frame.changeLevelByCode(code);
            dispose();
        } else {
            if (frame.gamePanel.getGame().getMenu()) {
                JOptionPane.showMessageDialog(frame,
                        "You haven't started the game yet! Please start the game to change level by code!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "That code isn't correct! Please enter the code again!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
