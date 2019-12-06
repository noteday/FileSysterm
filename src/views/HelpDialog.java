package views;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;


//显示帮助信息
public class HelpDialog extends JDialog {
    private JDialog jd1 = this;
    private JLabel jl;

    public HelpDialog(Component c) {
        this.setTitle("帮助");
        this.setSize(280, 330);
        //设置居中布局
        this.setLayout(new FlowLayout());
        this.setModal(true);
        //设置相对于主页的位置
        this.setLocationRelativeTo(c);
        //设置帮助信息
        this.addJLabel();
        this.setVisible(true);
    }

    private void addJLabel() {
        this.jl = new JLabel();
        String text = "<html>帮助：<br><br>中间的面板中点击右键：<br>1、创建文件；<br>2、创建文件夹<br><br>";
        text = text + "点击文件夹或文件右键：<br>";
        text = text + "1、打开<br>";
        text = text + "2、重命名<br>";
        text = text + "3、删除<br>";
        text = text + "4、属性<br><br>";
        text = text + "双击文件夹或文件则是打开<br><br><br>";
        text = text + "Thank you!";
        text = text + "</html>";
        this.jl.setText(text);
        this.add(this.jl);
    }
}
