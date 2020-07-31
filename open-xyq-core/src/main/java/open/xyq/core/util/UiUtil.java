package open.xyq.core.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class UiUtil {

    public static void launchImageAppBlocked(Image image) {
        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setLocation(700, 300);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setVisible(true);
        blockUntilExit(frame);
    }

    public static void launchAnimationAppBlocked(Image[] images) {

    }

    private static void blockUntilExit(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        AtomicBoolean closed = new AtomicBoolean(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed.set(true);
            }
        });
        while (!closed.get()) {
            SysUtil.sleep(Duration.ofMillis(100));
        }
    }
}
