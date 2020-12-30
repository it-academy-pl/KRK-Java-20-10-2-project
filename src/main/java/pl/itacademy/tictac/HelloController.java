package pl.itacademy.tictac;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<Hello> helloWebWorld() {
        Hello hello = new Hello("World");
        return ResponseEntity.ok(hello);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Hello> helloWebWorld(@PathVariable("name") String name) {
        Hello hello = new Hello(name);
        return ResponseEntity.ok(hello);
    }

}
