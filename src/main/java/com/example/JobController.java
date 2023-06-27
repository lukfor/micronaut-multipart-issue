package com.example;

import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA;
import static io.micronaut.http.MediaType.TEXT_PLAIN;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedPart;
import io.micronaut.http.server.multipart.MultipartBody;
import reactor.core.publisher.Mono;

@Controller
public class JobController {


	@Post(value = "/submit", consumes = MULTIPART_FORM_DATA, produces = TEXT_PLAIN)
    @SingleResult
    public Publisher<String> submit(@Body MultipartBody body) {

        return Mono.create(emitter -> {
            body.subscribe(new Subscriber<CompletedPart>() {
                private Subscription s;

                @Override
                public void onSubscribe(Subscription s) {
                    this.s = s;
                    System.out.println("Subscribed.");
                    s.request(1);
                }

                @Override
                public void onNext(CompletedPart completedPart) {
                    // Business logic that parses completedPart based on class
                	System.out.println("OnNext: " + completedPart.getName() + " (Class: " + completedPart.getClass() + ")");
                    s.request(1);
                }

                @Override
                public void onError(Throwable t) {
                    emitter.error(t);
                }

                @Override
                public void onComplete() {
                    emitter.success("Uploaded");
                }
            });
        });
    }

}
