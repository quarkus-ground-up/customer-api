package com.redhat.configuration;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class MicrometerConfiguration {

    @Produces
    @ApplicationScoped
    public MeterFilter enableHistogram() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if (id.getName().startsWith("http.server.requests")) {
                    return DistributionStatisticConfig.builder()
                            .percentiles(0.5, 0.95, 0.99, 0.999)
                            .build()
                            .merge(config);
                }
                return config;
            }
        };
    }

}