package org.zju.parser;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import org.zju.util.Base64Util;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 解析PDF
 * @author Tao
 * @date 2023/11/29
 */
public class Parser {


    /**
     * 通过pdf的url获取文本
     * @param url
     * @throws IOException
     */
    public String parsePDF(String url) throws IOException {
        //设置关闭日志
        java.util.logging.Logger
                .getLogger("org.apache.pdfbox").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger
                .getLogger("org.apache.fontbox").setLevel(java.util.logging.Level.OFF);
        //获取File
        File file = getFileByHttpURL(url);
        PDDocument doc = PDDocument.load(new FileInputStream(file) {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
        //获取第一页的数据
        PDFTextStripper text = new PDFTextStripper();
        //获取全文件的所有文本
        String FinalText = text.getText(doc);
        //关闭
        doc.close();
        return FinalText;
    }

    /**打印纲要**/
    @Test
    public void getPDFOutline(){
        try {
            // 加载PDF文件
            PDDocument document = PDDocument.load(getFileByHttpURL("https://aclanthology.org/2022.acl-long.1.pdf"));

            PDPageTree pageTree = document.getPages();

            // 遍历每个页面
            for (PDPage page : pageTree) {
                int pageNum = pageTree.indexOf(page) + 1;
                int count = 1;
                System.out.println("Page " + pageNum + ":");
                for (COSName xObjectName : page.getResources().getXObjectNames()) {

                    PDXObject pdxObject = page.getResources().getXObject(xObjectName);
                    if (pdxObject instanceof PDImageXObject) {
                        PDImageXObject image = (PDImageXObject) pdxObject;
                        System.out.println("Found image with width "
                                + image.getWidth()
                                + "px and height "
                                + image.getHeight()
                                + "px.");
                        String fileName = "one-more-" + pageNum + "-" + count + ".jpg";
                        ImageIO.write(image.getImage(), "jpg", new File(fileName));
                        count++;
                    }
                }
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getbase64Photos() {
        File file = getFileByHttpURL("https://aclanthology.org/2022.acl-long.1.pdf");
        int startIndex = 1;
        int endIndex = 10;
        List<String> photos = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file)) {
            //TODO 下标从0开始，所以-1
            for (int i = startIndex - 1; i < endIndex; i++) {
                PDPage pdfpage = document.getPage(i);
                // get resource of pdf
                PDResources pdResources = pdfpage.getResources();
                Iterable<COSName> xObjectNames = pdResources.getXObjectNames();
                Iterator<COSName> iterator = xObjectNames.iterator();
                while (iterator.hasNext()) {
                    PDXObject o = pdResources.getXObject(iterator.next());
                    if (o instanceof PDImageXObject) {
                        //得到BufferedImage对象
                        BufferedImage image = ((PDImageXObject) o).getImage();
                        String base64img = Base64Util.convertimgtoBase64(image);
                        // 可以打印到本地，查看输出顺序
                        //String imglocation = "C:\\CER\\AE EMC lab_Report template\\pdf img by page\\";
                        //File imgfile = new File(imglocation + StringUtil.get32UUID() + ".png");
                        //ImageIO.write(image, "png", imgfile);

                        photos.add("data:image/jpg;base64," + base64img);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return photos;
    }


    /**
     * 通过url获取File
     * @param path
     * @return {@link File}
     */
    private static File getFileByHttpURL(String path){
        String newUrl = path.split("[?]")[0];
        String[] suffix = newUrl.split("/");
        //得到最后一个分隔符后的名字
        String fileName = suffix[suffix.length - 1];
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            file = File.createTempFile("report",fileName);//创建临时文件
            URL urlFile = new URL(newUrl);
            inputStream = urlFile.openStream();
            outputStream = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead=inputStream.read(buffer,0,8192))!=-1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
