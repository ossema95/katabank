package com.harington.katabank;

import java.io.ByteArrayInputStream;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.harington.katabank.model.Client;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	private static final String STEP = "step";
	private static final String IMPORT_CLIENT_JOB = "importClientJob";
	private static final String INSERT_CLIENT_BATCH = "INSERT INTO ClIENT (email, password, fullName, id,roles) VALUES (:email, :password, :fullName, :id, :roles)";
	@Autowired
	private DataSource dataSource;

	@Bean
	@StepScope
	public FlatFileItemReader<Client> reader(@Value("#{jobParameters['input.file.byte']}") byte[] filePath) {
		var reader = new FlatFileItemReader<Client>();
		var ressource = new InputStreamResource(new ByteArrayInputStream(filePath));
		reader.setResource(ressource);
		reader.setLineMapper(new DefaultLineMapper<Client>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(Client.EMAIL, Client.PASSWORD, Client.FULL_NAME, Client.ID, Client.ROLES);
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Client>() {
					{
						setTargetType(Client.class);
					}
				});

			}
		});
		return reader;
	}

	@Bean
	public JdbcBatchItemWriter<Client> writer() {
		JdbcBatchItemWriter<Client> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql(INSERT_CLIENT_BATCH);
		writer.setDataSource(dataSource);
		return writer;
	}

	@Bean
	public Job importUserJob(JobRepository jobRepository, Step step1) {
		return new JobBuilder(IMPORT_CLIENT_JOB, jobRepository).start(step1).build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
			FlatFileItemReader<Client> reader, JdbcBatchItemWriter<Client> writer,
			ItemProcessor<Client, Client> passwordEncodingProcessor) {
		return new StepBuilder(STEP, jobRepository).<Client, Client>chunk(3, transactionManager).reader(reader)
				.processor(passwordEncodingProcessor).writer(writer).build();
	}

	@Bean
	public ItemProcessor<Client, Client> passwordEncodingProcessor(PasswordEncoder encoder) {
		return new ItemProcessor<Client, Client>() {

			@Override
			public Client process(Client item) throws Exception {
				var password = encoder.encode(item.getPassword());
				item.setPassword(password);
				return item;
			}
		};
	}
}
