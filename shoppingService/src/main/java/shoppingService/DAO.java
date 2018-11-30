package shoppingService;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DAO {
    static Map<String, ArrayList<ProductServiceBean>> map;

    public DAO() {
        map = new HashMap<>();

    }

    public ArrayList getCart(String mail){
        return map.getOrDefault(mail, new ArrayList<>());
    }

    public void addToCart(String mail, int product, WebClient.Builder w){
        if (!map.containsKey(mail)){
            map.put(mail,new ArrayList<>());
        }

        Mono<ProductServiceBean> bean = w.build().get().uri("http://productService/products/"+product).retrieve().bodyToMono(ProductServiceBean.class);
        bean.subscribe(p -> map.get(mail).add(p));
    }

    public String sendToDelivery(String mail, WebClient.Builder w){
        ArrayList<ProductServiceBean> payload = map.get(mail);
        int id = 200;
        WebClient.RequestHeadersSpec p = w.build().post().uri("http://deliveryService/create/"+id).body(BodyInserters.fromObject(payload));
        p.retrieve().bodyToMono(Void.class).subscribe();
        return ""+id;
    }
}
