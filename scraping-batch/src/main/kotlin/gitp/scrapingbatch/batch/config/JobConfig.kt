package gitp.scrapingbatch.batch.config

import gitp.scrapingbatch.batch.component.DptGroupRequestAndPersistTasklet
import gitp.scrapingbatch.batch.component.DptRequestAndPersistTasklet
import gitp.type.Semester
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager
import java.time.Year

@Configuration
class JobConfig {
    @Bean
    fun dptGroupPersistJob(
        jobRepository: JobRepository,
        dptGroupRequestStep: Step
    ): Job {
        return JobBuilder("dptGroupPersistJob", jobRepository)
            .start(dptGroupRequestStep)
            .build()
    }

    // TODO: add type conversion to jobParameters
    @JobScope
    @Bean
    fun dptGroupRequestStep(
        dptGroupRequestAndPersistTasklet: Tasklet,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("dptGroupRequestStep", jobRepository)
            .tasklet(dptGroupRequestAndPersistTasklet, transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun dptGroupRequestAndPersistTasklet(
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        jdbcTemplate: JdbcTemplate
    ): Tasklet {
        return DptGroupRequestAndPersistTasklet(
            Year.parse(year),
            Semester.codeOf(semester.toInt()),
            jdbcTemplate
        )
    }

    @Bean
    fun dptRequestAndPersistJob(
        jobRepository: JobRepository,
        dptRequestAndPersistStep: Step
    ): Job {
        return JobBuilder("dptRequestAndPersistJob", jobRepository)
            .start(dptRequestAndPersistStep)
            .build()
    }

    @JobScope
    @Bean
    fun dptRequestAndPersistStep(
        dptRequestAndPersistTasklet: Tasklet,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("dptRequestAndPersistStep", jobRepository)
            .tasklet(dptRequestAndPersistTasklet, transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun dptRequestAndPersistTasklet(
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        jdbcTemplate: JdbcTemplate
    ): Tasklet {
        return DptRequestAndPersistTasklet(
            jdbcTemplate
        )
    }
}