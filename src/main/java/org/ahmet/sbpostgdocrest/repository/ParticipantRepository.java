package org.ahmet.sbpostgdocrest.repository;

import org.ahmet.sbpostgdocrest.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByEmail(String email);
}
