package gui;

public class ChecklistItem {
    private int id;
    private int noteId;
    private String text;
    private boolean checked;

    public ChecklistItem(int id, int noteId, String text, boolean checked) {
        this.id = id;
        this.noteId = noteId;
        this.text = text;
        this.checked = checked;
    }

    public ChecklistItem(int noteId, String text, boolean checked) {
        this.noteId = noteId;
        this.text = text;
        this.checked = checked;
    }

    // Getters and Setters
    public int getId() { return id; }
    public int getNoteId() { return noteId; }
    public String getText() { return text; }
    public boolean isChecked() { return checked; }

    public void setId(int id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setChecked(boolean checked) { this.checked = checked; }

    @Override
    public String toString() {
        return (checked ? "[x] " : "[ ] ") + text;
    }
}
