package kml.test;
import de.micromata.opengis.kml.v_2_2_0.*;


import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/13 10:19
 */
public class ParsingKmlUtil {
    /**
     * 解析kml文件
     */
    public KmlData parseKmlByFile(File file) {
        Kml kml = Kml.unmarshal(file);
        return getByKml(kml);
    }

    /**
     * 解析kml文件流
     * @param inputstream
     * @return
     */
    public KmlData parseKmlByInputstream(InputStream inputstream) {
        Kml kml = Kml.unmarshal(inputstream);
        return getByKml(kml);
    }

    /**
     * Kml对象转自定义存储对象
     * @param kml
     * @return
     */
    private KmlData getByKml(Kml kml){
        KmlData kmlData = new KmlData();
        kmlData.setKmlPoints(new ArrayList<>());
        kmlData.setKmlLines(new ArrayList<>());
        kmlData.setKmlPolygons(new ArrayList<>());
        Feature feature = kml.getFeature();
        parseFeature(feature, kmlData);
        return kmlData;
    }

    /**
     * 解析kml节点
     * @param feature
     * @param kmlData
     */
    private void parseFeature(Feature feature, KmlData kmlData) {
        if (feature != null) {
            if (feature instanceof Document) {
                List<Feature> featureList = ((Document) feature).getFeature();
                featureList.forEach(documentFeature -> {
                            if (documentFeature instanceof Placemark) {
                                getPlaceMark((Placemark) documentFeature, kmlData);
                            } else {
                                parseFeature(documentFeature, kmlData);
                            }
                        }
                );
            } else if (feature instanceof Folder) {
                List<Feature> featureList = ((Folder) feature).getFeature();
                featureList.forEach(documentFeature -> {
                            if (documentFeature instanceof Placemark) {
                                getPlaceMark((Placemark) documentFeature, kmlData);
                            }else{
                                parseFeature(documentFeature, kmlData);
                            }
                        }
                );
            }
        }
    }

    private void getPlaceMark(Placemark placemark, KmlData kmlData) {
        Geometry geometry = placemark.getGeometry();
        String name = placemark.getName();
        if(name==null){
            name=placemark.getDescription();
        }
        parseGeometry(name, geometry, kmlData);
    }

    /**
     * 解析点线面形状的数据分别放入存储对象
     * @param name 形状名称
     * @param geometry 形状类型
     * @param kmlData 存储对象
     */
    private void parseGeometry(String name, Geometry geometry, KmlData kmlData) {
        if (geometry != null) {
            if (geometry instanceof Polygon) {
                Polygon polygon = (Polygon) geometry;
                Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
                if (outerBoundaryIs != null) {
                    LinearRing linearRing = outerBoundaryIs.getLinearRing();
                    if (linearRing != null) {
                        List<Coordinate> coordinates = linearRing.getCoordinates();
                        if (coordinates != null) {
                            outerBoundaryIs = ((Polygon) geometry).getOuterBoundaryIs();
                            addPolygonToList(kmlData.getKmlPolygons(), name, outerBoundaryIs);
                        }
                    }
                }
            } else if (geometry instanceof LineString) {
                LineString lineString = (LineString) geometry;
                List<Coordinate> coordinates = lineString.getCoordinates();
                if (coordinates != null) {
                    coordinates = ((LineString) geometry).getCoordinates();
                    addLineStringToList(kmlData.getKmlLines(), coordinates, name);
                }
            } else if (geometry instanceof Point) {
                Point point = (Point) geometry;
                List<Coordinate> coordinates = point.getCoordinates();
                if (coordinates != null) {
                    coordinates = ((Point) geometry).getCoordinates();
                    addPointToList(kmlData.getKmlPoints(), coordinates, name);
                }
            } else if (geometry instanceof MultiGeometry) {
                List<Geometry> geometries = ((MultiGeometry) geometry).getGeometry();
                for (Geometry geometryToMult : geometries) {
                    Boundary outerBoundaryIs;
                    List<Coordinate> coordinates;
                    if (geometryToMult instanceof Point) {
                        coordinates = ((Point) geometryToMult).getCoordinates();
                        addPointToList(kmlData.getKmlPoints(), coordinates, name);
                    } else if (geometryToMult instanceof LineString) {
                        coordinates = ((LineString) geometryToMult).getCoordinates();
                        addLineStringToList(kmlData.getKmlLines(), coordinates, name);
                    } else if (geometryToMult instanceof Polygon) {
                        outerBoundaryIs = ((Polygon) geometryToMult).getOuterBoundaryIs();
                        addPolygonToList(kmlData.getKmlPolygons(), name, outerBoundaryIs);
                    }
                }
            }
        }
    }

    /**
     * 保存面状数据
     * @param kmlPolygonList 已有面状数据
     * @param name 面状名称
     * @param outerBoundaryIs 面状信息
     */
    private void addPolygonToList(List<KmlPolygon> kmlPolygonList, String name, Boundary outerBoundaryIs) {
        LinearRing linearRing = outerBoundaryIs.getLinearRing();//面
        KmlPolygon kmlPolygon = new KmlPolygon();
        kmlPolygon.setPoints(linearRing.getCoordinates());
        kmlPolygon.setName(name);
        kmlPolygonList.add(kmlPolygon);
    }

    /**
     * 保存线状数据
     * @param kmlLineList 已有线状数据
     * @param coordinates 线状经纬度数据
     * @param name 线状名称
     */
    private void addLineStringToList( List<KmlLine> kmlLineList, List<Coordinate> coordinates, String name) {
        KmlLine kmlLine = new KmlLine();
        kmlLine.setPoints(coordinates);
        kmlLine.setName(name);
        kmlLineList.add(kmlLine);
    }


    /**
     * 保存点状数据
     * @param kmlPointList 已有点状数据
     * @param coordinates 点状经纬度数据
     * @param name 点状名称
     */
    private void addPointToList(List<KmlPoint> kmlPointList, List<Coordinate> coordinates, String name) {
        KmlPoint kmlPoint = new KmlPoint();
        kmlPoint.setName(name);
        kmlPoint.setPoints(coordinates);
        kmlPointList.add(kmlPoint);
    }


}
