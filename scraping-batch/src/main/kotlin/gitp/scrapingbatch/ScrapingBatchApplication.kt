package gitp.scrapingbatch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScrapingBatchApplication

fun main(args: Array<String>) {
    runApplication<ScrapingBatchApplication>(*args)
}
