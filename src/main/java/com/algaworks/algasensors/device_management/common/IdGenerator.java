package com.algaworks.algasensors.device_management.common;

import io.github.cdimascio.dotenv.Dotenv;
import io.hypersistence.tsid.TSID;

public class IdGenerator {

    private static final TSID.Factory tsidFactory;

    static {

        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

        System.setProperty("tsid.node", dotenv.get("tsid.node", "0"));
        System.setProperty("tsid.node.count", dotenv.get("tsid.node.count", "1"));

        tsidFactory = TSID.Factory.builder().build();
    }

    private IdGenerator() {
        // Private constructor to prevent instantiation
    }

    public static TSID generaTSID() {
        return tsidFactory.generate();
    }

}