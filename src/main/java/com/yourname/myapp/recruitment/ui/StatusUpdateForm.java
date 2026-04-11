package com.yourname.myapp.recruitment.ui;

import com.yourname.myapp.recruitment.entity.Candidate;
import com.yourname.myapp.recruitment.service.CandidateService;
import com.yourname.myapp.recruitment.service.CandidateServiceImpl;

import javax.swing.*;
import java.awt.*;

public class StatusUpdateForm extends JFrame {
    private final CandidateService candidateService = new CandidateServiceImpl();
    private final CandidateListView parent;
    private final String candidateId;
    private JComboBox<String> statusCombo;

    public StatusUpdateForm(CandidateListView parent, String candidateId) {
        this.parent = parent;
        this.candidateId = candidateId;
        setTitle("Update Candidate Status");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        Candidate candidate = candidateService.getCandidateById(candidateId);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Current Status:"));
        panel.add(new JLabel(candidate.getApplicationStatus().toString()));
        panel.add(new JLabel("New Status:"));
        statusCombo = new JComboBox<>(new String[]{"APPLIED", "SHORTLISTED", "INTERVIEW", "SELECTED", "REJECTED"});
        panel.add(statusCombo);
        JButton updateBtn = new JButton("Update Status");
        updateBtn.addActionListener(e -> onUpdateStatus());
        panel.add(updateBtn);
        add(panel);
    }

    private void onUpdateStatus() {
        try {
            String newStatus = (String) statusCombo.getSelectedItem();
            candidateService.updateStatus(candidateId, newStatus);
            JOptionPane.showMessageDialog(this, "Status updated successfully.");
            parent.loadCandidates(null);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
