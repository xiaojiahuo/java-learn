package org.laidu.crawler;

import org.laidu.crawler.spider.CrawlerBootStrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.Spider;

import java.util.List;

/**
 * crawler stater
 *
 * @author tiancai.zang
 * 2018-01-09 15:28.
 */
@SpringBootApplication
public class App {

    @Autowired
    private  ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}