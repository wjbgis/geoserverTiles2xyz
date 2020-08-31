package utils;

import java.io.*;
import java.nio.channels.FileChannel;

public class TileConverter {
    /**
     * geoServer EPSG_900913切片转google xyz 切片(ArcGIS， Google map， gaode)
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     */
    public static void gsToxyz(String inputPath, String outputPath) throws IOException {
        File gsFile = new File(inputPath);
        File[] zFiles = gsFile.listFiles();    // 获取所有的Z
        for (File gsZ: zFiles) {
            // Z
            String gsZName = gsZ.getName();
            String z = gsZName.split("_")[2];
            z = Integer.toString(Integer.parseInt(z));//转换后的Z

            // X和Y
            for (File gsTemp: gsZ.listFiles()) {
                for (File gsX_Y : gsTemp.listFiles()) {
                    String gsXYName = gsX_Y.getName();
                    String x = gsXYName.split("_")[0];
                    String y = gsXYName.split("_")[1];
                    x = Integer.toString(Integer.parseInt(x)); // x是文件夹
                    y = y.substring(0,y.lastIndexOf("."));// y是文件,需要提取出文件名

                    //重新计算y ----newY = (2^n-1)-y
                    int newY =(int)Math.pow(2,Integer.parseInt(z))-1-Integer.parseInt(y);  //

                    String oldPath = gsX_Y.getAbsolutePath();
                    String newFileName = newY +".pbf";
                    String newPath = outputPath + "\\" + z + "\\" + x + "\\" + newFileName;
                    File oldFile = new File(oldPath);
                    File newFile = new File(newPath);
                    if(!(newFile.getParentFile().exists())){
                        newFile.getParentFile().mkdirs();
                    }
                    if(newFile.exists()){       //如果存在这个文件就删除，否则就创建
                        newFile.delete();
                    }else{
                        newFile.createNewFile();
                    }
                    copyFileUsingFileChannels(oldFile, newFile);

                }
            }
        }
        System.out.println("OK");
    }

    // TMS切片y的编号翻转
    public static void tmsReverseY(String inputPath, String outputPath) throws IOException {
        File tmsFile = new File(inputPath);
        File[] zFiles = tmsFile.listFiles();    // 获取所有的Z
        for (File zFile: zFiles) {
            // z
            String zName = zFile.getName();
            System.out.println(zName);
            // x
            for (File xFile: zFile.listFiles()) {
                String xName = xFile.getName();
                //y
                for (File yFile : xFile.listFiles()) {
                    String yName = yFile.getName();
//                    System.out.println(yName);
                    String[] temp = yName.split("[.]");
                    String yPrefix = temp[0];//名称
                    String ySuffix = temp[1];//后缀
                    //重新计算y ----newY = (2^n-1)-y
                    int newY =(int)Math.pow(2,Integer.parseInt(zName))-1-Integer.parseInt(yPrefix);
                    String newYName = newY+"."+ySuffix;


                    String oldPath = yFile.getAbsolutePath();
                    String newPath = outputPath + "\\" + zName + "\\" + xName + "\\" + newYName;
                    File oldFile = new File(oldPath);
                    File newFile = new File(newPath);
                    if(!(newFile.getParentFile().exists())){
                        newFile.getParentFile().mkdirs();
                    }
                    if(newFile.exists()){       //如果存在这个文件就删除，否则就创建
                        newFile.delete();
                    }else{
                        newFile.createNewFile();
                    }
                    copyFileUsingFileChannels(oldFile, newFile);

                }
            }
        }
        System.out.println("OK");
    }

    //文件复制
    private static void copyFileUsingFileChannels(File source, File dest)
            throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}
