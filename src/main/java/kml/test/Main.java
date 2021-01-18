package kml.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/13 10:20
 */
public class Main {
    public static void main(String[] args) {
        KmlData kmlData = getKmlData();
        if (kmlData.getKmlPoints().size() > 0) {
            System.out.println("点====");
            for (KmlPoint k : kmlData.getKmlPoints()) {
                System.out.println("点【" + k.getName() + "】        ====》       " + k.getPoints());
            }
        }
        if (kmlData.getKmlLines().size() > 0) {
            System.out.println("线====");
            for (KmlLine k : kmlData.getKmlLines()) {
                System.out.println("线【" + k.getName() + "】        ====》       " + k.getPoints());
            }
        }
        if (kmlData.getKmlPolygons().size() > 0) {
            System.out.println("面====");
            for (KmlPolygon k : kmlData.getKmlPolygons()) {
                System.out.println("面【" + k.getName() + "】        ====》       " + k.getPoints());
            }
        }
    }

    public static KmlData getKmlData() {
        KmlData kmlData = null;
        ParsingKmlUtil parsingKmlUtil = new ParsingKmlUtil();
        File file = new File("src/金马河、沱江.kml"); //文件地址自己修改
        try {
            kmlData = parsingKmlUtil.parseKmlByInputstream(new FileInputStream(file));
            assert kmlData != null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return kmlData;
    }
}
