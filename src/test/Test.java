package test;

import utils.TileConverter;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
//        TileConverter.gsToxyz("C:\\Users\\weijunbo\\Desktop\\tyjr_tyjr","C:\\Users\\weijunbo\\Desktop\\tyjr");
        TileConverter.tmsReverseY("E:\\中国_电子地图222\\瓦片_TMS","E:\\Demo\\Web\\china_darkBlue_map2");
    }
}
