import javax.swing.*;
import java.awt.*;

class PicView {
    public static void run(String runpath,String filename){
        //形式参数：绝对路径，文件名称
        PicViewFrame picframe = new PicViewFrame(runpath,filename);
        //picframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        picframe.setVisible(true);
    }
}

class PicViewFrame extends JFrame {
    public static final int WIDTH = 720;
    public static final int HEIGHT = 1000;

    private JLabel pic;

    public PicViewFrame(String runpath,String filename) {
        super("PicView");
        setSize(WIDTH, HEIGHT);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //use a label to display a image
        pic = new JLabel();
        add(pic, BorderLayout.CENTER);

        String name = runpath+"\\"+filename+".png";
        //System.out.println(name);
        ImageIcon icon = new ImageIcon(name);
        // 按照图片大小等比缩放
        int imgWidth = icon.getIconWidth();
        int imgHeight = icon.getIconHeight();
        int conWidth = getWidth();
        int conHeight = getHeight();
        int reImgWidth;
        int reImgHeight;
        //按小的进行图片的大小缩放
        if (imgWidth / imgHeight >= conWidth / conHeight) {
            if (imgWidth > conWidth) {
                reImgWidth = conWidth;
                reImgHeight = imgHeight * reImgWidth / imgWidth;
            }
            else {
                reImgWidth = imgWidth;
                reImgHeight = imgHeight;
            }
        }
        else {
            if (imgWidth > conWidth) {
                reImgHeight = conHeight;
                reImgWidth = imgWidth * reImgHeight / imgHeight;
            }
            else {
                reImgWidth = imgWidth;
                reImgHeight = imgHeight;
            }
        }
        reImgHeight= (int) (reImgHeight*0.55);
        reImgWidth= (int) (reImgWidth*0.75);
        icon = new ImageIcon(icon.getImage().getScaledInstance(reImgWidth, reImgHeight, Image.SCALE_DEFAULT));
        pic.setIcon(icon);
        pic.setHorizontalAlignment(SwingConstants.CENTER);
    }
}