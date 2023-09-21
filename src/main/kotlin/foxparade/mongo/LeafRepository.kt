package foxparade.mongo

import foxparade.mongo.model.Leaf
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface LeafRepository : ReactiveMongoRepository<Leaf, Long>, LeafCustomRepository {
}