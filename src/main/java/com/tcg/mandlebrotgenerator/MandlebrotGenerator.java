package com.tcg.mandlebrotgenerator;

import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomParams;
import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomThread;
import com.tcg.mandlebrotgenerator.util.ThreadManager;

public class MandlebrotGenerator {

    public static void main(String[] args) {
        new ThreadManager(-1.2, 0.25, 4, 0.5, 1000, "test", 100);
    }

}
