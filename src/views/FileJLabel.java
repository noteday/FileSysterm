package views;


import util.FileSystemUtil;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//显示文件列表（文件logo+文件名）
public class FileJLabel extends JLabel {
    public boolean type = false;

    public FileJLabel(boolean isFile, String content) {

        //设置标签的文本相对其图像的垂直位置
        this.setVerticalTextPosition(3);
        //设置标签的文本相对其图像的水平位置
        this.setHorizontalTextPosition(0);
        //设置文件名
        this.setText(content);
        if (isFile) {
            this.setIcon(new ImageIcon(FileSystemUtil.filePath));
            this.type = true;
        } else {
            this.setIcon(new ImageIcon(FileSystemUtil.folderPath));
            this.type = false;
        }

    }

    public boolean isType() {
        return this.type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
