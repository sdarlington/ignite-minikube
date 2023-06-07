package com.gridgain.ps.k8s.igniteclient;

import org.apache.ignite.client.IgniteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private IgniteClient ignite;

    @GetMapping("/")
    public ResponseEntity<PeopleEntity> index() {
        var cache = ignite.getOrCreateCache("PERSON");
        if (cache != null) {
            return ResponseEntity.ok(new PeopleEntity(cache.size()));
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonEntity> person(@PathVariable Long id) {
        var cache = ignite.<Long,Person>getOrCreateCache("PERSON");
        if (cache != null) {
            var person = cache.get(id);
            if (person != null) {
                return ResponseEntity.ok(new PersonEntity(id, person));
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<PersonEntity> create(@RequestBody PersonEntity person) {
        var cache = ignite.<Long,Person>getOrCreateCache("PERSON");
        if (cache != null) {
            cache.put(person.getId(), person.person() );
            return ResponseEntity.ok(person);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    private static class PeopleEntity {
        private final long count;

        public PeopleEntity(long count) {
            this.count = count;
        }

        public long getCount() {
            return count;
        }
    }

    private static class PersonEntity {
        private final Long id;
        private final String name;
        private final Integer height;

        public PersonEntity() {
            this.id = null;
            this.name = null;
            this.height = null;
        }
        public PersonEntity(long id, String name, int height) {
            this.id = id;
            this.name = name;
            this.height = height;
        }

        public PersonEntity(Long id, Person person) {
            this.id = id;
            this.name = person.getName();
            this.height = person.getHeight();
        }

        public Person person() {
            return new Person(this.getName(), this.getHeight());
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getHeight() {
            return height;
        }
    }
}
