package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChecklistFrame extends JFrame {
    private int noteId;
    private ChecklistDAO checklistDAO;
    private DefaultListModel<ChecklistItem> itemListModel;
    private JList<ChecklistItem> itemList;
    private JButton addButton, deleteButton, saveButton;

    public ChecklistFrame(int noteId) {
        this.noteId = noteId;
        checklistDAO = new ChecklistDAO();
        setTitle("Checklist");
        setSize(400, 400);
        setLocationRelativeTo(null);

        itemListModel = new DefaultListModel<>();
        itemList = new JList<>(itemListModel);
        itemList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JCheckBox checkBox = new JCheckBox(value.getText(), value.isChecked());
            checkBox.setEnabled(false);
            checkBox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            return checkBox;
        });

        loadItems();

        addButton = new JButton("Add Item");
        deleteButton = new JButton("Delete Item");
        saveButton = new JButton("Save Changes");

        addButton.addActionListener(e -> {
            String text = JOptionPane.showInputDialog(this, "New item:");
            if (text != null && !text.trim().isEmpty()) {
                ChecklistItem newItem = new ChecklistItem(noteId, text.trim(), false);
                checklistDAO.addItem(newItem);
                loadItems();
            }
        });

        deleteButton.addActionListener(e -> {
            ChecklistItem selected = itemList.getSelectedValue();
            if (selected != null) {
                checklistDAO.deleteItem(selected.getId());
                loadItems();
            }
        });

        saveButton.addActionListener(e -> {
            for (int i = 0; i < itemListModel.size(); i++) {
                ChecklistItem item = itemListModel.get(i);
                checklistDAO.updateItem(item);
            }
            JOptionPane.showMessageDialog(this, "Saved!");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        JCheckBoxListEditor editor = new JCheckBoxListEditor(itemListModel, itemList);
        JScrollPane scrollPane = new JScrollPane(editor);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadItems() {
        itemListModel.clear();
        for (ChecklistItem item : checklistDAO.getItemsByNoteId(noteId)) {
            itemListModel.addElement(item);
        }
    }
}
