package com.github.rafaelsouzaf.app;

import hello.JobCompletionNotificationListener;
import hello.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.KeyValueItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.Map;

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

//    @Autowired
//    public DataSource dataSource;

    @Value("data/json/*.json")
    private Resource[] resources;

    @Bean
    public Job job01() {
        Job job = jobBuilderFactory.get("job-01")
                .listener(new JobExecutionListenerSupport() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("---------------FINISHED: " + jobExecution.getJobInstance().getJobName());
                    }
                })
                .flow(step01())
                .end()
                .build();
        return job;
    }

    @Bean
    public Step step01() {



        return stepBuilderFactory.get("step-01")
                .chunk(10)
                .reader(reader())
//                .processor(null)
                .writer(writer())
                .build();
    }

    @Bean
    public MultiResourceItemReader<Map<String, Object>> reader() {

//        CustomItemReader reader = new CustomItemReader<String>();

        FlatFileItemReader readerItem = new FlatFileItemReader();
        readerItem.setLineMapper(new JsonLineMapper());

        MultiResourceItemReader<Map<String, Object>> reader = new MultiResourceItemReader<Map<String, Object>>();
        reader.setResources(resources);
        reader.setDelegate(readerItem);

        return reader;
    }

    @Bean
    public FlatFileItemWriter<Object> writer() {
        FlatFileItemWriter<Object> writer = new FlatFileItemWriter<Object>();
        writer.setResource(new FileSystemResource("output/domain.all.csv"));
        writer.setLineAggregator(new LineAggregator<Object>() {
            @Override
            public String aggregate(Object stringObjectMap) {
                return stringObjectMap.toString() + "-lerolero";
            }
        });
        return writer;
    }

//    @Bean
//    public JdbcBatchItemWriter<Person> writer2() {
//        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
//        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
//        writer.setDataSource(dataSource);
//        log.info("Inserting people: ...");
//        return writer;
//    }
}
