package pl.itacademy.tictac;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.itacademy.tictac.domain.Player;

@Controller
@RequestMapping(path = "/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/register")
    public ResponseEntity<NewPlayerResponse> registerNewPlayer(@RequestParam("name") String name, @RequestParam("password") String password) {
        Player newPlayer = playerService.registerPlayer(name, password);
        return ResponseEntity.ok(NewPlayerResponse.fromPlayer(newPlayer));
    }

    @PostMapping("/register2")
    public ResponseEntity<NewPlayerResponse> registerNewPlayer2(@RequestBody NewPlayerRequest request) {
        Player newPlayer = playerService.registerPlayer(request.getName(), request.getPassword());
        return ResponseEntity.ok(NewPlayerResponse.fromPlayer(newPlayer));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class NewPlayerResponse {
        private String name;

        public static NewPlayerResponse fromPlayer(Player player) {
            return new NewPlayerResponse(player.getName());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class NewPlayerRequest {
        private String name;
        private String password;
    }
}
