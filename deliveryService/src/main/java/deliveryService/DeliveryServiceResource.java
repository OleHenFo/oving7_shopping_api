package deliveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DeliveryServiceResource implements ApplicationListener<ApplicationReadyEvent> {
    Map<String,String> status = new HashMap<>();
    private Map<String,ArrayList<ProductServiceBean>> orders = new HashMap<>();

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
    }

    @GetMapping("/status/{id}")
    public Mono<String> getStatus(@PathVariable String id){
        if (status.containsKey(id)) {
            return Mono.just(status.get(id));
        } else {
            return Mono.empty();
        }
    }

    @PostMapping("/deliver/{id}")
    public Mono deliverOrder(@PathVariable String id){
        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(4000);
                    status.put(id,"Under Delivery");
                    Thread.sleep(4000);
                    status.put(id,"Delivered!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        return Mono.empty();
    }

    @PostMapping("/create/{id}")
    public Mono createOrder(@PathVariable String id, @RequestBody ArrayList<ProductServiceBean> products){
        System.out.println(id);
        orders.put(id,products);
        status.put(id,"Pending");
        return Mono.just(id);
    }

    private void waitFor2Seconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
