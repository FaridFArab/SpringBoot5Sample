package com.example.springbootdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@SpringBootApplication
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx){
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for(String beanName: beanNames){
                System.out.println(beanName);
            }
        };
    }

}
class Coffee{
    private final String id;
    private String name;

    public Coffee(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Coffee(String name){
        this(UUID.randomUUID().toString(), name);
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}

//@RestController
//class RestApiDemonController{
//    private List<Coffee> coffees = new ArrayList<>();
//    public RestApiDemonController(){
//        coffees.addAll(List.of(
//                new Coffee("Cereza"),
//                new Coffee("Ganador"),
//                new Coffee("Lareno"),
//                new Coffee("Tres Pontas")
//                ));
//    }
//    @RequestMapping(value="/coffees", method = RequestMethod.GET)
//    Iterable<Coffee> getCoffees(){
//        return coffees;
//    }
//}

@RestController
@RequestMapping("/coffees")
class RestApiDemonController{
    private List<Coffee> coffees = new ArrayList<>();
    public RestApiDemonController(){
        coffees.addAll(List.of(
                new Coffee("Cereza"),
                new Coffee("Ganador"),
                new Coffee("Lareno"),
                new Coffee("Tres Pontas")
        ));
    }
    @GetMapping
    Iterable<Coffee> getCoffees(){
        return coffees;
    }
    @GetMapping("/{id}")
    Optional<Coffee> getCoffeeById(@PathVariable String id){
        for (Coffee c: coffees){
            if (c.getId().equals(id)){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @PostMapping()
    Coffee postCoffee(@RequestBody Coffee coffee){
        coffees.add(coffee);
        return coffee;
    }
//    @PutMapping("/{id}")
//    Coffee putCoffee(@PathVariable String id, @RequestBody Coffee coffee){
//        int coffeeIndex = -1;
//        for(Coffee c: coffees){
//            if(c.getId().equals(id)){
//                coffeeIndex = coffees.indexOf(c);
//                coffees.set(coffeeIndex, coffee);
//            }
//        }
//        return (coffeeIndex == 1) ? postCoffee(coffee): coffee;
//    }
    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee){
        int coffeeIndex = -1;
        for (Coffee c: coffees){
            if(c.getId().equals(id)){
                coffeeIndex = coffees.indexOf(c);
                coffees.set(coffeeIndex, coffee);
            }
        }
        return (coffeeIndex == 1)?
                new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED):
                new ResponseEntity<>(coffee, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    String deleteCoffee(@PathVariable String id){
//        coffees.removeIf(c -> c.getId().equals(id));
        for(Coffee c: coffees){
            if(c.getId().equals(id)){
                coffees.removeIf(d -> d.getId().equals(id));
                return "Delete Completed";
            }
        }
        return "Item not found";
    }
}
