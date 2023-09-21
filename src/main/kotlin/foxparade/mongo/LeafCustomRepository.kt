package foxparade.mongo

import com.mongodb.client.result.UpdateResult
import reactor.core.publisher.Mono

interface LeafCustomRepository {

    fun incrementUserLeaves(id: Long, leaves: Long): Mono<UpdateResult>
    fun decrementUserLeaves(id: Long, leaves: Long): Mono<UpdateResult>
}