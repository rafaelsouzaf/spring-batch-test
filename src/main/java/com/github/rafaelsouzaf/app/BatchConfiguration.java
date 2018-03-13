package com.github.rafaelsouzaf.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Created by rsouza on 13-03-18.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Value("csv/inputs/domain*.csv")
    private Resource[] resources;

    @Bean
    public Job job01() {
        Job job = jobBuilderFactory.get("job-01")
                .flow(step01())
                .end()
                .build();
        return job;
    }

    @Bean
    public Step step01() {

        MultiResourceItemReader reader = new MultiResourceItemReader();
        reader.setResources(resources);

        return stepBuilderFactory.get("step-01")
                .chunk(10)
                .reader(reader)
                .processor(null)
                .writer(null)
                .build();
    }




}
