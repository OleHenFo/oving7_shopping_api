package productService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
public class ProductServiceResource implements ApplicationListener<ApplicationReadyEvent> {
    private static ArrayList<ProductServiceBean> products;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceResource.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        products = new ArrayList<>();
        products.add(new ProductServiceBean(0,"Spiker",20));
        products.add(new ProductServiceBean(1,"Ost",30));
        products.add(new ProductServiceBean(2,"Banan",15));
        products.add(new ProductServiceBean(3,"Brettspill",120));
    }

    @GetMapping("/products")
    public Flux getAll() {
        return Flux.fromIterable(products);
    }

    @GetMapping("/products/{id}")
    public Mono<ProductServiceBean> getProduct(@PathVariable int id){
        return Mono.just(products.get(id));
    }

    @GetMapping("/products/{id}/price")
    public Mono getProductPrice(@PathVariable int id){
        return Mono.just(products.get(id).getPrice());
    }

    private void waitFor2Seconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
