package shoppingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
public class ShoppingServiceResource implements ApplicationListener<ApplicationReadyEvent> {
    private static DAO dao;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        dao = new DAO();
    }

    @GetMapping("/cart/{mail}")
    public Flux<ArrayList> getCart(@PathVariable String mail) {
        return Flux.just(dao.getCart(mail));
    }

    @PostMapping("/cart/{mail}/add")
    public Mono<Void> addProduct(@PathVariable String mail,@RequestParam("id") int product){
        dao.addToCart(mail,product,webClientBuilder);
        return Mono.empty();
    }

    @PostMapping("/cart/{mail}/send")
    public Mono<String> sendToDelivery(@PathVariable String mail){
        return Mono.just(dao.sendToDelivery(mail,webClientBuilder));
    }

    private void waitFor2Seconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
