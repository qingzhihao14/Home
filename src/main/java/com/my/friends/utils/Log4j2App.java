package com.my.friends.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;

/**
 * @Atuhor: qin
 * @Create: 2022-02-08-20-43
 * @Time: 20:43
 * @Description:
 */
public class Log4j2App {
    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) throws IOException {
        File file  =new File("D:\\Home\\Home\\src\\main\\resources\\simple-log4j.xml");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        final ConfigurationSource source = new ConfigurationSource(in);
        Configurator.initialize(null, source);
        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        logger.fatal("fatal message");
    }
}