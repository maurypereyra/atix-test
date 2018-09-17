package com.atix.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Monitor {
    private static final Logger LOGGER = Logger.getLogger(Monitor.class);

    private BlockingQueue<Double> measurements = new LinkedBlockingQueue<>();
    @Value("amplitude.max")
    private Double maximumAmplitude;
    @Value("average.min")
    private Double minimunAverage;

    private Double maxValue;
    private Double minValue;

    private BigDecimal totalInputs = new BigDecimal(0);
    private BigDecimal total = new BigDecimal(0);

    public void register(Double measurement) throws InterruptedException {
        measurements.put(measurement);

    }

    public void process() {
        LOGGER.info("Processing");
        List<Double> toProcess = new ArrayList<>();
        measurements.drainTo(toProcess);
        totalInputs.add(new BigDecimal(toProcess.size()));
        totalInputs.add(new BigDecimal(toProcess.stream().mapToDouble(Double::valueOf).sum()));
        Double avg = getAvg();
        Double max = toProcess.stream().max(Comparator.naturalOrder()).get();
        Double min = toProcess.stream().min(Comparator.naturalOrder()).get();


    }

    private double getAvg() {
        return total.divide(totalInputs).doubleValue();
    }
}
