package open.xyq.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class OpenDialog extends JFileChooser {

    private static final long serialVersionUID = 1L;

    public OpenDialog() {
        super();
        setMultiSelectionEnabled(true);
    }

    public int showDialog(Component parent, File dir, String title, FileFilter filter) {
        setCurrentDirectory(dir);
        setFileFilter(filter);
        setDialogTitle(title);

        return showOpenDialog(parent);
    }

    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }
}
