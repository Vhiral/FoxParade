package foxparade.mongo

import foxparade.mongo.model.Cooldown
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CooldownRepository : ReactiveMongoRepository<Cooldown, Cooldown.CooldownId> {

}