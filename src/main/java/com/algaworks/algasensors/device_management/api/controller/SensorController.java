package com.algaworks.algasensors.device_management.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import com.algaworks.algasensors.device_management.api.model.SensorInput;
import com.algaworks.algasensors.device_management.api.model.SensorOutput;
import com.algaworks.algasensors.device_management.common.IdGenerator;
import com.algaworks.algasensors.device_management.domain.model.Sensor;
import com.algaworks.algasensors.device_management.domain.model.SensorId;
import com.algaworks.algasensors.device_management.domain.repository.SensorRepository;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;

    @PutMapping("/{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable("sensorId") TSID sensorId) {
        SensorId id = new SensorId(sensorId);
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));

        sensor.setEnabled(true);
        sensorRepository.saveAndFlush(sensor);
        
    }

    @DeleteMapping("/{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));
        sensor.setEnabled(false);
        sensorRepository.saveAndFlush(sensor);
    }

    @PutMapping("/{sensorId}")
    @ResponseStatus(HttpStatus.OK)
    public SensorOutput update(@PathVariable("sensorId") TSID sensorId, @RequestBody SensorInput input) {
        SensorId id = new SensorId(sensorId);
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));

        if (input.getName() != null) {
            sensor.setName(input.getName());
        }
        if (input.getIp() != null) {
            sensor.setIp(input.getIp());
        }
        if (input.getLocation() != null) {
            sensor.setLocation(input.getLocation());
        }
        if (input.getProtocol() != null) {
            sensor.setProtocol(input.getProtocol());
        }
        if (input.getModel() != null) {
            sensor.setModel(input.getModel());
        }

        return toOutput(sensorRepository.saveAndFlush(sensor));
    }

    @DeleteMapping("/{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sensorId") TSID sensorId) {
        SensorId id = new SensorId(sensorId);
        if (!sensorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found");
        }
        sensorRepository.deleteById(id);
    }

    @GetMapping
    public Page<SensorOutput> search(Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensors.map(this::toOutput);
    }

    @GetMapping("/{sensorId}")
    public SensorOutput getOne(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found"));

        return toOutput(sensor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.generaTSID()))
                .name(input.getName())
                .ip(input.getIp())
                .location(input.getLocation())
                .protocol(input.getProtocol())
                .model(input.getModel())
                .enabled(false)
                .build();

        sensor = sensorRepository.saveAndFlush(sensor);

                return toOutput(sensor);
    }

    private SensorOutput toOutput(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }

}
