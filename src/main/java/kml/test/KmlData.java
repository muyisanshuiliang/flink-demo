package kml.test;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/13 10:18
 */
@Data
public class KmlData {
    private List<KmlPoint> kmlPoints;
    private List<KmlLine> kmlLines;
    private List<KmlPolygon> kmlPolygons;
}
