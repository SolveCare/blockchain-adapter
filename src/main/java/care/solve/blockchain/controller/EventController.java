package care.solve.blockchain.controller;

import care.solve.blockchain.service.LedgerService;
import care.solve.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    private LedgerService ledgerService;

    @Autowired
    public EventController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> publishEvent(@RequestBody Event event) throws JsonProcessingException {
        byte[] responseBytes = ledgerService.saveEvent(event);

        String respMsg = new String(responseBytes);
        return ResponseEntity.ok(ImmutableMap.of("ResponseMessage", respMsg));
    }

    @GetMapping("{eventId}")
    public Event getEvent(@PathVariable String eventId) throws IOException {
        return ledgerService.getEvent(eventId);
    }
}
