
package com.plantynet.webflux.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.plantynet.webflux.vo.AppLogDocument;

import reactor.core.publisher.Flux;

@Repository
public interface MongoDbRepository extends ReactiveMongoRepository<AppLogDocument, String> {

	@Override
	Flux<AppLogDocument> findAll();

	Flux<AppLogDocument> findBySrcIp(String srcIp); //  조건이 있는 조회 메소드

	@Query("{'srcIp': {$regex: ?0 }}")
	Flux<AppLogDocument> findRegexBySrcIp(String srcIp); //  like 모양의 조회 메소드

	@Query("{'srcIp': {$regex: ?0 }}")
	Flux<AppLogDocument> findRegexPagingBySrcIp(String srcIp, Pageable page); //  페이징 처리가 가능한 조회 메소드

	// pipeline에 aggregate와 관련된 내용을 추가하여준다.
	// aggregate와 관련된 문법은 MongoDB 문법과 동일하게 써 주면 된다.
	public static final String match = "{ $match : { srcIp : {$regex : ?0 }, size : {$gte:?1} } }";
	//public static final String group = "{ $group :  {  _id:null, counts:{$sum : 1}, total:{$sum : '$number'}, texts:{$addToSet : '$text' } }    }";
	public static final String sort = "{ $sort : {_id:-1} }";

	@Aggregation(pipeline = { match, sort })
	Flux<AppLogDocument> aggregateSrcIp(String srcIp, int size); // aggregation 조회 메소드

}
