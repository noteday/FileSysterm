package util;

import java.awt.Component;
import javax.swing.JOptionPane;

//弹窗显示各类提示出错信息
public class MessageUtil {
    public MessageUtil() {
    }

    public static void showMgs(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "错误", 0);
    }

    public static void showErrorMgs(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "错误", 0);
    }

    public static int showConfirmMgs(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "确认", 0);
    }
}
