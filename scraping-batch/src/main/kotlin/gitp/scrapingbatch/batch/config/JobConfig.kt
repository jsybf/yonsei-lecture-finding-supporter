package gitp.scrapingbatch.batch.config

import gitp.scrapingbatch.batch.component.*
import gitp.scrapingbatch.repository.*
import gitp.scrapingbatch.service.LectureResponsePersistService
import gitp.type.Semester
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        dptGroupRepository: DptGroupRepository,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("dptGroupRequestStep", jobRepository)
            .tasklet(
                DptGroupRequestAndPersistTasklet(
                    Year.parse(year),
                    Semester.codeOf(semester.toInt()),
                    dptGroupRepository
                ), transactionManager
            )
            .build()
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
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        dptGroupRepository: DptGroupRepository,
        dptRepository: DptRepository,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("dptRequestAndPersistStep", jobRepository)
            .tasklet(
                DptRequestAndPersistTasklet(
                    Year.parse(year),
                    Semester.codeOf(semester.toInt()),
                    dptGroupRepository,
                    dptRepository
                ), transactionManager
            )
            .build()
    }

    // TODO: remove unused params
    @Bean
    fun lectureRequestAndPersistJob(
        jobRepository: JobRepository,
        lectureRequestAndPersistStep: Step
    ): Job {
        return JobBuilder("lectureRequestAndPersistJob", jobRepository)
            .start(lectureRequestAndPersistStep)
            .build()
    }

    @JobScope
    @Bean
    fun lectureRequestAndPersistStep(
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        jobRepository: JobRepository,
        dptRepository: DptRepository,
        lectureResponsePersistService: LectureResponsePersistService,
        objectMappingErrorRepository: ObjectMappingErrorRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("lectureRequestAndPersistStep", jobRepository)
            .tasklet(
                LectureRequestAndPersistTasklet(
                    Year.parse(year),
                    Semester.codeOf(semester.toInt()),
                    dptRepository,
                    lectureResponsePersistService,
                    objectMappingErrorRepository,
                ), transactionManager
            )
            .build()
    }

    @Bean
    fun mileageRequestAndPersistJob(
        jobRepository: JobRepository,
        mileageRequestAndPersistStep: Step
    ): Job {
        return JobBuilder("mileageRequestAndPersistJob", jobRepository)
            .start(mileageRequestAndPersistStep)
            .build()
    }

    @JobScope
    @Bean
    fun mileageRequestAndPersistStep(
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        jobRepository: JobRepository,
        lectureRepository: LectureRepository,
        mileageRankRepository: MileageRankRepository,
        objectMappingErrorRepository: ObjectMappingErrorRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("mileageRequestAndPersistStep", jobRepository)
            .tasklet(
                MileageRankRequestAndPersistTasklet(
                    Year.parse(year),
                    Semester.codeOf(semester.toInt()),
                    lectureRepository,
                    mileageRankRepository,
                    objectMappingErrorRepository,
                ), transactionManager
            )
            .build()
    }

    @Bean
    fun mileageSummaryRequestAndPersistJob(
        jobRepository: JobRepository,
        mileageSummaryRequestAndPersistStep: Step
    ): Job {
        return JobBuilder("mileageSummaryRequestAndPersistJob", jobRepository)
            .start(mileageSummaryRequestAndPersistStep)
            .build()
    }

    @JobScope
    @Bean
    fun mileageSummaryRequestAndPersistStep(
        @Value("#{jobParameters[year]}")
        year: String,
        @Value("#{jobParameters[semester]}")
        semester: String,
        jobRepository: JobRepository,
        lectureRepository: LectureRepository,
        mileageSummaryRepository: MileageSummaryRepository,
        objectMappingErrorRepository: ObjectMappingErrorRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("mileageSummaryRequestAndPersistStep", jobRepository)
            .tasklet(
                MileageSummaryRequestAndPersistTasklet(
                    Year.parse(year),
                    Semester.codeOf(semester.toInt()),
                    lectureRepository,
                    mileageSummaryRepository,
                    objectMappingErrorRepository,
                ), transactionManager
            )
            .build()
    }

}