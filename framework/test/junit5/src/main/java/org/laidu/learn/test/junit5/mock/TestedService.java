package org.laidu.learn.test.junit5.mock;

import lombok.extern.slf4j.Slf4j;

/**
 * 将要被测试的service
 */
@Slf4j
class TestedService {

    void open(String service) throws InterruptedException {

        Thread.sleep(1000);

        log.info("-*--*--*--*--*--*--*--*--*-- 打开服务 --*--*--*--*--*--*--*--*--*-: {}", service);
    }

    void call(String service){

        log.info("-*--*--*--*--*--*--*--*--*-- 调用服务 --*--*--*--*--*--*--*--*--*-: {}", service);
    }
}
