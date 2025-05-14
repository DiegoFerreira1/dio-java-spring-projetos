package dio.projeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dio.projeto.model.User;
import dio.projeto.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> listarUsuarios() {
        return userService.listarUsuarios();
    }

    @PostMapping
    public User salvarUsuario(@RequestBody User usuario) {
        return userService.salvarUsuario(usuario);
    }

    @GetMapping("/{id}")
    public User buscarUsuarioPorId(@PathVariable Long id) {
        return userService.buscarUsuarioPorId(id);
    }
}
