package com.saniazt.restapi.RestApp1.repositories;

import com.saniazt.restapi.RestApp1.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
