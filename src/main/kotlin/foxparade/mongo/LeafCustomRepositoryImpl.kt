package foxparade.mongo

import com.mongodb.client.result.UpdateResult
import foxparade.mongo.model.Leaf
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Mono

class LeafCustomRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : LeafCustomRepository {

    override fun incrementUserLeaves(id: Long, leaves: Long): Mono<UpdateResult> {
        val update = Update().inc("leaves", leaves)
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))

        return reactiveMongoTemplate.upsert(query, update, Leaf::class.java)
    }

    override fun decrementUserLeaves(id: Long, leaves: Long): Mono<UpdateResult> {
        val update = Update().inc("leaves", -leaves)
        val query = Query().addCriteria(Criteria.where("_id").`is`(id))

        return reactiveMongoTemplate.upsert(query, update, Leaf::class.java)
    }

}