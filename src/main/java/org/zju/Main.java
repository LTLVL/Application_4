package org.zju;

import org.zju.crawler.Crawl;
import org.zju.exception.MyException;
import org.zju.index.Index;
import org.zju.ui.CLI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, MyException {
        new CLI().start();
    }
}