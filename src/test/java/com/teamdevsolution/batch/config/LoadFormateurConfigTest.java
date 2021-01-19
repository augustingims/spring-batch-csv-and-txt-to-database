package com.teamdevsolution.batch.config;

import com.teamdevsolution.batch.utils.QueryUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBatchTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LoadFormateurConfigTest {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldLoadFormateursWithSuccess() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchStep("loadFormateurStep", jobParameters);

        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        Integer count = jdbcTemplate.queryForObject(QueryUtils.COUNT_FORMATEURS_QUERY, Integer.class);

        assertThat(count).isEqualTo(16);

    }

}