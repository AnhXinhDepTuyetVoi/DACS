package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private int userId;
    private NoteDAO noteDAO;

    private DefaultListModel<Note> noteListModel;
    private JList<Note> noteJList;

    private JTextField titleField, searchField;
    private JTextArea contentArea;

    private JButton addButton, updateButton, deleteButton;
    private JButton trashButton, restoreButton, viewTrashButton, viewActiveButton;
    private JButton pinButton, checklistButton, noteInfoButton, logoutButton;

    private boolean viewingTrash = false;

    public MainFrame(int userId) {
        this.userId = userId;
        this.noteDAO = new NoteDAO();

        setTitle("SimpleNote - Notes Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // === Sidebar ===
        noteListModel = new DefaultListModel<>();
        noteJList = new JList<>(noteListModel);
        noteJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noteJList.setCellRenderer(new NoteRenderer());
        JScrollPane listScrollPane = new JScrollPane(noteJList);
        listScrollPane.setMinimumSize(new Dimension(250, 0));

        // === Search Bar ===
        searchField = new JTextField();
        searchField.setToolTipText("Search notes...");
        searchField.addActionListener(e -> searchNotes());

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(searchField, BorderLayout.NORTH);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        // === Main Content ===
        titleField = new JTextField();
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("Title:"), BorderLayout.WEST);
        titlePanel.add(titleField, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(titlePanel, BorderLayout.NORTH);
        inputPanel.add(contentScrollPane, BorderLayout.CENTER);

        // === Buttons ===
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        trashButton = new JButton("Move to Trash");
        restoreButton = new JButton("Restore");
        viewActiveButton = new JButton("View Notes");
        viewTrashButton = new JButton("View Trash");
        pinButton = new JButton("Pin");
        checklistButton = new JButton("Checklist");
        noteInfoButton = new JButton("Note Info");
        logoutButton = new JButton("Logout");

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.add(addButton);
        topButtonPanel.add(updateButton);
        topButtonPanel.add(deleteButton);
        topButtonPanel.add(trashButton);
        topButtonPanel.add(restoreButton);
        topButtonPanel.add(viewActiveButton);
        topButtonPanel.add(viewTrashButton);

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomButtonPanel.add(pinButton);
        bottomButtonPanel.add(checklistButton);
        bottomButtonPanel.add(noteInfoButton);
        bottomButtonPanel.add(logoutButton);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));
        buttonsPanel.add(topButtonPanel);
        buttonsPanel.add(bottomButtonPanel);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(inputPanel, BorderLayout.CENTER);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);

        loadNotes();
        setupListeners();
    }

    private void setupListeners() {
    	noteJList.addListSelectionListener(e -> {
    	    Note selectedNote = noteJList.getSelectedValue();
    	    if (selectedNote != null) {
    	        titleField.setText(selectedNote.getTitle());
    	        contentArea.setText(selectedNote.getContent());

    	        // Cập nhật nút Pin
    	        pinButton.setText(selectedNote.isPinned() ? "Unpin" : "Pin");
    	    }
    	});


        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty");
                return;
            }
            Note newNote = new Note(userId, title, content);
            if (noteDAO.addNote(newNote)) {
                JOptionPane.showMessageDialog(this, "Added new note");
                loadNotes();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Add failed");
            }
        });

        updateButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote == null || selectedNote.isTrashed()) {
                JOptionPane.showMessageDialog(this, "Select a note to update");
                return;
            }
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty");
                return;
            }
            selectedNote.setTitle(title);
            selectedNote.setContent(content);
            if (noteDAO.updateNote(selectedNote)) {
                JOptionPane.showMessageDialog(this, "Updated note");
                loadNotes();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed");
            }
        });

        deleteButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote == null) {
                JOptionPane.showMessageDialog(this, "Select a note to delete");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Permanently delete this note?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (noteDAO.deleteNote(selectedNote.getId())) {
                    JOptionPane.showMessageDialog(this, "Note permanently deleted");
                    loadNotes();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed");
                }
            }
        });

        trashButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote == null || selectedNote.isTrashed()) {
                JOptionPane.showMessageDialog(this, "Select a note to move to trash");
                return;
            }
            if (noteDAO.moveToTrash(selectedNote.getId())) {
                JOptionPane.showMessageDialog(this, "Note moved to trash");
                loadNotes();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to move to trash");
            }
        });

        restoreButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote == null || !selectedNote.isTrashed()) {
                JOptionPane.showMessageDialog(this, "Select a trashed note to restore");
                return;
            }
            if (noteDAO.restoreFromTrash(selectedNote.getId())) {
                JOptionPane.showMessageDialog(this, "Note restored");
                loadNotes();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Restore failed");
            }
        });

        viewTrashButton.addActionListener(e -> {
            viewingTrash = true;
            loadNotes();
        });

        viewActiveButton.addActionListener(e -> {
            viewingTrash = false;
            loadNotes();
        });

        pinButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote == null || selectedNote.isTrashed()) {
                JOptionPane.showMessageDialog(this, "Select a non-trashed note to pin/unpin");
                return;
            }

            boolean newPinState = !selectedNote.isPinned();
            NoteDAO noteDAO = new NoteDAO();
            boolean success = noteDAO.togglePin(selectedNote.getId(), newPinState);
            if (success) {
                selectedNote.setPinned(newPinState); // Cập nhật trạng thái trong bộ nhớ
                pinButton.setText(newPinState ? "Unpin" : "Pin");
                loadNotes(); // Cập nhật lại danh sách để áp dụng thứ tự mới
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update pin status");
            }
        });

        checklistButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote != null) {
                new ChecklistFrame(selectedNote.getId()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Select a note to open checklist");
            }
        });
        
        
        noteInfoButton.addActionListener(e -> {
            Note selectedNote = noteJList.getSelectedValue();
            if (selectedNote != null) {
                String info = String.format("Created: %s\nUpdated: %s\nWords: %d\nCharacters: %d",
                        selectedNote.getCreatedAt(),
                        selectedNote.getUpdatedAt(),
                        selectedNote.getContent().split("\\s+").length,
                        selectedNote.getContent().length());
                JOptionPane.showMessageDialog(this, info, "Note Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Select a note to view info");
            }
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void loadNotes() {
        noteListModel.clear();
        List<Note> notes = viewingTrash ? noteDAO.getTrashedNotesByUserId(userId) : noteDAO.getNotesByUserId(userId);
        for (Note note : notes) {
            noteListModel.addElement(note);
        }
    }

    private void clearFields() {
        titleField.setText("");
        contentArea.setText("");
        noteJList.clearSelection();
    }

    private void searchNotes() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadNotes();
            return;
        }
        noteListModel.clear();
        List<Note> notes = viewingTrash ? noteDAO.getTrashedNotesByUserId(userId) : noteDAO.getNotesByUserId(userId);
        for (Note note : notes) {
            if (note.getTitle().toLowerCase().contains(keyword) || note.getContent().toLowerCase().contains(keyword)) {
                noteListModel.addElement(note);
            }
        }
    }

    private static class NoteRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Note) {
                Note note = (Note) value;
                String text = note.getTitle();
                if (note.isPinned()) {
                    text = "📌 " + text;
                }
                if (note.isTrashed()) {
                    text += " [Trashed]";
                }
                setText(text);

            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame(1);
            mf.setVisible(true);
        });
    }
}