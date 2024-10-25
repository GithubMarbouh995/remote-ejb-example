package client;

import com.illucit.ejbremote.server.CD;
import com.illucit.ejbremote.server.CdDvdStatefulService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CdDvdClient extends JFrame {
    private CdDvdStatefulService cdDvdService;
    private JTextField cdIdField;
    private JTextField cdTitleField;
    private JTextArea displayArea;

    public CdDvdClient() {
        try {
            InitialContext ctx = new InitialContext();
            cdDvdService = (CdDvdStatefulService) ctx.lookup("java:global/your-app/CdDvdServiceImpl");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        setTitle("CD/DVD Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("CD ID:"));
        cdIdField = new JTextField();
        inputPanel.add(cdIdField);
        inputPanel.add(new JLabel("CD Title:"));
        cdTitleField = new JTextField();
        inputPanel.add(cdTitleField);

        JButton addButton = new JButton("Add CD");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCd();
            }
        });
        inputPanel.add(addButton);

        JButton updateButton = new JButton("Update CD");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCd();
            }
        });
        inputPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete CD");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCd();
            }
        });
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JButton listButton = new JButton("List CDs");
        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listCds();
            }
        });
        add(listButton, BorderLayout.SOUTH);
    }

    private void addCd() {
        String id = cdIdField.getText();
        String title = cdTitleField.getText();
        cdDvdService.addCd(new CD(id, title));
        displayArea.setText("CD added: " + id + " - " + title);
    }

    private void updateCd() {
        String id = cdIdField.getText();
        String title = cdTitleField.getText();
        cdDvdService.updateCd(new CD(id, title));
        displayArea.setText("CD updated: " + id + " - " + title);
    }

    private void deleteCd() {
        String id = cdIdField.getText();
        cdDvdService.deleteCd(id);
        displayArea.setText("CD deleted: " + id);
    }

    private void listCds() {
        List<CD> availableCds = cdDvdService.listAvailableCds();
        List<CD> borrowedCds = cdDvdService.listBorrowedCds();
        StringBuilder sb = new StringBuilder();
        sb.append("Available CDs:\n");
        for (CD cd : availableCds) {
            sb.append(cd.getId()).append(" - ").append(cd.getTitle()).append("\n");
        }
        sb.append("\nBorrowed CDs:\n");
        for (CD cd : borrowedCds) {
            sb.append(cd.getId()).append(" - ").append(cd.getTitle()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CdDvdClient().setVisible(true);
            }
        });
    }
}