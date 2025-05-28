package gui;

import java.sql.Timestamp;

public class Note {
    private int id;
    private int userId;
    private String title;
    private String content;
    private boolean trashed;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean pinned;

    // Constructor đầy đủ
    public Note(int id, int userId, String title, String content, boolean trashed, Timestamp createdAt, Timestamp updatedAt, boolean pinned) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.trashed = trashed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.pinned = pinned;
    }

    // Constructor khi thêm mới
    public Note(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.trashed = false;
        this.pinned = false;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getter và Setter
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public boolean isTrashed() { return trashed; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public boolean isPinned() { return pinned; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setTrashed(boolean trashed) { this.trashed = trashed; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }

    @Override
    public String toString() {
        return (pinned ? "📌 " : "") + title;
    }
}
