package ru.otus.hw.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.ClientDto;
import ru.otus.hw.services.ClientService;

@Controller
@RequiredArgsConstructor
public class ClientsController {

    private final ClientService clientService;

    @GetMapping("/clients")
    public String listAuthorsPage(Model model) {
        List<ClientDto> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/clients/new")
    public String getCreatePage(Model model) {
        model.addAttribute("client", new ClientDto());
        return "clientCreate";
    }

    @PostMapping("/clients/create")
    public String createClient(@ModelAttribute("client") ClientDto client) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/clients/{id}/edit")
    public String getEditPage(@PathVariable Long id, Model model) {
        ClientDto client = clientService.getClient(id);
        model.addAttribute("client", client);
        return "clientEdit";
    }

    @PostMapping("/clients/edit")
    public String editClient(@ModelAttribute("client") ClientDto client, Model model) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @PostMapping("/clients/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }
}
