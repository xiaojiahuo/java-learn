package org.laidu.learn.design.pattern.factory;

import lombok.extern.slf4j.Slf4j;

/**
 * 红色产品
 * <p>
 * Created by 臧天才 on 2017-09-01 18:17.
 */
@Slf4j
public class ConcreteBlueProduct extends AbstractProduct {

    ConcreteBlueProduct() {
        
        super(ColorType.RED);
    }
}
