package de.thkoeln.syp.iot_etage.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.thkoeln.syp.iot_etage.domain.entity.EventData;

/**
 * EventRepository Interface
 */
// @RepositoryRestResource(exported = false)
public interface EventRepository extends JpaRepository<EventData, Long> {

  public EventData findTopByUidAndActionOrderByTimestampDesc(int uid, String action);
}