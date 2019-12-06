package util;

public class FileSystemUtil {
    public static int num = 5;
    public static String folderPath = "/Users/noteday/IdeaProjects/FileSysterm/src/images/folder.jpg";
    public static String folder1Path = "/Users/noteday/IdeaProjects/FileSysterm/src/images/folder1.jpg";
    public static String filePath = "/Users/noteday/IdeaProjects/FileSysterm/src/images/file.jpg";
    public static String file1Path = "/Users/noteday/IdeaProjects/FileSysterm/src/images/file1.jpg";
    public static String diskPath = "/Users/noteday/IdeaProjects/FileSysterm/src/images/disk.jpg";
    public static String imgPath = "/Users/noteday/IdeaProjects/FileSysterm/src/images/img1.jpg";

    public static int END = 255;      //文件结束
    public static int DISK = 0;      //表示该物理块可以使用
    public static int ERROR =254;      //表示该物理块已经损坏

    public static int FOLDER = 1;    //代表目录
    public static int FILE = 2;       //代表文件
    public static int flagRead = 0;
    public static int flagWrite = 1;

    public FileSystemUtil() {
    }

    public static int getHeight(int n) {
        int a = n / 4;
        if (n % 4 > 0) {
            ++a;
        }

        return a * 120;
    }

    //返回文件所占用的物理块数
    public static int getNumOfFAT(int length) {
        if (length <= 64) {
            return 1;
        } else {
            int n;
            if (length % 64 == 0) {
                n = length / 64;
            } else {
                n = length / 64;
                ++n;
            }
            return n;
        }
    }
}
