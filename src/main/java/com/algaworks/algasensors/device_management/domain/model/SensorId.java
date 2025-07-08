package com.algaworks.algasensors.device_management.domain.model;

import java.io.Serializable;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SensorId implements Serializable {

    private TSID value;

    public SensorId(TSID value) {
        this.value = value;
    }

    public SensorId(String value) {
        this.value = TSID.from(value);
    }

    public SensorId(Long value) {
        this.value = TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
