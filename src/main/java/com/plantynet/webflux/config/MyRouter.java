package com.plantynet.webflux.config;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.plantynet.webflux.dao.MongoDbRepository;
import com.plantynet.webflux.vo.AppLogDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class MyRouter {

	@Autowired
	MongoDbRepository db;

	@Autowired
	DynamicDocumentName dynamicDocumentName;

	@Bean
	public RouterFunction<ServerResponse> find() {
		final RequestPredicate predicate = RequestPredicates.GET("/find");

		// https://lts0606.tistory.com/312?category=761939
		// https://godekdls.github.io/Reactive%20Spring/springwebflux/

		RouterFunction<ServerResponse> response = RouterFunctions
				.route(predicate, (request) -> {
			// http://127.0.0.1:8080/find
			dynamicDocumentName.setDocumnetName("web_log_2021-01-12");

			Flux<AppLogDocument> mapper = db.aggregateSrcIp("218.234.77.223", 0);

			//db.findAll();
			//db.findBySrcIp("218.234.77.88").collectList().subscribe(System.out::println); // 만들어준 단일 검색
			//db.findRegexBySrcIp(".*218.*").collectList().subscribe(System.out::println); // 만들어준 like 검색

			//Pageable page = PageRequest.of(0, 2);
			//db.findRegexPagingBySrcIp(".*218.*", page).collectList().subscribe(System.out::println);
			//db.aggregateSrcIp("218.234.77.223", 0);


			Mono<ServerResponse> res = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromProducer(mapper, AppLogDocument.class));
			return res;
		});

		return response;
	}


	@Bean
	public RouterFunction<ServerResponse> requestGetParam() {
		RequestPredicate predicate = RequestPredicates.GET("/request").and(RequestPredicates.accept(MediaType.TEXT_PLAIN));

		RouterFunction<ServerResponse> response = RouterFunctions.route(predicate, (request) -> {

			// http://127.0.0.1:8080/request?ip=218.234.77.223&collectionName=web_log_2021-01-12
			System.out.println(request.queryParams().get("ip"));
			System.out.println(request.queryParams().get("collectionName"));
			System.out.println(request.pathVariables()); //이건뭐지

			List<String> ip = request.queryParams().get("ip");
			List<String> collectionName = request.queryParams().get("collectionName");


			dynamicDocumentName.setDocumnetName(collectionName.get(0)); // 배열로 받네..

			Flux<AppLogDocument> mapper = db.aggregateSrcIp(ip.get(0), 0); // 배열로 받네..

			//db.findAll();
			//db.findBySrcIp("218.234.77.88").collectList().subscribe(System.out::println); // 만들어준 단일 검색
			//db.findRegexBySrcIp(".*218.*").collectList().subscribe(System.out::println); // 만들어준 like 검색

			//Pageable page = PageRequest.of(0, 2);
			//db.findRegexPagingBySrcIp(".*218.*", page).collectList().subscribe(System.out::println);
			//db.aggregateSrcIp("218.234.77.223", 0);


			Mono<ServerResponse> res = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromProducer(mapper, AppLogDocument.class));

			AtomicReference<Long> startTime = new AtomicReference<Long>();
			return res.doOnSubscribe(u -> startTime.set(System.nanoTime()))
					.doFinally(t -> System.out.println("소요시간(ms) Elapsed time: " + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime.get()))));
		});


		return response;
	}
}
