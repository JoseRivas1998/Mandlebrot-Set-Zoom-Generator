package com.tcg.mandlebrotgenerator;

import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomParams;
import com.tcg.mandlebrotgenerator.mandlebrot.MandlebrotZoomThread;
import com.tcg.mandlebrotgenerator.util.ThreadManager;

public class MandlebrotGenerator {

    public static void main(String[] args) {
        new ThreadManager(-0.6506400014677644, 0.372903024192037, 4, 0.95, 1000, "test", 700);
    }

}
