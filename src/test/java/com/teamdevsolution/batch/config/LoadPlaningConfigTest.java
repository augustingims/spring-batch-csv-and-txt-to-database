package com.teamdevsolution.batch.config;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetup;
import com.teamdevsolution.batch.SmtpServerRule;
import com.teamdevsolution.batch.services.PlanningMailSenderService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBatchTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LoadPlaningConfigTest {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Mock
    private PlanningMailSenderService planningMailSenderService;

    @Rule
    public SmtpServerRule serverSmtp = new SmtpServerRule(2525);

    @Test
    @Sql("classpath:init-all-tables.sql")
    public void shouldSendPlanningsWithSuccess() throws MessagingException {

        JobExecution result = jobLauncherTestUtils.launchStep("planningStep");

        assertThat(result.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        //Mockito.verify(planningMailSenderService, Mockito.timeout(4)).send(ArgumentMatchers.any(),ArgumentMatchers.any());
        //assertThat(serverSmtp.getMessages()).hasSize(4);
        //assertThat(serverSmtp.getMessages()[0].getSubject()).isEqualTo("Votre planning de formations");
    }
}