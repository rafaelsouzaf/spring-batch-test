package com.github.rafaelsouzaf.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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

    @Value("data/json/*.json")
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

        MultiResourceItemReader<String> reader = new MultiResourceItemReader<String>();
        reader.setResources(resources);
        reader.setDelegate(reader());

        return stepBuilderFactory.get("step-01")
                .chunk(10)
                .reader(reader)
//                .processor(null)
                .writer(writer())
                .build();
    }

    @Bean
    public FlatFileItemReader reader() {
        FlatFileItemReader reader = new FlatFileItemReader();
        reader.setLineMapper(new JsonLineMapper() {

        });
//        reader.setLineMapper(new DefaultLineMapper() {{
//            setLineTokenizer(new DelimitedLineTokenizer() {{
//                setNames(new String[]{"patente", "marca", "modelo", "color", "fecha"});
//            }});
//            setFieldSetMapper(new BeanWrapperFieldSetMapper<String>() {{
//            }});
//        }});
        return reader;
    }

    @Bean
    public FlatFileItemWriter writer() {
        FlatFileItemWriter writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/domain.all.csv"));
        writer.setLineAggregator(new DelimitedLineAggregator() {{
            setDelimiter(",");
//            setFieldExtractor(new BeanWrapperFieldExtractor() {{
//                setNames(new String[]{"id", "domain"});
//            }});
        }});
        return writer;
    }

}
