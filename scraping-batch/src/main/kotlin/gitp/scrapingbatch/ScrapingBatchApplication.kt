package gitp.scrapingbatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["gitp.entity"])
class ScrapingBatchApplication

fun main(args: Array<String>) {
    runApplication<ScrapingBatchApplication>(*args)
}
