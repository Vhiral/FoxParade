package foxparade.mongo

import foxparade.mongo.model.Toy
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ToyRepository : ReactiveMongoRepository<Toy, String> {
}