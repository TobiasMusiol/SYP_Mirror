package de.thkoeln.syp.iot_etage.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import de.thkoeln.syp.iot_etage.domain.entity.Event;

/**
 * EventRepository Interface
 */
// @RepositoryRestResource(exported = false)
public interface EventRepository extends CrudRepository<Event, Long> {

}