package io.conductor.demos.kafka.jsonschema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty
    public String firstName;

    @JsonProperty
    public String lastName;

    @JsonProperty
    public int age;

    public User() {}

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }


    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.age;
    }
}