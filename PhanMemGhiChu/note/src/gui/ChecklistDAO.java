package gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChecklistDAO {

    public List<ChecklistItem> getItemsByNoteId(int noteId) {
        List<ChecklistItem> items = new ArrayList<>();
        String sql = "SELECT * FROM checklists WHERE note_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new ChecklistItem(
                    rs.getInt("id"),
                    noteId,
                    rs.getString("item_text"),
                    rs.getBoolean("checked")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean addItem(ChecklistItem item) {
        String sql = "INSERT INTO checklists (note_id, item_text, checked) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getNoteId());
            ps.setString(2, item.getText());
            ps.setBoolean(3, item.isChecked());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateItem(ChecklistItem item) {
        String sql = "UPDATE checklists SET item_text = ?, checked = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getText());
            ps.setBoolean(2, item.isChecked());
            ps.setInt(3, item.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM checklists WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
