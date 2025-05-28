package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JCheckBoxListEditor extends JPanel {
    private DefaultListModel<ChecklistItem> model;
    private JList<ChecklistItem> list;

    public JCheckBoxListEditor(DefaultListModel<ChecklistItem> model, JList<ChecklistItem> list) {
        super(new BorderLayout());
        this.model = model;
        this.list = list;

        list.setCellRenderer(new CheckboxListRenderer());

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0) {
                    ChecklistItem item = model.get(index);
                    item.setChecked(!item.isChecked());
                    list.repaint(list.getCellBounds(index, index));
                }
            }
        });

        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    private class CheckboxListRenderer extends JCheckBox implements ListCellRenderer<ChecklistItem> {
        @Override
        public Component getListCellRendererComponent(JList<? extends ChecklistItem> list,
                                                      ChecklistItem value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText(value.getText());
            setSelected(value.isChecked());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setEnabled(true);
            return this;
        }
    }
}
