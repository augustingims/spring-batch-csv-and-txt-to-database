package com.teamdevsolution.batch.config;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetup;
import com.teamdevsolution.batch.utils.QueryUtils;
import org.junit.Rule;
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
public class BatchConfigTest {

    @Rule
    public GreenMailRule serverSmtp = new GreenMailRule(new ServerSetup(2525, "localhost", ServerSetup.PROTOCOL_SMTP));

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldExecuteJobWithSuccess() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .addString("formationsFile", "classpath:inputs/formationsFile.xml")
                .addString("seancesFile", "classpath:inputs/seancesFile.csv")
                .toJobParameters();

        JobExecution result = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        Integer countSeances = jdbcTemplate.queryForObject(QueryUtils.COUNT_SEANCE_QUERY, Integer.class);
        assertThat(countSeances).isEqualTo(20);

        Integer countFormations = jdbcTemplate.queryForObject(QueryUtils.COUNT_FORMATIONS_QUERY, Integer.class);
        assertThat(countFormations).isEqualTo(4);

        Integer countFormateurs = jdbcTemplate.queryForObject(QueryUtils.COUNT_FORMATEURS_QUERY, Integer.class);
        assertThat(countFormateurs).isEqualTo(16);

        //assertThat(serverSmtp.getReceivedMessages()).hasSize(4);
       // assertThat(serverSmtp.getReceivedMessages()[0].getSubject()).isEqualTo("Votre planning de formations");
    }

}