package foxparade

import foxparade.mongo.EventRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = [EventRepository::class])
class FoxParadeApplication

fun main(args: Array<String>) {
	runApplication<FoxParadeApplication>(*args)
}
