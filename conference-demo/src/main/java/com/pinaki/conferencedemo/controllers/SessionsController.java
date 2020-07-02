package com.pinaki.conferencedemo.controllers;

import com.pinaki.conferencedemo.models.Session;
import com.pinaki.conferencedemo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Respond to payloads incoming and outgoing as JSON REST endpoints
@RequestMapping("/api/v1/sessions") // tells router what mapping URL will look like. Route path in " ". All requests to this URL will be send to this controller
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    // create list endpoint. Return all sessions when called
    @GetMapping // Annotation for HTTP REST verb
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    // Get specific method by ID
    @GetMapping // return 200 status code by default
    @RequestMapping("{id}")     // in addition to class request mapping
    public Session get(@PathVariable Long id) {
        return sessionRepository.getOne(id);
    }

    // Create endpoint for CRUD's C
    // Create a new session and save in database after we pass session info to the API endpoint

    @PostMapping    // returns 200 status code by default // Requires HTTP verb POST to be presented with the API call
    @ResponseStatus(HttpStatus.CREATED) // specify exact response that we want to occur (CREATED = 201)
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session);
    }

    // Delete endpoint
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)  // requires HTTP verb DELETE presented with this API endpoint
    public void delete(@PathVariable Long id) {
        // Also need to check for children records before deleting
        sessionRepository.deleteById(id);
    }

    // Update endpoint
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session) {
        //because this is a PUT, we expect all attributes to be passed in. A PATCH would only need what has changed.
        //TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Session existingSession = sessionRepository.getOne(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }
}
