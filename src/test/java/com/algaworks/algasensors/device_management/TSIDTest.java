package com.algaworks.algasensors.device_management;

import org.junit.jupiter.api.Test;

import com.algaworks.algasensors.device_management.common.IdGenerator;

import io.hypersistence.tsid.TSID;

public class TSIDTest {

    @Test
    public void shouldGenerateTSID() {

        TSID tsid = IdGenerator.generaTSID();

        System.out.println(tsid);
        System.out.println(tsid.toLong());
        System.out.println(tsid.getInstant());
    }

}
